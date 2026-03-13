package com.tozu.arrays;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CharacterFrequency2 {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter a string: ");
		String input = scanner.nextLine();
		
		// Lowercase for case-insensitivity
		input = input.toLowerCase();
		
		// TreeMap to automatically sort keys alphabetically
		Map<Character, Integer> frequency = new TreeMap<>();
		
		for (char c : input.toCharArray()) {
			// No spaces
			if (c == ' ') {
				continue;
			}
			
			// Count only letters, ignore others
			if (!Character.isLetter(c)) {
				continue;
			}
			
			frequency.put(c, frequency.getOrDefault(c,  0) + 1);
		}
		
		if (frequency.isEmpty()) {
			System.out.println("No valid characters found (text)");
		}
		
		System.out.println("\nCharacter frequencies:");
		for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
			System.out.println("'" + entry.getKey() + "' : " + entry.getValue());
		}
		
		scanner.close();
	}

}
