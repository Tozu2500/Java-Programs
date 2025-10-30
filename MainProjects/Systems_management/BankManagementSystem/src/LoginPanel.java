import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Bank Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Tozus Banking Solutions");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10);
        centerPanel.add(subtitleLabel, gbc);
        
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.insets = new Insets(10, 10, 10, 10);
        loginGbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginGbc.gridx = 0;
        loginGbc.gridy = 0;
        loginGbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, loginGbc);
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        loginGbc.gridx = 0;
        loginGbc.gridy = 1;
        loginPanel.add(usernameField, loginGbc);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginGbc.gridx = 0;
        loginGbc.gridy = 2;
        loginPanel.add(passwordLabel, loginGbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        loginGbc.gridx = 0;
        loginGbc.gridy = 3;
        loginPanel.add(passwordField, loginGbc);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(new LoginActionListener());
        buttonPanel.add(loginButton);
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(new RegisterActionListener());
        buttonPanel.add(registerButton);
        
        loginGbc.gridx = 0;
        loginGbc.gridy = 4;
        loginGbc.insets = new Insets(20, 10, 10, 10);
        loginPanel.add(buttonPanel, loginGbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(loginPanel, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
        
        passwordField.addActionListener(e -> loginButton.doClick());
    }
    
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginPanel.this,
                    "Please enter both username and password.",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BankSystem system = BankSystem.getInstance();
            if (system.login(username, password)) {
                mainFrame.showDashboard();
            } else {
                JOptionPane.showMessageDialog(LoginPanel.this,
                    "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        }
    }
    
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.showRegistration();
        }
    }
}