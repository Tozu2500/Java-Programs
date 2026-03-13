package com.tozu.arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CharacterFrequency {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter a piece of text: ");
		String input = scanner.nextLine();

		Map<Character, Integer> frequency = new HashMap<>();
		
		for (char c : input.toCharArray()) {
			frequency.put(c, frequency.getOrDefault(c, 0) + 1);
		}
		
		System.out.println("\nCharacter frequencies:");
		for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
			System.out.println("'" + entry.getKey() + "' : " + entry.getValue());
		}
		
		scanner.close();
	}

}
