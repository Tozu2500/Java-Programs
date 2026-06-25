package com.tozu.searching;

import java.util.Arrays;

public class RecursiveBinarySearch {

	public static void main(String[] args) {

		int[] numbers = {3, 9, 12, 15, 18, 21, 27, 33, 43};
		int target = 18;
		
		System.out.println("Array: " + Arrays.toString(numbers));
		System.out.println("Searching for: " + target);
		
		int index = recursiveBinarySearch(numbers, target, 0, numbers.length - 1);
		
	}
	
	private static void test(int[] arr, int target) {
		int result = recursiveBinarySearch(arr, target, 0, arr.length - 1);
		System.out.println("Search for: " + target + " --->>> " + result);
	}
	
	public static int recursiveBinarySearch(int[] arr, int target, int left, int right) {
		// Base case: interval invalid -> not found
		if (left > right) {
			return -1;
		}
		
		int mid = (right - left) / 2;
		
		// Debug
		System.out.println("Checking range [" + left + ", " + right + "], mid=" + mid);
		
		if (arr[mid] == target) {
			return mid;
		}
		
		if (target < arr[mid]) {
			return recursiveBinarySearch(arr, target, left, mid - 1);
		} else {
			return recursiveBinarySearch(arr, target, mid + 1, right);
		}
	}

}
