package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePopulateService {
    public static void main(String[] args) {
        String sqlFilePath = "sql/populate_db.sql";
        try {
            String sql = Files.readString(Path.of(sqlFilePath));
            
            Connection conn = Database.getInstance().getConnection();
            try (Statement stmt = conn.createStatement()) {
                // Split queries by semicolon to execute them one by one
                String[] queries = sql.split(";");
                for (String query : queries) {
                    String trimmedQuery = query.trim();
                    if (!trimmedQuery.isEmpty()) {
                        stmt.executeUpdate(trimmedQuery);
                    }
                }
                System.out.println("Database successfully populated.");
            }
        } catch (IOException e) {
            System.err.println("Failed to read SQL populate file: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL Exception during population: " + e.getMessage());
        }
    }
}
