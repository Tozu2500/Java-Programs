package com.tozu.arrays;

import java.util.Arrays;
import java.util.Random;

public class FindSmallestElementInArray {

	public static void main(String[] args) {
		
		int[] arr = new int[10];
		
		Random r = new Random();
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = r.nextInt(101);
		}
		
		System.out.println(Arrays.toString(arr));

		int smallest = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < smallest) {
				smallest = arr[i];
			}
		}
		
		System.out.println("Array smallest: " + smallest);
	}

}
