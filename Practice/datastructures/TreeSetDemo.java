package com.tozu.datastructures;

import java.util.Comparator;
import java.util.TreeSet;

public class TreeSetDemo {

	record Student(String name, double gpa) {}
	
	public static void main(String[] args) {
		
		// Natural ordering (String implements Comparable)
		TreeSet<String> names = new TreeSet<>();
		names.add("Charlie");
		names.add("Alice");
		names.add("Bob");
		names.add("Diana");
		names.add("Alice");  // Duplicate ignored
		
		System.out.println("Sorted names: " + names);
		System.out.println("First: " + names.first());
		System.out.println("Last: " + names.last());
		
		// Navigation methods
		System.out.println("floor(Bob): " + names.floor("Bob"));
		System.out.println("lower(Bob): " + names.lower("Bob"));
		System.out.println("ceiling(Bob): " + names.ceiling("Bob"));
		System.out.println("higher(Bob): " + names.higher("Bob"));
		
		// Range views
		System.out.println("headSet(Charlie): " + names.headSet("Charlie"));
		System.out.println("tailSet(Charlie): " + names.tailSet("Bob"));
		System.out.println("subSet(Bob, Diana): " + names.subSet("Bob", "Diana"));
		System.out.println("subSet inclusive both: " + names.subSet("Bob", true, "Diana", true));
		
		// Descending view
		System.out.println("Descending: " + names.descendingSet());
		
		// Custom comparator, sort students by GPA descending
		TreeSet<Student> students = new TreeSet<>(
				Comparator.comparingDouble(Student::gpa).reversed()
						.thenComparing(Student::name)
				);
		
		students.add(new Student("Alice", 3.9));
		students.add(new Student("Bob", 3.5));
		students.add(new Student("Carol", 3.9));
		students.add(new Student("Dave", 3.7));
		
		System.out.println("\nStudents by GPA (highest first):");
		students.forEach(s ->
					System.out.printf("%-10s %.1f%n", s.name(), s.gpa()));
		
		// use compareTo to determine duplicates, not equals
		// two students with identical GPA AND name ----> TreeSet sees them as the SAME
		boolean added = students.add(new Student("Alice", 3.9));
		System.out.println("\nAdding Alice 3.9 again: " + (added ? "Accepted" : "Rejected"));
	}

}

