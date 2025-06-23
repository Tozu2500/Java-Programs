package com.demo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MessagingClient extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8093;
    
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    
    // GUI Components
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton connectButton;
    private JTextField usernameField;
    private boolean connected = false;
    
    public MessagingClient() {
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Java Messaging Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Create components
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Top panel for connection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        topPanel.add(usernameField);
        connectButton = new JButton("Connect");
        topPanel.add(connectButton);
        
        // Bottom panel for messaging
        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setEnabled(false);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        
        // Layout
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event listeners
        connectButton.addActionListener(e -> toggleConnection());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });
    }
    
    private void toggleConnection() {
        if (!connected) {
            connect();
        } else {
            disconnect();
        }
    }
    
    private void connect() {
        username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username!");
            return;
        }
        
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            
            // Send username to server
            writer.println("USERNAME:" + username);
            
            // Start listening for messages
            new Thread(this::listenForMessages).start();
            
            // Update GUI
            connected = true;
            connectButton.setText("Disconnect");
            usernameField.setEnabled(false);
            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            messageField.requestFocus();
            
            appendToChat("Connected to server as " + username);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage());
        }
    }
    
    private void disconnect() {
        if (connected) {
            connected = false;
            try {
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Update GUI
            connectButton.setText("Connect");
            usernameField.setEnabled(true);
            messageField.setEnabled(false);
            sendButton.setEnabled(false);
            appendToChat("Disconnected from server");
        }
    }
    
    private void sendMessage() {
        if (connected && writer != null) {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                writer.println("MESSAGE:" + message);
                appendToChat("You: " + message);
                messageField.setText("");
                messageField.requestFocus();
            }
        }
    }
    
    private void listenForMessages() {
        try {
            String message;
            while (connected && (message = reader.readLine()) != null) {
                final String msg = message;
                SwingUtilities.invokeLater(() -> {
                    if (msg.startsWith("MESSAGE:")) {
                        appendToChat(msg.substring(8));
                    } else if (msg.startsWith("SYSTEM:")) {
                        appendToChat("*** " + msg.substring(7) + " ***");
                    } else if (msg.startsWith("HISTORY:")) {
                        appendToChat(msg.substring(8));
                    }
                });
            }
        } catch (IOException e) {
            if (connected) {
                SwingUtilities.invokeLater(() -> {
                    appendToChat("Connection lost!");
                    disconnect();
                });
            }
        }
    }
    
    private void appendToChat(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
    
    public static void main(String[] args) {
    	
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
    
