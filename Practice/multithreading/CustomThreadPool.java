package com.tozu.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool {
	
	private final BlockingQueue<Runnable> taskQueue;
	private final Thread[] workers;
	private volatile boolean isShutdown = false;

	@SuppressWarnings("unused")
	public CustomThreadPool(int numThreads) {
		taskQueue = new LinkedBlockingQueue<>();
		workers = new Thread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			workers[i] = new Thread(() -> {
				while (!isShutdown || !taskQueue.isEmpty()) {
					try {
						Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});
			workers[i].start();
		}
	}
	
	public void submit(Runnable task) {
		if (!isShutdown) {
			taskQueue.offer(task);
		}
	}
	
	public void shutdown() throws InterruptedException {
		isShutdown = true;
		for (Thread worker : workers) {
			worker.join();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		CustomThreadPool pool = new CustomThreadPool(3);
	
		for (int i = 0; i <= 6; i++) {
			final int taskId = i;
			pool.submit(() -> {
				System.out.println("Task " + taskId + " running on " + Thread.currentThread().getName());
			});
		}
		
		pool.shutdown();
		System.out.println("All tasks completed!!!");
	}
}

