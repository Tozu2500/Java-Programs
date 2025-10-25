
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TransactionHistoryPanel extends JPanel {
    private MainFrame mainFrame;
    private BankAccount account;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    
    public TransactionHistoryPanel(MainFrame mainFrame, BankAccount account) {
        this.mainFrame = mainFrame;
        this.account = account;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 248, 255));
        
        JPanel titlePanel = new JPanel(new GridLayout(3, 1, 0, 5));
        titlePanel.setBackground(new Color(240, 248, 255));
        
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        titlePanel.add(titleLabel);
        
        JLabel accountLabel = new JLabel("Account: " + account.getAccountNumber() + 
            " - " + account.getAccountType());
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        accountLabel.setForeground(new Color(70, 130, 180));
        titlePanel.add(accountLabel);
        
        JLabel balanceLabel = new JLabel(String.format("Current Balance: $%.2f", 
            account.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(34, 139, 34));
        titlePanel.add(balanceLabel);
        
        topPanel.add(titlePanel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> mainFrame.showDashboard());
        buttonPanel.add(backButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] columnNames = {"Date & Time", "Type", "Amount", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 13));
        transactionTable.setRowHeight(30);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        transactionTable.getTableHeader().setBackground(new Color(176, 196, 222));
        
        loadTransactions();
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void loadTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = account.getTransactionHistory();
        
        if (transactions.isEmpty()) {
            Object[] row = {"No transactions", "", "", "This account has no transaction history yet."};
            tableModel.addRow(row);
        } else {
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction transaction = transactions.get(i);
                Object[] row = {
                    transaction.getFormattedTimestamp(),
                    transaction.getType().toString(),
                    String.format("$%.2f", transaction.getAmount()),
                    transaction.getDescription()
                };
                tableModel.addRow(row);
            }
        }
    }
}