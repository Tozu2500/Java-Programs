package com.tozu.arrays;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class MergeTwoArrays {
	
	public static void main(String[] args) {
		
		int[] a = {1, 3, 5, 7};
		int[] b = {2, 4, 6, 8};
		
		// 1. Simple merge, concatenate
		int[] merged = mergeSimple(a, b);
		System.out.println("1) Simple Merge, concatenate");
		System.out.println(Arrays.toString(merged));
		System.out.println();
		
		// 2. Merge + sort
		int[] mergedSorted = mergeAndSort(a, b);
		System.out.println("2) Merge + Sort");
		System.out.println(Arrays.toString(mergedSorted));
		System.out.println();
		
		// 3. Merge + remove duplicates (order is preserved)
		int[] a2 = {1, 2, 2, 3};
		int[] b2 = {2, 3, 4, 4, 5};
		int[] mergedUnique = mergeUnique(a2, b2);
		System.out.println("3) Merge + Unique (Order is preserved)");
		System.out.println(Arrays.toString(mergedUnique));
		System.out.println();
		
		// 4. Manual merge of two arrays
		int[] sortedA = {1, 4, 6, 9};
		int[] sortedB = {2, 3, 7, 10};
		int[] mergedSortedManual = mergeSortedArrays(sortedA, sortedB);
		System.out.println("4) Manual merge of two sorted arrays");
		System.out.println(Arrays.toString(mergedSortedManual));
		System.out.println();
		
		// Stress test
		System.out.println("5) Performance test (100k elements)");
		performanceTest();
		
	}
	
	public static int[] mergeSimple(int[] a, int[] b) {
		int[] result = new int[a.length + b.length];
		
		// Copy of a
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i];
		}
		
		// Copy b
		for (int i = 0; i < b.length; i++) {
			result[a.length + i] = b[i];
		}
		
		return result;
	}
	
	public static int[] mergeAndSort(int[] a, int[] b) {
		int[] merged = mergeSimple(a, b);
		Arrays.sort(merged);
		
		return merged;
	}
	
	public static int[] mergeUnique(int[] a, int[] b) {
		Set<Integer> set = new LinkedHashSet<>();
		
		for (int x : a) {
			set.add(x);
		}
		
		for (int x : b) {
			set.add(x);
		}
		
		int[] result = new int[set.size()];
		int i = 0;
		for (int x : set) result[i++] = x;
		
		return result;
	}
	
	public static int[] mergeSortedArrays(int[] a, int[] b) {
		int[] result = new int[a.length + b.length];
		
		int i = 0;
		int j = 0;
		int k = 0;
		
		while (i < a.length && j < b.length) {
			if (a[i] <= b[j]) {
				result[k++] = a[i++];
			} else {
				result[k++] = b[j++];
			}
		}
		
		while (i < a.length) result[k++] = a[i++];
		while (j < b.length) result[k++] = b[j++];
		
		return result;
	}
	
	public static void performanceTest() {
		int size = 100_000;
		int[] a = new int[size];
		int[] b = new int[size];
		
		for (int i = 0; i < size; i++) {
			a[i] = i;
			b[i] = size - i;
		}
		
		long start = System.currentTimeMillis();
		int[] merged = mergeSimple(a, b);
		long end = System.currentTimeMillis();
		
		System.out.println("Simple merge took: " + (end - start) + " ms");
		
		start = System.currentTimeMillis();
		Arrays.sort(merged);
		end = System.currentTimeMillis();
		
		System.out.println("Sorting merged array took : " + (end - start) + " ms");
	}

}

