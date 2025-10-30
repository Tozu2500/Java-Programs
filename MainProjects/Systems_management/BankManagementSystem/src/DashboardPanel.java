
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel welcomeLabel;
    private JLabel totalBalanceLabel;
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JButton createAccountButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton viewHistoryButton;
    private JButton calculateInterestButton;
    private JButton profileButton;
    private JButton adminButton;
    private JButton logoutButton;
    
    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        
        User currentUser = BankSystem.getInstance().getCurrentUser();
        
        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(25, 25, 112));
        
        totalBalanceLabel = new JLabel();
        totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalBalanceLabel.setForeground(new Color(34, 139, 34));
        updateTotalBalance();
        
        JPanel topLeftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        topLeftPanel.setBackground(new Color(240, 248, 255));
        topLeftPanel.add(welcomeLabel);
        topLeftPanel.add(totalBalanceLabel);
        
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setBackground(new Color(240, 248, 255));
        
        profileButton = createStyledButton("Profile", new Color(100, 149, 237));
        profileButton.addActionListener(e -> mainFrame.showProfile());
        topRightPanel.add(profileButton);
        
        if (currentUser.isAdmin()) {
            adminButton = createStyledButton("Admin Panel", new Color(220, 20, 60));
            adminButton.addActionListener(e -> mainFrame.showAdminPanel());
            topRightPanel.add(adminButton);
        }
        
        logoutButton = createStyledButton("Logout", new Color(178, 34, 34));
        logoutButton.addActionListener(e -> {
            BankSystem.getInstance().logout();
            mainFrame.showLogin();
        });
        topRightPanel.add(logoutButton);
        
        panel.add(topLeftPanel, BorderLayout.WEST);
        panel.add(topRightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tableTitle = new JLabel("Your Accounts");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(25, 25, 112));
        panel.add(tableTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"Account Number", "Type", "Balance", "Interest Rate", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        accountsTable = new JTable(tableModel);
        accountsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        accountsTable.setRowHeight(25);
        accountsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        accountsTable.getTableHeader().setBackground(new Color(176, 196, 222));
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(accountsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshAccountsTable();
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        createAccountButton = createStyledButton("Create Account", new Color(60, 179, 113));
        createAccountButton.addActionListener(e -> showCreateAccountDialog());
        panel.add(createAccountButton);
        
        depositButton = createStyledButton("Deposit", new Color(70, 130, 180));
        depositButton.addActionListener(e -> showDepositDialog());
        panel.add(depositButton);
        
        withdrawButton = createStyledButton("Withdraw", new Color(255, 140, 0));
        withdrawButton.addActionListener(e -> showWithdrawDialog());
        panel.add(withdrawButton);
        
        transferButton = createStyledButton("Transfer", new Color(147, 112, 219));
        transferButton.addActionListener(e -> showTransferDialog());
        panel.add(transferButton);
        
        viewHistoryButton = createStyledButton("View History", new Color(100, 149, 237));
        viewHistoryButton.addActionListener(e -> showTransactionHistory());
        panel.add(viewHistoryButton);
        
        calculateInterestButton = createStyledButton("Calculate Interest", new Color(34, 139, 34));
        calculateInterestButton.addActionListener(e -> calculateInterest());
        panel.add(calculateInterestButton);
        
        JButton closeAccountButton = createStyledButton("Close Account", new Color(220, 20, 60));
        closeAccountButton.addActionListener(e -> showCloseAccountDialog());
        panel.add(closeAccountButton);
        
        JButton refreshButton = createStyledButton("Refresh", new Color(105, 105, 105));
        refreshButton.addActionListener(e -> refreshData());
        panel.add(refreshButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    public void refreshAccountsTable() {
        tableModel.setRowCount(0);
        User currentUser = BankSystem.getInstance().getCurrentUser();
        List<BankAccount> accounts = currentUser.getAccounts();
        
        for (BankAccount account : accounts) {
            Object[] row = {
                account.getAccountNumber(),
                account.getAccountType(),
                String.format("$%.2f", account.getBalance()),
                String.format("%.2f%%", account.getInterestRate()),
                account.isActive() ? "Active" : "Closed"
            };
            tableModel.addRow(row);
        }
        
        updateTotalBalance();
    }
    
    private void updateTotalBalance() {
        User currentUser = BankSystem.getInstance().getCurrentUser();
        double total = currentUser.getTotalBalance();
        totalBalanceLabel.setText(String.format("Total Balance: $%.2f", total));
    }
    
    private void showCreateAccountDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Create New Account", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel typeLabel = new JLabel("Account Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(typeLabel, gbc);
        
        String[] accountTypes = {"Savings", "Checking", "Business"};
        JComboBox<String> typeCombo = new JComboBox<>(accountTypes);
        typeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        dialog.add(typeCombo, gbc);
        
        JLabel balanceLabel = new JLabel("Initial Deposit:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(balanceLabel, gbc);
        
        JTextField balanceField = new JTextField(15);
        balanceField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        dialog.add(balanceField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createBtn = new JButton("Create");
        createBtn.setFont(new Font("Arial", Font.BOLD, 12));
        createBtn.addActionListener(e -> {
            try {
                double initialBalance = Double.parseDouble(balanceField.getText());
                if (initialBalance < 0) {
                    JOptionPane.showMessageDialog(dialog,
                        "Initial deposit cannot be negative.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String accountType = (String) typeCombo.getSelectedItem();
                User currentUser = BankSystem.getInstance().getCurrentUser();
                BankAccount account = BankSystem.getInstance().createAccount(
                    currentUser, accountType, initialBalance);
                
                if (account != null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Account created successfully!\nAccount Number: " + 
                        account.getAccountNumber(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAccountsTable();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter a valid amount.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(createBtn);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showDepositDialog() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first.",
                "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String input = JOptionPane.showInputDialog(this,
            "Enter amount to deposit:",
            "Deposit Money",
            JOptionPane.PLAIN_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (selectedAccount.deposit(amount)) {
                    BankSystem.getInstance().saveData();
                    JOptionPane.showMessageDialog(this,
                        String.format("Successfully deposited $%.2f", amount),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAccountsTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Deposit failed. Please check the amount.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid amount.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showWithdrawDialog() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first.",
                "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String input = JOptionPane.showInputDialog(this,
            "Enter amount to withdraw:",
            "Withdraw Money",
            JOptionPane.PLAIN_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (selectedAccount.withdraw(amount)) {
                    BankSystem.getInstance().saveData();
                    JOptionPane.showMessageDialog(this,
                        String.format("Successfully withdrew $%.2f", amount),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAccountsTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Withdrawal failed. Insufficient balance or below minimum balance.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid amount.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showTransferDialog() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first.",
                "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Transfer Money", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel accountLabel = new JLabel("Target Account Number:");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(accountLabel, gbc);
        
        JTextField accountField = new JTextField(15);
        accountField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        dialog.add(accountField, gbc);
        
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(amountLabel, gbc);
        
        JTextField amountField = new JTextField(15);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        dialog.add(amountField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton transferBtn = new JButton("Transfer");
        transferBtn.setFont(new Font("Arial", Font.BOLD, 12));
        transferBtn.addActionListener(e -> {
            try {
                String targetAccountNumber = accountField.getText().trim();
                double amount = Double.parseDouble(amountField.getText());
                
                BankAccount targetAccount = BankSystem.getInstance()
                    .findAccountByNumber(targetAccountNumber);
                
                if (targetAccount == null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Target account not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (targetAccount.equals(selectedAccount)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Cannot transfer to the same account.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (selectedAccount.transfer(targetAccount, amount)) {
                    BankSystem.getInstance().saveData();
                    JOptionPane.showMessageDialog(dialog,
                        String.format("Successfully transferred $%.2f", amount),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAccountsTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Transfer failed. Insufficient balance or invalid amount.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter a valid amount.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(transferBtn);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showTransactionHistory() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first.",
                "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        mainFrame.showTransactionHistory(selectedAccount);
    }
    
    private void calculateInterest() {
        int result = JOptionPane.showConfirmDialog(this,
            "Calculate interest for all accounts?",
            "Calculate Interest",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            User currentUser = BankSystem.getInstance().getCurrentUser();
            for (BankAccount account : currentUser.getAccounts()) {
                account.calculateInterest();
            }
            BankSystem.getInstance().saveData();
            refreshAccountsTable();
            JOptionPane.showMessageDialog(this,
                "Interest calculated for all accounts.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showCloseAccountDialog() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first.",
                "No Account Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to close this account?\n" +
            "Account balance must be $0.00 to close.",
            "Close Account",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            User currentUser = BankSystem.getInstance().getCurrentUser();
            if (BankSystem.getInstance().deleteAccount(currentUser, selectedAccount)) {
                JOptionPane.showMessageDialog(this,
                    "Account closed successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAccountsTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cannot close account. Balance must be $0.00",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private BankAccount getSelectedAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        
        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        User currentUser = BankSystem.getInstance().getCurrentUser();
        return currentUser.getAccountByNumber(accountNumber);
    }
    
    private void refreshData() {
        refreshAccountsTable();
        JOptionPane.showMessageDialog(this,
            "Data refreshed successfully.",
            "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }
}