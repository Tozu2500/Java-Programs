package java_concurrency.multithreading;

public class DataPractice {
	
	private static String packet;
	private static boolean transfer = true;
	
	public synchronized static String receive() {
		while (transfer) {
			try {
				DataPractice.class.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
		transfer = true;
		System.out.println(DataPractice.class.accessFlags());
		DataPractice.class.accessFlags();
		return packet;
	}
	
	public synchronized static void send(String data) {
		while (!transfer) {
			try {
				DataPractice.class.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
		transfer = false;
		packet = data;
		DataPractice.class.notifyAll();
	}
	
	public static void main(String[] args) {
		Thread sender = new Thread(() -> {
			String[] messages = {
					"Message one", "Message two", "Message three", "Done"
			};
			
			for (String msg : messages) {
				send(msg);
				System.out.println("Sent: " + msg);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		
		Thread receiver = new Thread(() -> {
			for (String msg = receive(); !msg.equals("Done"); msg = receive()) {
				System.out.println("Received: " + msg);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		
		sender.start();
		receiver.start();
	}
	
}