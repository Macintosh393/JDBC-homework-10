package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            // Use H2 file-based database to persist state between runs
            String url = "jdbc:h2:./testdb";
            String user = "sa";
            String password = "";
            this.connection = DriverManager.getConnection(url, user, password);

            // Run Flyway migrations
            org.flywaydb.core.Flyway flyway = org.flywaydb.core.Flyway.configure()
                .dataSource(url, user, password)
                .load();
            flyway.migrate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to H2 database", e);
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
