package com.tozu.sorting;

public class SelectionSort {
	
	public static void selectionSort(int[] arr) {
		int n = arr.length;
		
		for (int i = 0; i < n - 1; i++) {
			int minIndex = i;
			
			for (int j = i + 1; j < n; j++) {
				if (arr[j] <= arr[minIndex]) {
					minIndex = j;
				}
			}
			
			int temp = arr[minIndex];
			arr[minIndex] = arr[i];
			arr[i] = temp;
		}
	}
	
	public static void printArray(int[] arr) {
		for (int value : arr) {
			System.out.print(value + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {

		int[] data = {33, 64, 25, 12, 22, 11};
		int[] data2 = {104, 55, 78, 224, 5, 19, 34, 7};
		
		System.out.println("Original array (1):");
		printArray(data);
		
		selectionSort(data);
		
		System.out.println("Sorted array (1):");
		printArray(data);

		System.out.println("Original array (2):");
		printArray(data2);
		
		selectionSort(data2);
		
		System.out.println("Sorted array (2):");
		printArray(data2);
	}

}
