package com.tozu.practice.collections.two;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortListAlphabetically {

	public static void main(String[] args) {
		
		List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
		Collections.sort(names);
		System.out.println(names);

	}

}
