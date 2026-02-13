package com.cargarage.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * DatabaseConnection Singleton for managing a single JDBC connection.
 * Provides centralized connection management with automatic reconnection capability.
 * Implements shutdown hook for clean connection closure on application exit.
 */

public class DatabaseConnection {
    private static final Logger log = Logger.getLogger(DatabaseConnection.class.getName());
    private static DatabaseConnection instance;
    private Connection connection;

    private String url;
    private String username;
    private String password;
    private String driver;

    private DatabaseConnection() {
        loadConfiguration();
        initializeConnection();
        registerShutdownHook();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void loadConfiguration() {
        this.url = System.getenv("DB_URL") != null ? 
            System.getenv("DB_URL") : "jdbc:h2:./data/garage";
        this.username = System.getenv("DB_USER") != null ? 
            System.getenv("DB_USER") : "sa";
        this.password = System.getenv("DB_PASSWORD") != null ? 
            System.getenv("DB_PASSWORD") : "";
        this.driver = System.getenv("DB_DRIVER") != null ? 
            System.getenv("DB_DRIVER") : "org.h2.Driver";
        
        log.info("Database URL: " + this.url);
        log.info("Database Driver: " + this.driver);
    }

    private void initializeConnection() {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, username, password);
            log.info("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            log.severe("JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            log.severe("Failed to establish database connection: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            log.info("Connection lost, attempting to reconnect...");
            reconnect();
        }
        return connection;
    }

    public void reconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.warning("Error closing old connection: " + e.getMessage());
        }
        
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            log.info("Reconnection successful");
        } catch (SQLException e) {
            log.severe("Reconnection failed: " + e.getMessage());
            throw e;
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    log.info("Database connection closed cleanly");
                }
            } catch (SQLException e) {
                log.warning("Error closing database connection: " + e.getMessage());
            }
        }));
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            log.info("Database connection closed");
        }
    }
}