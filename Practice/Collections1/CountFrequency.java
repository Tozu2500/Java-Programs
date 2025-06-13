package com.tozu.practice.collections.two;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountFrequency {

	public static void main(String[] args) {
		
		List<String> items = Arrays.asList("apple", "banana", "apple", "orange", "mango", "mango");
		Map<String, Integer> fruitFrequency = new HashMap<>();
		
		for (String item : items) {
			fruitFrequency.put(item, fruitFrequency.getOrDefault(item, 0) + 1);
		}
		
		System.out.println(fruitFrequency);

	}

}
