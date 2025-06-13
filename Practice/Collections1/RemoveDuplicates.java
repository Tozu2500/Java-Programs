package com.tozu.practice.collections.two;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveDuplicates {

	public static void main(String[] args) {
		
		// A list can contain duplicate elements.
		List<Integer> numbers = Arrays.asList(1, 2, 2, 2, 3, 3, 4, 4, 4, 4, 5, 6, 6);
		
		// Set contains no duplicate elements.
		Set<Integer> onlyUnique = new HashSet<>(numbers);
		
		System.out.println(onlyUnique);

	}

}
