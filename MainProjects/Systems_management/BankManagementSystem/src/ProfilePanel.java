
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
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ProfilePanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton updateProfileButton;
    private JButton changePasswordButton;
    private JButton backButton;
    
    public ProfilePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 248, 255));
        
        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> mainFrame.showDashboard());
        topPanel.add(backButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(new Color(240, 248, 255));
        
        JPanel profileInfoPanel = createProfileInfoPanel();
        centerPanel.add(profileInfoPanel);
        
        JPanel passwordPanel = createPasswordPanel();
        centerPanel.add(passwordPanel);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createProfileInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        User currentUser = BankSystem.getInstance().getCurrentUser();
        
        JLabel sectionTitle = new JLabel("Profile Information");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(sectionTitle, gbc);
        
        gbc.gridwidth = 1;
        
        addLabel(panel, gbc, "Username:", 1);
        JLabel usernameValue = new JLabel(currentUser.getUsername());
        usernameValue.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameValue, gbc);
        
        addLabel(panel, gbc, "Full Name:", 2);
        fullNameField = new JTextField(20);
        fullNameField.setText(currentUser.getFullName());
        styleTextField(fullNameField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(fullNameField, gbc);
        
        addLabel(panel, gbc, "Email:", 3);
        emailField = new JTextField(20);
        emailField.setText(currentUser.getEmail());
        styleTextField(emailField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(emailField, gbc);
        
        addLabel(panel, gbc, "Phone:", 4);
        phoneField = new JTextField(20);
        phoneField.setText(currentUser.getPhoneNumber());
        styleTextField(phoneField);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(phoneField, gbc);
        
        addLabel(panel, gbc, "Registered:", 5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        JLabel registeredValue = new JLabel(currentUser.getRegistrationDate().format(formatter));
        registeredValue.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(registeredValue, gbc);
        
        addLabel(panel, gbc, "Account Type:", 6);
        JLabel accountTypeValue = new JLabel(currentUser.isAdmin() ? "Administrator" : "Regular User");
        accountTypeValue.setFont(new Font("Arial", Font.PLAIN, 14));
        accountTypeValue.setForeground(currentUser.isAdmin() ? new Color(220, 20, 60) : new Color(34, 139, 34));
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(accountTypeValue, gbc);
        
        updateProfileButton = new JButton("Update Profile");
        updateProfileButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateProfileButton.setBackground(new Color(60, 179, 113));
        updateProfileButton.setForeground(Color.WHITE);
        updateProfileButton.setFocusPainted(false);
        updateProfileButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        updateProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateProfileButton.addActionListener(new UpdateProfileListener());
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(updateProfileButton, gbc);
        
        return panel;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel sectionTitle = new JLabel("Change Password");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(sectionTitle, gbc);
        
        gbc.gridwidth = 1;
        
        addLabel(panel, gbc, "Current Password:", 1);
        currentPasswordField = new JPasswordField(20);
        styleTextField(currentPasswordField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(currentPasswordField, gbc);
        
        addLabel(panel, gbc, "New Password:", 2);
        newPasswordField = new JPasswordField(20);
        styleTextField(newPasswordField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(newPasswordField, gbc);
        
        addLabel(panel, gbc, "Confirm Password:", 3);
        confirmPasswordField = new JPasswordField(20);
        styleTextField(confirmPasswordField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(confirmPasswordField, gbc);
        
        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setFont(new Font("Arial", Font.BOLD, 14));
        changePasswordButton.setBackground(new Color(255, 140, 0));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        changePasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePasswordButton.addActionListener(new ChangePasswordListener());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(changePasswordButton, gbc);
        
        return panel;
    }
    
    private void addLabel(JPanel panel, GridBagConstraints gbc, String text, int yPos) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(label, gbc);
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private class UpdateProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(ProfilePanel.this,
                    "All fields are required.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(ProfilePanel.this,
                    "Please enter a valid email address.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User currentUser = BankSystem.getInstance().getCurrentUser();
            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phone);
            
            for (BankAccount account : currentUser.getAccounts()) {
                account.setAccountHolderName(fullName);
            }
            
            BankSystem.getInstance().saveData();
            
            JOptionPane.showMessageDialog(ProfilePanel.this,
                "Profile updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private class ChangePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(ProfilePanel.this,
                    "All password fields are required.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User currentUser = BankSystem.getInstance().getCurrentUser();
            
            if (!currentUser.getPassword().equals(currentPassword)) {
                JOptionPane.showMessageDialog(ProfilePanel.this,
                    "Current password is incorrect.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                currentPasswordField.setText("");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(ProfilePanel.this,
                    "New passwords do not match.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                newPasswordField.setText("");
                confirmPasswordField.setText("");
                return;
            }
            
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(ProfilePanel.this,
                    "Password must be at least 6 characters long.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentUser.setPassword(newPassword);
            BankSystem.getInstance().saveData();
            
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
            
            JOptionPane.showMessageDialog(ProfilePanel.this,
                "Password changed successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}