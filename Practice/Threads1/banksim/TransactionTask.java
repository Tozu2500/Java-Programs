package java_concurrency.multithreading.banksim;

public class TransactionTask implements Runnable {
	
	private final Bank bank;
	private final Transaction transaction;
	
	public TransactionTask(Bank bank, Transaction transaction) {
		this.bank = bank;
		this.transaction = transaction;
	}

	@Override
	public void run() {
		boolean success = bank.transfer(transaction.fromId, transaction.toId, transaction.amount);
		String status = success ? "SUCCESS" : "FAILED";
		System.out.printf("Transaction %s: %d -> %d : %.2f%n", status, transaction.fromId, transaction.toId, transaction.amount);
	}

}
