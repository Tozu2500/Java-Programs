package com.tozu.strings;

import java.util.Scanner;

public class WordCount {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter text: ");
		
		String text = scanner.nextLine();
		
		int words = countWords(text);
		int characters = text.length();
		int charactersNoSpaces = text.replaceAll("\\s+", "").length();
		
		System.out.println("Results");
		System.out.println("Words: " + words);
		System.out.println("Characters: " + characters);
		System.out.println("Characters, no spaces: " + charactersNoSpaces);
	}

	private static int countWords(String text) {
		if (text == null || text.trim().isEmpty()) {
			return 0;
		}
		
		return text.trim().split("\\s+").length;
	}
}
