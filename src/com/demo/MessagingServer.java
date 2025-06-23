package com.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MessagingServer {
    private static final int PORT = 8093;
    private Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private DatabaseManager dbManager;
    private ServerSocket serverSocket;
    
    public MessagingServer() {
        dbManager = new DatabaseManager();
    }
    
    public void start() {
        try {
            // Test database connection first
            System.out.println("Testing database connection...");
            dbManager.getRecentMessages(1); // Test database access
            System.out.println("Database connection successful!");
            
            serverSocket = new ServerSocket(PORT);
            System.out.println("=== MESSAGING SERVER STARTED ===");
            System.out.println("Server listening on port: " + PORT);
            System.out.println("Server address: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Waiting for client connections...");
            System.out.println("================================");
            
            while (serverSocket != null && !serverSocket.isClosed()) {
                try {
                	Socket clientSocket = serverSocket.accept();
                	System.out.println("Accepted connection from:" + clientSocket.getRemoteSocketAddress());
                	ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                	clients.add(clientHandler);
                	new Thread(clientHandler).start();
                	
                	System.out.println("New client connected. Total clients: " + clients.size());
                } catch (IOException e) {
                	System.err.println("Error accepting client: " + e.getMessage());
                	e.printStackTrace();
                } catch (Exception e) {
                	System.err.println("Unexpected error: " + e.getMessage());
                	e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            System.err.println("Please check your MySQL connection settings.");
            e.printStackTrace();
        }
    }
    
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender && client.isConnected()) {
                client.sendMessage(message);
            }
        }
    }
    
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected. Total clients: " + clients.size());
    }
    
    public void saveMessage(String username, String message) {
        dbManager.saveMessage(username, message);
    }
    
    public List<ChatMessage> getRecentMessages() {
        return dbManager.getRecentMessages(50);
    }
    
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    	try {
    		MessagingServer server = new MessagingServer();
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            server.start();
    	} catch (Exception e) {
    		System.err.println("Failed to start server:" + e.getMessage());
    		e.printStackTrace();
    	}
    	SwingUtilities.invokeLater(() -> {
           try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
           } catch (Exception e) {
               e.printStackTrace();
           }
           new MessagingClient().setVisible(true);
       });
   }
}
