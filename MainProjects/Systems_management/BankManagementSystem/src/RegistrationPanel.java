
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegistrationPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton registerButton;
    private JButton backButton;
    
    public RegistrationPanel(MainFrame mainFrame) {
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
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(8, 10, 8, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        
        addFormField(formPanel, formGbc, "Username:", 0);
        usernameField = new JTextField(25);
        styleTextField(usernameField);
        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formPanel.add(usernameField, formGbc);
        
        addFormField(formPanel, formGbc, "Password:", 2);
        passwordField = new JPasswordField(25);
        styleTextField(passwordField);
        formGbc.gridy = 3;
        formPanel.add(passwordField, formGbc);
        
        addFormField(formPanel, formGbc, "Confirm Password:", 4);
        confirmPasswordField = new JPasswordField(25);
        styleTextField(confirmPasswordField);
        formGbc.gridy = 5;
        formPanel.add(confirmPasswordField, formGbc);
        
        addFormField(formPanel, formGbc, "Full Name:", 6);
        fullNameField = new JTextField(25);
        styleTextField(fullNameField);
        formGbc.gridy = 7;
        formPanel.add(fullNameField, formGbc);
        
        addFormField(formPanel, formGbc, "Email:", 8);
        emailField = new JTextField(25);
        styleTextField(emailField);
        formGbc.gridy = 9;
        formPanel.add(emailField, formGbc);
        
        addFormField(formPanel, formGbc, "Phone Number:", 10);
        phoneField = new JTextField(25);
        styleTextField(phoneField);
        formGbc.gridy = 11;
        formPanel.add(phoneField, formGbc);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(new RegisterActionListener());
        buttonPanel.add(registerButton);
        
        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> mainFrame.showLogin());
        buttonPanel.add(backButton);
        
        formGbc.gridy = 12;
        formGbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(buttonPanel, formGbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 10, 10);
        centerPanel.add(formPanel, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || 
                email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationPanel.this,
                    "All fields are required.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegistrationPanel.this,
                    "Passwords do not match.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(RegistrationPanel.this,
                    "Password must be at least 6 characters long.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(RegistrationPanel.this,
                    "Please enter a valid email address.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BankSystem system = BankSystem.getInstance();
            if (system.registerUser(username, password, fullName, email, phone)) {
                JOptionPane.showMessageDialog(RegistrationPanel.this,
                    "Registration successful! You can now login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                mainFrame.showLogin();
            } else {
                JOptionPane.showMessageDialog(RegistrationPanel.this,
                    "Username already exists. Please choose another.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        fullNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }
}
