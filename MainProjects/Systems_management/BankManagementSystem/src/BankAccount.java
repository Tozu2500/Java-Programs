
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

    

}
