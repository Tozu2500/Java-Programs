package com.demo;

import java.sql.Timestamp;

public class ChatMessage {
    private String username;
    private String message;
    private Timestamp timestamp;
    
    public ChatMessage(String username, String message, Timestamp timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public Timestamp getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", 
            timestamp.toString().substring(11, 19), username, message);
    }
}
