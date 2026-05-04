package com.tozu.arrays;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FrequencyCount {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Java arrays and freq. counting demo app");
		
		// Integer frequency demo
		int[] numbers = {4, 2, 7, 4, 2, 4, 9, 7, 2, 2};
		System.out.println("1) Integer Array: " + Arrays.toString(numbers));
		countIntFrequency(numbers);
		
		// String frequency demo
		String[] words = {"apple", "banana", "apple", "kiwi", "banana", "apple"};
		System.out.println("\n2) String Array: " + Arrays.toString(words));
		countStringFrequency(words);
		
		// Character frequency demo
		String text = "hello everyone";
		System.out.println("\n3) Character Frequency in: \"" + text + "\"");
		countCharFrequency(text);
		
		// Sorted frequency demo
		System.out.println("\n4) Sorted Frequency Count: ");
		countSortedFrequency(numbers);
		
		// User input demo
		System.out.println("\n5) Enter a sentence to count word frequency:");
		String userSentence = scanner.nextLine();
		countWordFrequency(userSentence);
		
		scanner.close();
	}
	
	// Count Integer frequency using a HashMap
	public static void countIntFrequency(int[] arr) {
		Map<Integer, Integer> freq = new HashMap<>();
		
		for (int num : arr) {
			freq.put(num, freq.getOrDefault(num, 0) + 1);
		}
		
		System.out.println("Integer Frequency:");
		for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
			System.out.printf("%d -> %d times%n", entry.getKey(), entry.getValue());
		}
	}
	
	// Frequency count for strings
	public static void countStringFrequency(String[] arr) {
		Map<String, Integer> freq = new HashMap<>();
		
		for (String word : arr) {
			freq.put(word, freq.getOrDefault(word, 0) + 1);
		}
		
		System.out.println("String Frequency: ");
		for (Map.Entry<String, Integer> entry : freq.entrySet()) {
			System.out.printf("  \"%s\" -> %d times%n", entry.getKey(), entry.getValue());
		}
	}
	
	
	// Character frequency in a string
	public static void countCharFrequency(String text) {
		Map<Character, Integer> freq = new HashMap<>();
		
		for (char c : text.toCharArray()) {
			if (c == ' ') continue;
			freq.put(c, freq.getOrDefault(c, 0) + 1);
		}
		
		System.out.println("Character Frequency:");
		for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
			System.out.printf("  '%c' -> %d times%n", entry.getKey(), entry.getValue());
		}
	}
	
	
	// Frequency counting using sorting
	public static void countSortedFrequency(int[] arr) {
		int[] sorted = arr.clone();
		Arrays.sort(sorted);
		
		System.out.println("Sorted Array: " + Arrays.toString(sorted));
		
		int count = 1;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i] == sorted[i - 1]) {
				count++;
			} else {
				System.out.printf("  %d -> %d times%n", sorted[i - 1], count);
				count = 1;
			}
		}
		System.out.printf("  %d -> %d times%n", sorted[sorted.length - 1], count);
	}
	
	
	// Word frequency from user input
	public static void countWordFrequency(String sentence) {
		String[] words = sentence.toLowerCase().split("\\W+");
		Map<String, Integer> freq = new HashMap<>();
		
		for (String w : words) {
			if (w.isEmpty()) continue;
			freq.put(w, freq.getOrDefault(words,  0) + 1);
		}
		
		System.out.println("Word Frequency:");
		for (Map.Entry<String, Integer> entry : freq.entrySet()) {
			System.out.printf("  \"%s\" -> %d times%n", entry.getKey(), entry.getValue());
		}
	}

}

