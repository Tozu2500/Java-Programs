package com.tozu.arrays;

import java.util.Random;

public class SumOfArrayElements {

	public static void main(String[] args) {
		
		int[] array = new int[10];
		int sum = 0;
		Random random = new Random();
		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt(55);
		}
		System.out.println("The array has these numbers: ");
		for (int num : array) {
			System.out.print(num + ", ");
		}
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		
		System.out.println("\n\nThe sum: " + sum);
	}
}