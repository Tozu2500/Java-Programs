package com.demo;

import java.io.*;
import java.net.*;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private MessagingServer server;
    private String username;
    private boolean connected = true;
    
    public ClientHandler(Socket socket, MessagingServer server) {
        this.clientSocket = socket;
        this.server = server;
        
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            // Send recent messages to new client
            List<ChatMessage> recentMessages = server.getRecentMessages();
            try {
            	recentMessages = server.getRecentMessages();
            } catch (Exception e) {
            	System.err.println("Failed to fetch recent messages: " + e.getMessage());
            	e.printStackTrace();
            }
            try {
            	 for (ChatMessage msg : recentMessages) {
                     writer.println("HISTORY:" + msg.toString());
            	 }} catch (Exception e) {
            	e.printStackTrace();
            	 }
            
            String inputLine;
            while (connected && (inputLine = reader.readLine()) != null) {
                if (inputLine.startsWith("USERNAME:")) {
                    username = inputLine.substring(9);
                    server.broadcastMessage("SYSTEM:" + username + " joined the chat", this);
                } else if (inputLine.startsWith("MESSAGE:")) {
                    String message = inputLine.substring(8);
                    if (username != null) {
                        server.saveMessage(username, message);
                        String formattedMessage = "MESSAGE:" + username + ": " + message;
                        server.broadcastMessage(formattedMessage, this);
                    }
                }
            }
        } catch (IOException e) {
            if (connected) {
                System.out.println("Client disconnected unexpectedly");
                e.printStackTrace();
            }
        } catch (Exception e) {
        	System.err.println("Unexpected error in client handler: " + e.getMessage());
        	e.printStackTrace();
        } finally {
            disconnect();
        }
    }
    
    public void sendMessage(String message) {
        if (connected && writer != null) {
            writer.println(message);
        }
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void disconnect() {
        connected = false;
        try {
            if (username != null) {
                server.broadcastMessage("SYSTEM:" + username + " left the chat", this);
            }
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
        }
    }
}