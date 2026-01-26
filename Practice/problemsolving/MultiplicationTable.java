package com.tozu.problemsolving;

import java.util.Scanner;

public class MultiplicationTable {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.printf("Enter a number for the multiplication table: ");
		int n = scanner.nextInt();
		
		System.out.println("You selected " + n + "\n\n");
		
		for (int i = 1; i < 11; i++) {
			int total = n * i;
			System.out.println(n + " * " + i + " = " + total);
		}
		
		scanner.close();
	}

}
