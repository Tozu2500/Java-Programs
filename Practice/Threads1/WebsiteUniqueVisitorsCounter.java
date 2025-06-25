package java_concurrency.multithreading;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class WebsiteUniqueVisitorsCounter {
	
	private static final Set<String> uniqueVisitors = new HashSet<>();
	
	public static void main(String[] args) throws InterruptedException {
		Runnable visit = () -> {
			for (int i = 0; i < 200; i++) {
				String user = "User" + (i % 20); // 20 unique users, prints all the way to 200
				synchronized (uniqueVisitors) {
					uniqueVisitors.add(user);
				}
				System.out.println("Visit #" + (i + 1) + " by " + user);
				
				try {
					// Random sleeps between 300ms and 1500ms
					Thread.sleep(ThreadLocalRandom.current().nextInt(300, 1501));
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.err.println("Thread was interrupted during sleep.");
				}
			}
			
			System.out.println("Simulation complete");
			System.out.println("Unique visitors: " + uniqueVisitors.size());
			System.out.println("Visitors: " + uniqueVisitors);
		};
		
		Thread simulationThread = new Thread(visit);
		simulationThread.start();
	}

}
