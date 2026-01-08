package com.tozu.arrays;

import java.util.Random;

public class ArrayDeclaration {

	public static void main(String[] args) {
		
		Random random = new Random();
		
		// Declare an array, that can hold 10 numbers
		int[] myArray = new int[10];

		System.out.println("Filling an array of 10 with random numbers");
		
		for (int i = 0; i < myArray.length; i++) {
			// Fill the array with numbers 0-10
			myArray[i] = random.nextInt(101);
		}
		
		for (int num : myArray) {
			System.out.print(num + ", ");
		}
		
	}

}
