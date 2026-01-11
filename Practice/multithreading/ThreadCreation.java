package com.tozu.multithreading;

public class ThreadCreation {
	
	static class MyThread extends Thread {
		private String threadName;
		
		public MyThread(String name) {
			this.threadName = name;
		}
		
		@Override
		public void run() {
			for (int i = 1; i <= 5; i++) {
				System.out.println(threadName + " - Count: " + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println(threadName + " interrupted.");
				}
			}
		}
	}
	
	public static void main(String[] args) {
		MyThread t1 = new MyThread("Thread-1");
		MyThread t2 = new MyThread("Thread-2");
		
		t1.start();
		t2.start();
	}
}