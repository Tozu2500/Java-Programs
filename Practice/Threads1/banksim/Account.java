package java_concurrency.multithreading.banksim;

public class Account {

	private final int id;
	private double balance;
	
	public Account(int id, double initialBalance) {
		this.id = id;
		this.balance = initialBalance;
	}
	
	public synchronized void deposit(double amount) {
		balance += amount;
	}
	
	public synchronized boolean withdraw(double amount) {
		if (balance >= amount) {
			balance -= amount;
			return true;
		}
		return false;
	}
	
	public synchronized double getBalance() {
		return balance;
	}
	
	public int getId() {
		return id;
	}
}
