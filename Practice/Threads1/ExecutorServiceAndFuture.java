package com.tozu.practice.concurrency.one;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceAndFuture {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		Future<Integer> future = executor.submit(() -> {
			Thread.sleep(1000);
			return 42;
		});
		
		System.out.println("Doing other tasks...");
		Integer result = future.get();
		System.out.println("Result: " + result);
		
		executor.shutdown();
		
	}

}
