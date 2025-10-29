
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JButton viewAccountsButton;
    private JButton deleteUserButton;
    private JButton calculateAllInterestButton;
    private JButton backButton;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Administrator Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 20, 60));
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

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("All Users");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(25, 25, 112));
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        String[] columnNames = {"Username", "Full Name", "Email", "Phone", "Accounts", "Total Balance", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setFont(new Font("Arial", Font.PLAIN, 13));
        usersTable.setRowHeight(25);
        usersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        usersTable.getTableHeader().setBackground(new Color(176, 196, 222));
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        refreshUsersTable();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        viewAccountsButton = createStyledButton("View User Accounts", new Color(70, 130, 180));
        viewAccountsButton.addActionListener(e -> showUserAccounts());
        panel.add(viewAccountsButton);

        deleteUserButton = createStyledButton("Delete User", new Color(220, 20, 60));
        deleteUserButton.addActionListener(e -> deleteSelectedUser());
        panel.add(deleteUserButton);

        calculateAllInterestButton = createStyledButton("Calculate All Interest", new Color(34, 139, 34));
        calculateAllInterestButton.addActionListener(e -> calculateAllInterest());
        panel.add(calculateAllInterestButton);

        JButton refreshButton = createStyledButton("Refresh", new Color(105, 105, 105));
        refreshButton.addActionListener(e -> refreshUsersTable());
        panel.add(refreshButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void refreshUsersTable() {
        tableModel.setRowCount(0);
        List<User> users = BankSystem.getInstance().getAllUsers();

        for (User user : users) {
            Object[] row = {
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAccounts().size(),
                String.format("$%.2f", user.getTotalBalance()),
                user.isAdmin() ? "Admin" : "Regular"
            };
            tableModel.addRow(row);
        }
    }

    private void showUserAccounts() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user first",
                "No user selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        User user = BankSystem.getInstance().getUserByUsername(username);

        if (user == null) {
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Accounts - " + user.getFullName(), true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(700, 400);

        String[] columnNames = {"Account Number", "Type", "Balance", "Interest Rate", "Status"};
        DefaultTableModel accountsModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable accountsTable = new JTable(accountsModel);
        accountsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        accountsTable.setRowHeight(25);
        accountsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        accountsTable.getTableHeader().setBackground(new Color(176, 196, 222));

        for (BankAccount account : user.getAccounts()) {
            Object[] row = {
                account.getAccountNumber(),
                account.getAccountType(),
                String.format("$%.2f", account.getBalance()),
                String.format("$%.2f%%", account.getInterestRate()),
                account.isActive() ? "Active" : "Closed"
            };
            accountsModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton viewHistoryBtn = new JButton("View Transaction History");
        viewHistoryBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewHistoryBtn.addActionListener(e -> {
            int selectedAccountRow = accountsTable.getSelectedRow();
            if (selectedAccountRow == -1) {
                JOptionPane.showMessageDialog(dialog,
                    "Please select an account first",
                    "No account selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String accountNumber = (String) accountsModel.getValueAt(selectedAccountRow, 0);
            BankAccount account = user.getAccountByNumber(accountNumber);

            if (account != null) {
                showAccountTransactionHistory(account);
            }
        });
        buttonPanel.add(viewHistoryBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        closeBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAccountTransactionHistory(BankAccount account) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Transaction History - " + account.getAccountNumber(), true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(800, 500);

        String[] columnNames = {"Date & Time", "Type", "Amount", "Description"};
        DefaultTableModel transModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable transTable = new JTable(transModel);
        transTable.setFont(new Font("Arial", Font.PLAIN, 13));
        transTable.setRowHeight(50);
        transTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        transTable.getTableHeader().setBackground(new Color(176, 196, 222));

        List<Transaction> transactions = account.getTransactionHistory();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction trans = transactions.get(i);
            Object[] row = {
                trans.getFormattedTimestamp(),
                trans.getType().toString(),
                String.format("$%.2f", trans.getAmount()),
                trans.getDescription()
            };
            transModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(transTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user first.",
                "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        User user = BankSystem.getInstance().getUserByUsername(username);

        if (user == null) {
            return;
        }

        if (user.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete administrator accounts.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete user: " + username + "?\n" +
            "User must have no accounts with balance to be deleted.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            if (BankSystem.getInstance().deleteUser(username)) {
                JOptionPane.showMessageDialog(this,
                    "User deleted successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshUsersTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cannot delete this user. User may have accounts with balance.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void calculateAllInterest() {
        int result = JOptionPane.showConfirmDialog(this,
            "Calculate total interest for all accounts in the system?",
            "Calculate Interest",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            BankSystem.getInstance().calculateAllInterests();
            refreshUsersTable();
            JOptionPane.showMessageDialog(this,
                "Interest calculated for all accounts in the system.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}