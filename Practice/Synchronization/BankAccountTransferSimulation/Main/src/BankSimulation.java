// File: BankSimulation.java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankSimulation {
    public static void main(String[] args) {
        BankAccount acc1 = new BankAccount(1, 1000);
        BankAccount acc2 = new BankAccount(2, 1000);
        BankAccount acc3 = new BankAccount(3, 1000);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Simulate random transfers
        for (int i = 0; i < 10; i++) {
            System.out.println("**********************************");
            executor.execute(new TransferTask(acc1, acc2, 100));
            executor.execute(new TransferTask(acc2, acc3, 50));
            executor.execute(new TransferTask(acc3, acc1, 70));
        }

        executor.shutdown();
    }
}
