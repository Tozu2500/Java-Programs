package java_concurrency.multithreading.banksim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
	
	private final Map<Integer, Account> accounts = new HashMap<>();
	
	private final ReentrantLock lock = new ReentrantLock();
	
	private final AtomicInteger totalTransactions = new AtomicInteger(0);
	
	private final String transactionFile = "transactions.txt";
	
	public Bank() {
		loadTransactionCount();
	}
	
	public void addAccount(Account account) {
		accounts.put(account.getId(), account);
	}
	
	public boolean transfer(int fromId, int toId, double amount) {
		Account from = accounts.get(fromId);
		Account to = accounts.get(toId);
		
		if (from == null || to == null || fromId == toId)
			return false;
		
		Account first = fromId < toId ? from : to;
		Account second = fromId < toId ? to : from;
		
		synchronized(first) {
			synchronized(second) {
				if (from.withdraw(amount)) {
					to.deposit(amount);
					int count = totalTransactions.incrementAndGet();
					saveTransactionCount(count);
					return true;
				}
				return false;
			}
		}
	}
	
	public void printBalances() {
		for (Account acc : accounts.values()) {
			System.out.printf("Account %d: %.2f%n", acc.getId(), acc.getBalance());
		}
	}

	public int getTotalTransactions() {
		return totalTransactions.get();
	}
	
	private void loadTransactionCount() {
		try (BufferedReader reader = new BufferedReader(new FileReader(transactionFile))) {
			String line = reader.readLine();
			if (line != null) {
				totalTransactions.set(Integer.parseInt(line.trim()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No previous transaction .txt stats file found. Starting a new one now!");
		}
	}
	
	private void saveTransactionCount(int count) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFile))) {
			writer.write(String.valueOf(count));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to save transactions to .txt file\n" + e.getMessage());
		}
	}
}
