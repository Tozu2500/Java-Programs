package com.tozu.testing;

import java.util.Scanner;

public class BinaryToHexExplainer {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter a binary number: ");
		String binary = scanner.nextLine().trim();
		
		// Step 1: Pad to multiples of 4
		int padding = (4 - (binary.length() % 4)) % 4;
		String paddedBinary = "0".repeat(padding) + binary;
		
		System.out.println("\nStep 1: Pad to groups of 4 bits");
		System.out.println("Original: " + binary);
		System.out.println("Padded:   " + paddedBinary);
		
		// Step 2: Split into groups of 4
		System.out.println("\nStep 2: Split into 4-bit groups");
		for (int i = 0; i < paddedBinary.length(); i += 4) {
			System.out.println(paddedBinary.substring(i, i + 4));
		}
		
		// Step 3: Convert each group to hex
		System.out.println("\nStep 3: Convert each group to hex");
		StringBuilder hex = new StringBuilder();
		
		for (int i = 0; i < paddedBinary.length(); i += 4) {
			String group = paddedBinary.substring(i, i + 4);
			int decimal = Integer.parseInt(group, 2);
			String hexDigit = Integer.toHexString(decimal).toUpperCase();
			
			System.out.println("\n" + group + " -> " + decimal + " -> " + hexDigit);
			hex.append(hexDigit);
		}
		
		System.out.println("\nFinal Hex: " + hex.toString());
		
		// Show as an integer
		int num = Integer.parseInt(hex.toString(), 16);
		
		System.out.println("\nIn integer: " + num);
		
		scanner.close();
	}

}
