
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private DashboardPanel dashboardPanel;
    private ProfilePanel profilePanel;
    private AdminPanel adminPanel;

    public MainFrame() {
        setTitle("Bank Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initializePanels();

        add(mainPanel);
        showLogin();
    }

    private void initializePanels() {
        loginPanel = new LoginPanel(this);
        registrationPanel = new RegistrationPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registrationPanel, "registration");
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "login");
    }

    public void showRegistration() {
        cardLayout.show(mainPanel, "registration");
    }

    public void showDashboard() {
        if (dashboardPanel != null) {
            mainPanel.remove(dashboardPanel);
        }
        dashboardPanel = new DashboardPanel(this);
        mainPanel.add(dashboardPanel, "dashboard");
        cardLayout.show(mainPanel, "dashboard");
    }

    public void showProfile() {
        if (profilePanel != null) {
            mainPanel.remove(profilePanel);
        }
        profilePanel = new ProfilePanel(this);
        mainPanel.add(profilePanel, "profile");
        cardLayout.show(mainPanel, "profile");
    }

    public void showAdminPanel() {
        User currentUser = BankSystem.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Access denied. Admin privileges required.",
                "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (adminPanel != null) {
            mainPanel.remove(adminPanel);
        }
        adminPanel = new AdminPanel(this);
        mainPanel.add(adminPanel, "admin");
        cardLayout.show(mainPanel, "admin");
    }

    public void showTransactionHistory(BankAccount account) {
        TransactionHistoryPanel historyPanel = new TransactionHistoryPanel(this, account);
        mainPanel.add(historyPanel, "history");
        cardLayout.show(mainPanel, "history");
    }
}
