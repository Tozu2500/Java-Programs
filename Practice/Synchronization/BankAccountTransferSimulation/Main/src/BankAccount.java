public class BankAccount {

    private int id;
    private double balance;

    public BankAccount(int id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
        System.out.println("Account " + id + " deposited: " + amount + ", New balance: " + balance);
    }

    public synchronized boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Account " + id + " withdrew: " + amount + ", New balance: " + balance);
            return true;
        } else {
            System.out.println("Account " + id + " insufficient funds for withdrawal: " + amount);
            return false;
        }
    }
}
