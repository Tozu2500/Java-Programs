package com.demo;

public class Run {
	public static void main(String[] args) {
		Thread serverThread = new Thread(() -> {
			MessagingServer server = new MessagingServer();
			server.start();
		});
		serverThread.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		MessagingClient.main(new String[0]);
	}
}

