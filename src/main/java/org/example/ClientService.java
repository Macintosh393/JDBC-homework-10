package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final Connection connection;

    public ClientService() {
        this.connection = Database.getInstance().getConnection();
    }

    public long create(String name) {
        validateName(name);

        String sql = "INSERT INTO client (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new RuntimeException("Creating client failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during client creation", e);
        }
    }

    public String getById(long id) {
        validateId(id);

        String sql = "SELECT name FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                } else {
                    throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during client retrieval", e);
        }
    }

    public void setName(long id, String name) {
        validateId(id);
        validateName(name);

        String sql = "UPDATE client SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setLong(2, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during client update", e);
        }
    }

    public void deleteById(long id) {
        validateId(id);

        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during client deletion", e);
        }
    }

    public List<Client> listAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name FROM client";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                clients.add(new Client(id, name));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during client listing", e);
        }
        return clients;
    }

    private void validateName(String name) {
        if (name == null || name.trim().length() < 2 || name.trim().length() > 1000) {
            throw new IllegalArgumentException("Client name must be non-null and between 2 and 1000 characters.");
        }
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Client ID must be greater than 0.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting ClientService tests...");
        ClientService service = new ClientService();

        // 1. List initial clients
        System.out.println("\nInitial clients list:");
        List<Client> clients = service.listAll();
        for (Client c : clients) {
            System.out.println(c);
        }

        // 2. Create client
        System.out.println("\nTesting Create Client...");
        long newId = service.create("SpaceX");
        System.out.println("Successfully created client SpaceX with ID: " + newId);

        // 3. Get client by ID
        System.out.println("\nTesting Get Client by ID...");
        String name = service.getById(newId);
        System.out.println("Client name with ID " + newId + ": " + name);
        if (!"SpaceX".equals(name)) {
            throw new AssertionError("Client name does not match SpaceX");
        }

        // 4. Update client name
        System.out.println("\nTesting Update Client Name...");
        service.setName(newId, "SpaceX Redesigned");
        String updatedName = service.getById(newId);
        System.out.println("Updated client name with ID " + newId + ": " + updatedName);
        if (!"SpaceX Redesigned".equals(updatedName)) {
            throw new AssertionError("Updated name does not match SpaceX Redesigned");
        }

        // 5. Delete client by ID
        System.out.println("\nTesting Delete Client...");
        service.deleteById(newId);
        System.out.println("Successfully deleted client with ID: " + newId);

        // 6. Verify client is deleted
        System.out.println("\nTesting verification after deletion (should throw exception)...");
        try {
            service.getById(newId);
            throw new AssertionError("Expected IllegalArgumentException but none was thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("Success! Caught expected exception: " + e.getMessage());
        }

        // 7. Test invalid input validations
        System.out.println("\nTesting input validation with too short name (1 character)...");
        try {
            service.create("A");
            throw new AssertionError("Expected IllegalArgumentException but none was thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("Success! Caught expected exception: " + e.getMessage());
        }

        System.out.println("\nTesting input validation with null name...");
        try {
            service.create(null);
            throw new AssertionError("Expected IllegalArgumentException but none was thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("Success! Caught expected exception: " + e.getMessage());
        }

        System.out.println("\nTesting input validation with negative ID...");
        try {
            service.getById(-5);
            throw new AssertionError("Expected IllegalArgumentException but none was thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("Success! Caught expected exception: " + e.getMessage());
        }

        // 8. Print all clients after tests to verify original data is intact or cleaned up
        System.out.println("\nFinal clients list:");
        clients = service.listAll();
        for (Client c : clients) {
            System.out.println(c);
        }

        Database.getInstance().close();
        System.out.println("\nAll ClientService tests completed successfully and database connection closed.");
    }
}
