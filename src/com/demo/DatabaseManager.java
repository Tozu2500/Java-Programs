package com.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
 
    private static final String DB_URL = "jdbc:mysql://localhost:3306/messaging_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    public DatabaseManager() {
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connecting to database: " + DB_URL);
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS messages (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL,
                    message TEXT NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            
            Statement stmt = conn.createStatement();
            stmt.execute(createTableSQL);
            System.out.println("Database table 'messages' ready.");
        } catch (SQLException e) {
            System.err.println("=== DATABASE CONNECTION ERROR ===");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            System.err.println("Error: " + e.getMessage());
            System.err.println("Please check:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'messaging_app' exists");
            System.err.println("3. Username and password are correct");
            System.err.println("4. MySQL JDBC driver is in classpath");
            System.err.println("===============================");
            throw new RuntimeException("Database connection failed", e);
        }
    }
    
    public void saveMessage(String username, String message) {
        String sql = "INSERT INTO messages (username, message) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<ChatMessage> getRecentMessages(int limit) {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT username, message, timestamp FROM messages ORDER BY timestamp DESC LIMIT ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(0, new ChatMessage(
                    rs.getString("username"),
                    rs.getString("message"),
                    rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
}