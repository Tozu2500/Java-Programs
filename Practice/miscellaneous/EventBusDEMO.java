package com.tozu.misc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EventBusDEMO {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void register(EventListener listener) {
        listeners.add(listener);
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    public void publish(String event) {
        // Safe iteration even if listeners are added/removed during publish
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        EventBusDEMO bus = new EventBusDEMO();

        AtomicInteger counter = new AtomicInteger(0);
        
        // Writer thread: occasionally add listeners
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
            	
            	int id = counter.getAndIncrement();
            	
                bus.register(e -> System.out.println("Listener " + id + " received: " + e));
                sleep(500);
            }
        });

        // Reader thread: constantly publish events
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                bus.publish("Event #" + i);
                sleep(200);
            }
        });

        writer.start();
        reader.start();

        writer.join();
        reader.join();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    interface EventListener {
        void onEvent(String event);
    }
}