package com.tozu.sorting;

import java.util.Random;

public class MergeSort {
	
	public static void mergeSort(int[] arr) {
		// In this case, the array only contains one element.
		// That means, that it is already sorted.
		if (arr.length <= 1) {
			return;
		}
		
		int mid = arr.length / 2;
		
		int[] left = new int[mid];
		int[] right = new int[arr.length - mid];
		
		// Data copying
		System.arraycopy(arr, 0, left, 0, mid);
		System.arraycopy(arr, mid, right, 0, arr.length - mid);
		
		// Recursive sorting
		mergeSort(left);
		mergeSort(right);
		
		// Merge
		merge(arr, left, right);
	}
	
	private static void merge(int[] arr, int[] left, int[] right) {
		int i = 0, j = 0, k = 0;
		
		while (i < left.length && j < right.length) {
			if (left[i] <= right[j]) {
				arr[k++] = left[i++];
			} else {
				arr[k++] = right[j++];
			}
		}
		
		// Remaining elements
		while (i < left.length) {
			arr[k++] = left[i++];
		}
		
		while (j < right.length) {
			arr[k++] = right[j++];
		}
	}

	public static void main(String[] args) {

		Random random = new Random();
		
		// Declare an array with X amount of elements
		int[] arr = new int[100000000];
		
		// Add random elements to the array
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(1000);
		}
		
		mergeSort(arr);
		
		for (int num : arr) {
			System.out.print(num + " ");
		}
	}

}
