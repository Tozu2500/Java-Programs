public class TransferTask implements Runnable {

    private BankAccount from;
    private BankAccount to;
    private double amount;

    public TransferTask(BankAccount from, BankAccount to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        // For avoiding deadlocking, we will always lock accounts in a consistent order
        BankAccount firstLock = (from.getId() < to.getId()) ? from : to;
        BankAccount secondLock = (from.getId() < to.getId()) ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                    System.out.println("Transferred " + amount + " from account " + from.getId() + " to account " + to.getId());
                    System.out.println("******************************");
                }
            }
        }
    }
}