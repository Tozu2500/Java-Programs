import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankSystem {
    private static BankSystem instance;
    private List<User> users;
    private User currentUser;
    private static final String DATA_FILE = "bank_data.dat";
    
    private BankSystem() {
        users = new ArrayList<>();
        loadData();
        if (users.isEmpty()) {
            createDefaultAdmin();
        }
    }
    
    public static BankSystem getInstance() {
        if (instance == null) {
            instance = new BankSystem();
        }
        return instance;
    }
    
    private void createDefaultAdmin() {
        User admin = new User("admin", "admin123", "System Administrator", 
            "admin@bank.com", "000-000-0000");
        admin.setAdmin(true);
        users.add(admin);
        saveData();
    }
    
    public boolean registerUser(String username, String password, String fullName, 
                                String email, String phoneNumber) {
        if (getUserByUsername(username) != null) {
            return false;
        }
        User newUser = new User(username, password, fullName, email, phoneNumber);
        users.add(newUser);
        saveData();
        return true;
    }
    
    public boolean login(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;
        boolean exists;
        
        do {
            accountNumber = String.format("%010d", random.nextInt(1000000000));
            exists = false;
            
            for (User user : users) {
                for (BankAccount account : user.getAccounts()) {
                    if (account.getAccountNumber().equals(accountNumber)) {
                        exists = true;
                        break;
                    }
                }
                if (exists) break;
            }
        } while (exists);
        
        return accountNumber;
    }
    
    public BankAccount createAccount(User user, String accountType, double initialBalance) {
        String accountNumber = generateAccountNumber();
        BankAccount account = null;
        
        switch (accountType) {
            case "Savings":
                account = new SavingsAccount(accountNumber, user.getFullName(), initialBalance);
                break;
            case "Checking":
                account = new CheckingAccount(accountNumber, user.getFullName(), initialBalance);
                break;
            case "Business":
                account = new BusinessAccount(accountNumber, user.getFullName(), initialBalance);
                break;
        }
        
        if (account != null) {
            user.addAccount(account);
            saveData();
        }
        
        return account;
    }
    
    public boolean deleteAccount(User user, BankAccount account) {
        if (account.getBalance() > 0) {
            return false;
        }
        user.removeAccount(account);
        saveData();
        return true;
    }
    
    public BankAccount findAccountByNumber(String accountNumber) {
        for (User user : users) {
            for (BankAccount account : user.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }
    
    public void calculateAllInterests() {
        for (User user : users) {
            for (BankAccount account : user.getAccounts()) {
                account.calculateInterest();
            }
        }
        saveData();
    }
    
    public boolean deleteUser(String username) {
        User user = getUserByUsername(username);
        if (user == null || user.isAdmin()) {
            return false;
        }
        
        for (BankAccount account : user.getAccounts()) {
            if (account.getBalance() > 0) {
                return false;
            }
        }
        
        users.remove(user);
        saveData();
        return true;
    }
    
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}