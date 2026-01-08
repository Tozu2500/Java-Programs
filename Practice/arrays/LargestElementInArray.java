package com.tozu.arrays;

import java.util.Random;

public class LargestElementInArray {

	public static void main(String[] args) {
		
		Random random = new Random();
		
		int[] arr = new int[15];
		int largestNum = 0;
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(100);
		}

		for (int num : arr) {
			System.out.print(num + ", ");
		}
		
		largestNum = findLargestElement(arr, largestNum);
		
		System.out.println("\nThe largest num: " + largestNum);
	}

	public static int findLargestElement(int[] arr, int largestNum) {
		
		for (int i = 0; i < arr.length; i++) {
			if (largestNum < arr[i]) {
				largestNum = arr[i];
			} else {
				continue;
			}
		}
		
		return largestNum;
	}
}
