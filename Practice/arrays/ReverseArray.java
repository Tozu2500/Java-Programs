package com.tozu.arrays;

import java.util.Arrays;
import java.util.Random;

public class ReverseArray {

	public static void main(String[] args) {
		
		Random random = new Random();
		
		int[] arr = new int[15];
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(101);
		}
		
		System.out.println("Array: " + Arrays.toString(arr));
		
		// Reverse array
		for (int i = 0; i < arr.length / 2; i++) {
			int temp = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = temp;
		}

		System.out.println("Reversed array: " + Arrays.toString(arr));

	}

}
