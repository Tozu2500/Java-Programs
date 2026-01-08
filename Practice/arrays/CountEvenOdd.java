package com.tozu.arrays;

import java.util.Arrays;
import java.util.Random;

public class CountEvenOdd {

	public static void main(String[] args) {
		
		Random random = new Random();
		
		int[] arr = new int[10];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(101);
		}

		System.out.println(Arrays.toString(arr));
		
		int even = 0;
		int odd = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] % 2 == 0) {
				even++;
			} else {
				odd++;
			}
		}
		
		System.out.println("Even nums: " + even + "    Odd nums: " + odd);
	}

}
