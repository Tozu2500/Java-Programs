package com.tozu.arrays;

import java.util.Arrays;

public class CopyArray {

	public static void main(String[] args) {
		
		int[] original = {1, 2, 3, 4, 5};
		
		int[] copy1 = new int[original.length];
		
		// Manual copy with a loop
		for (int i = 0; i < original.length; i++) {
			copy1[i] = original[i];
		}
		
		// Using Arrays.copyOf
		int[] copy2 = Arrays.copyOf(original, original.length);
		
		// Using System.arraycopy
		int[] copy3 = new int[original.length];
		System.arraycopy(original, 0, copy3, 0, original.length);
		
		System.out.println("Original array: " + original);
		System.out.println("Copy 1 (looped): " + copy1);
		System.out.println("Copy 2 (Arrays.copyOf): " + copy2);
		System.out.println("Copy 3 (System.arraycopy): ");
	}

}
