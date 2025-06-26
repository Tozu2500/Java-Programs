import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class CollectionsAndConcurrency {
    public static void main(String[] args) throws InterruptedException {
        // 1. Using ConcurrentHashMap
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("John", 1);
        map.put("Tozu", 2);
        
        Runnable producer = () -> {
            for (int i = 0; i < 5; i++) {
                map.put(Thread.currentThread().getName() + "-" + i, i);
            }
        };

        Thread t1 = new Thread(producer, "Thread One!!");
        Thread t2 = new Thread(producer, "Thread Two!!");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("ConcurrentHashMap contents: " + map);

        // 2.Using LinkedBlockingQueue for Producer-Consumer
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();

        Runnable consumer = () -> {
            try {
                for (int i = 0; i < 5; i++) {
                    String item = "Item " + 1;
                    queue.put(item);
                    System.out.println("Produced: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        producerThread.start();
        consumerThread.start();
        producerThread.join();
        consumerThread.join();
    }
}