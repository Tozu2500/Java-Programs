package practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class MultithreadedFileProcessor {
	
	// Task for counting words in a single file.
	static class WordCountTask implements Callable<Integer> {
		private final Path filePath;

		public WordCountTask(Path filePath) {
			this.filePath = filePath;
		}
		
		@Override
		public Integer call() throws IOException {
			try (Stream<String> lines = Files.lines(filePath)) {
				return (int) lines
						.flatMap(line -> Arrays.stream(line.trim().split("\\s+")))
						.filter(word -> !word.isEmpty())
						.count();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error reading file: " + filePath);
				return 0;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		// List of file paths to process concurrently
		List<Path> files = List.of(
				Paths.get("D:\\Java Eclipse\\Java Practice\\src\\practice\\sample1.txt"),
				Paths.get("D:\\Java Eclipse\\Java Practice\\src\\practice\\sample2.txt"),
				Paths.get("D:\\Java Eclipse\\Java Practice\\src\\practice\\sample3.txt")
			);
		
		ExecutorService executor = Executors.newFixedThreadPool(files.size());
		List<Future<Integer>> futures = new ArrayList<>();
		
		// Submit
		for (Path file : files) {
			futures.add(executor.submit(new WordCountTask(file)));
		}
		
		// Results
		int totalWords = 0;
		for (Future<Integer> future : futures) {
			totalWords += future.get();
		}
		
		executor.shutdown();
		System.out.println("Total word count across all files: " + totalWords);

	}

}
