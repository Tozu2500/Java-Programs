package com.tozu.arrays;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDuplicates {
	
	public static void main(String[] args) {
		String[] arr = {"apple", "banana", "apple", "orange", "banana"};
		
		Set<String> set = new LinkedHashSet<>(Arrays.asList(arr));
		String[] result = set.toArray(new String[0]);
		
		System.out.println(Arrays.toString(result));
	}
	
	
}
