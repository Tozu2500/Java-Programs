package com.tozu.multithreading;

public class RunnableInterface {
	
	static class MyRunnable implements Runnable {
        private String taskName;
        
        public MyRunnable(String name) {
            this.taskName = name;
        }
        
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println(taskName + " executing - Step: " + i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(new MyRunnable("Task-A"));
        Thread t2 = new Thread(new MyRunnable("Task-B"));
        
        // Using lambda (Java 8+)
        Thread t3 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("Lambda Task - Step: " + i);
            }
        });
        
        t1.start();
        t2.start();
        t3.start();
    }
}