package java_concurrency.multithreading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class NewsAgency {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> newsQueue = new LinkedBlockingQueue<>(5);

        List<String> producedNews = Collections.synchronizedList(new ArrayList<>());
        List<String> consumedNews = Collections.synchronizedList(new ArrayList<>());
        Set<String> uniqueHeadlines = Collections.synchronizedSet(new HashSet<>());
        Map<String, Integer> readCount = new ConcurrentHashMap<>();

        Thread producer = new Thread(() -> {
            String[] headlines = {"Java 24 released",
                    "Sunny in Helsinki",
                    "Local team wins!",
                    "AI Beat my grandma in chess",
                    "What's your favorite food"
            };

            try {
                for (String news : headlines) {
                    newsQueue.put(news);
                    producedNews.add(news);
                    uniqueHeadlines.add(news);
                    System.out.println(Thread.currentThread().getName() + " published: " + news);
                    Thread.sleep(1000);
                }
                // Signal the consumer to stop
                newsQueue.put("End");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }, "News Producer");

        Thread consumer = new Thread(() -> {
            try {
                String news;
                while (true) {
                    news = newsQueue.take();
                    if (news.equals("End")) {
                        break;
                    }
                    consumedNews.add(news);
                    readCount.merge(news, 1, Integer::sum);
                    System.out.println(Thread.currentThread().getName() + " read: " + news);
                    Thread.sleep(1200);
                }
                System.out.println(Thread.currentThread().getName() + " receive and signal.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "News Reader");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("---- Summary ----");
        System.out.println("Produced News: " + producedNews);
        System.out.println("Consumed News: " + consumedNews);
        System.out.println("Unique Headlines: " + uniqueHeadlines);
        System.out.println("Read Count: " + readCount);
    }

}