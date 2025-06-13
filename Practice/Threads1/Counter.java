package com.tozu.practice.concurrency.one;

public class Counter {
	
	private int count = 0;
	private final Object lock = new Object();
	
	public void increment() {
		synchronized(lock) {
			count++;
		}
	}
	
	public int getCount() {
		synchronized(lock) {
			return count;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		
		Counter counter = new Counter();
		
		Thread thread1 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				counter.increment();
			}
		});
		
		Thread thread2 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				counter.increment();
			}
		});
		
		thread1.start();
		thread2.start();
		
		thread1.join();
		thread2.join();
		
		System.out.println("Final count: " + counter.getCount());

	}

}
