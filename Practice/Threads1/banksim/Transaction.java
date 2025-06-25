package java_concurrency.multithreading.banksim;

public class Transaction {
	
	public final int fromId;
	public final int toId;
	public final double amount;
	
	public Transaction(int fromId, int toId, double amount) {
		this.fromId = fromId;
		this.toId = toId;
		this.amount = amount;
	}

}
