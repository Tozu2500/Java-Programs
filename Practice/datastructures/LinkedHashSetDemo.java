package com.tozu.datastructures;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedHashSetDemo {
	
	public static void main(String[] args) {
		
		// LinkedHashSet preserves order, but still rejects duplicates
		Set<String> recentSearches = new LinkedHashSet<>();
		
		recentSearches.add("java collections");
		recentSearches.add("hashet example");
		recentSearches.add("linked list vs arraylist");
		recentSearches.add("java collections");  // duplicate -> ignored, original position kept
		recentSearches.add("treemap vs hashmap");
		
		System.out.println("Search history (insertion order):");
		recentSearches.forEach(s -> System.out.println(" " + s));
		System.out.println("Size: " + recentSearches.size());  // 4, not 5
	
		// Contrast with HashSet - same elements, unpredictable order
		Set<String> unordered = new HashSet<>(recentSearches);
		System.out.println("\nSame data in HashSet (order is undefined though):");
		unordered.forEach(s -> System.out.println(" " + s));
		
		// First occurrence wins
		Set<String> pipeline = new LinkedHashSet<>();
		pipeline.add("step-one");
		pipeline.add("step-two");
		pipeline.add("step-one");  // Ignored, the first "step-one" is counted
		pipeline.add("step-three");
		
		System.out.println("\nPipeline steps (first occurrence wins):");
		pipeline.forEach(s -> System.out.println(" " + s));
		// Step one stays at index zero. Not moved to end.
		
		
		// Removal and re-adding does move it to the end of the Set
		pipeline.remove("step-one");
		pipeline.add("step-one");
		System.out.println("\nAfter re-inserting step-one:");
		pipeline.forEach(s -> System.out.println(" " + s));
		// After this, step one is at the end of the Set
	}

}




















