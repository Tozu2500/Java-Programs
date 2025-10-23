import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private double balance;
    private String accountHolderName;
    private LocalDateTime creationDate;
    private List<Transaction> transactionHistory;
    private boolean isActive;
    
    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.creationDate = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
        this.isActive = true;
        
        if (initialBalance > 0) {
            addTransaction(new Transaction(TransactionType.DEPOSIT, initialBalance, "Initial deposit"));
        }
    }
    
    public abstract String getAccountType();
    public abstract double getInterestRate();
    public abstract double getMinimumBalance();
    
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        addTransaction(new Transaction(TransactionType.DEPOSIT, amount, "Deposit"));
        return true;
    }
    
    public boolean withdraw(double amount) {
        if (amount <= 0 || !isActive) {
            return false;
        }
        if (balance - amount < getMinimumBalance()) {
            return false;
        }
        balance -= amount;
        addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal"));
        return true;
    }
    
    public boolean transfer(BankAccount targetAccount, double amount) {
        if (amount <= 0 || !isActive || !targetAccount.isActive()) {
            return false;
        }
        if (balance - amount < getMinimumBalance()) {
            return false;
        }
        balance -= amount;
        targetAccount.deposit(amount);
        addTransaction(new Transaction(TransactionType.TRANSFER, amount, 
            "Transfer to " + targetAccount.getAccountNumber()));
        return true;
    }
    
    public void calculateInterest() {
        double interest = balance * (getInterestRate() / 100);
        if (interest > 0) {
            balance += interest;
            addTransaction(new Transaction(TransactionType.INTEREST, interest, 
                "Interest credited at " + getInterestRate() + "%"));
        }
    }
    
    private void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getAccountHolderName() {
        return accountHolderName;
    }
    
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public void setAccountHolderName(String name) {
        this.accountHolderName = name;
    }
}