package com.tozu.practice.concurrency.one;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManualThreadEx {

	public static void main(String[] args) {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		executor.execute(() -> System.out.println("Running in a thread pool..."));
		executor.shutdown();

	}

}
