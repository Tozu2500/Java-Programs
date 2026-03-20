package com.tozu.sorting;

public class BubbleSort {
	
	public static void bubbleSort(int[] arr) {
		int n = arr.length;
		boolean swapped;
		
		// Outer loop for passes
		for (int i = 0; i < n - 1; i++) {
			swapped = false;
			
			// Inner loop for comparisons
			for (int j = 0; j < n - i - 1; j++) {
				// Adjacent element comparison
				if (arr[j] > arr[j + 1]) {
					// Swap, if out of order
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
					swapped = true;
				}
			}
			
			// If no swaps happened the array was already sorted
			if (!swapped) break;
		}
	}
	
	public static void printArray(int[] arr) {
		for (int num : arr) {
			System.out.print(num + " ");
		}
	}

	public static void main(String[] args) {
		int[] arr = {64, 34, 25, 22, 12, 90};
		System.out.print("Original array: ");
		printArray(arr);
		System.out.println();
		
		bubbleSort(arr);
		
		System.out.print("Sorted array: ");
		printArray(arr);
	}

}
