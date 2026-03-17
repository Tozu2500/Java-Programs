package com.tozu.datastructures;

import java.util.HashSet;
import java.util.Set;

public class HashSetDemo {
	
	// Simple value object -- pay attention to equals/hashCode contract with Java
	// We could also use a Java Record!
	static class UserId {
		private final int id;
		
		UserId(int id) {
			this.id = id;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof UserId other)) return false;
			return this.id == other.id;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(id);  // Consistent with equals
		}
		
		@Override
		public String toString() {
			return "UserId(" + id + ")";
		}
	}

	public static void main(String[] args) {

		// Basic set
		Set<String> tags = new HashSet<>();
		tags.add("java");
		tags.add("collections");
		tags.add("java");  // Duplicate! - silently ignored!
		tags.add(null);  // HashSet(s) allow one null value!
		
		System.out.println("Tags: " + tags);  // undefined order
		System.out.println("Size: " + tags.size());  // 3, and not 4
		System.out.println("Contains 'java': " + tags.contains("java"));  // TRUE
		
		// Remove values
		tags.remove(null);
		System.out.println("After null removal: " + tags);
		
		// Set operations
		@SuppressWarnings("unused")
		Set<String> a = new HashSet<>(Set.of("a", "b", "c"));
		Set<String> b = new HashSet<>(Set.of("b", "c", "d"));
		
		Set<String> intersection = new HashSet<>();
		intersection.retainAll(b);
		System.out.println("Intersection: " + intersection);
		
		Set<String> union = new HashSet<>();
		union.addAll(b);
		System.out.println("Union: " + union);
		
		Set<String> difference = new HashSet<>();
		difference.removeAll(b);
		System.out.println("Difference (a - b): " + difference);
		
		// Custom objects (or records) --- equals and hashCode do matter
		Set<UserId> ids = new HashSet<>();
		ids.add(new UserId(1));
		ids.add(new UserId(2));
		ids.add(new UserId(1));  // This is a duplicate. equals() and hashCode will say "NO!"
		
		System.out.println("Unique user IDs: " + ids);
	}
}












