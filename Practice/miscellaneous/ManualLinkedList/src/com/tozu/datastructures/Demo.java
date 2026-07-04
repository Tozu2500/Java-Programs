package com.tozu.datastructures;

public class Demo {

	public static void main(String[] args) {

		LinkedList<Integer> list = new LinkedList<>();
		
		list.addFirst(10);
		list.addFirst(5);
		list.addLast(20);
		
		System.out.println("Size: " + list.size());
		System.out.println("Contains 10: " + list.contains(10));
		System.out.println("Element at index 1: " + list.get(1));
		
		System.out.println("Iterating list:");
		LinkedListIterator<Integer> it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		
		list.remove(10);
		System.out.println("After removing 10, size: " + list.size());
	}

}
