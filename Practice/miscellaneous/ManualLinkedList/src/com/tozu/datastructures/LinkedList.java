package com.tozu.datastructures;

/*
 * A manual singly linked list implementation.
 * Insertion, deletion, search, iteration and size tracking.
 * 
 * */
public class LinkedList<T> {
	
	private Node<T> head;
	private int size;
	
	public LinkedList() {
		this.head = null;
		this.size = 0;
	}
	
	// Insert a new element to the front of the list
	public void addFirst(T value) {
		Node<T> newNode = new Node<>(value);
		newNode.next = head;
		head = newNode;
		size++;
	}
	
	// Insert a new element to the end of the list
	public void addLast(T value) {
		Node<T> newNode = new Node<>(value);
		
		if (head == null) {
			head = newNode;
		} else {
			Node<T> current = head;
			while (current.next != null) {
				current = current.next;
			}
			
			current.next = newNode;
		}
		
		size++;
	}

	// Removes the first of the given value
	// Returns true if successful
	public boolean remove(T value) {
		if (head == null) return false;
		
		// Special case: removing head
		if (head.value.equals(value)) {
			head = head.next;
			size--;
			return true;
		}
		
		Node<T> current = head;
		while (current.next != null) {
			if (current.next.value.equals(value)) {
				current.next = current.next.next;
				size--;
				return true;
			}
			current = current.next;
		}
		
		return false;
	}
	
	// Returns true if the list contains the given value
	public boolean contains(T value) {
		Node<T> current = head;
		while (current != null) {
			if (current.value.equals(value)) return true;
			current = current.next;
		}
		
		return false;
	}
	
	// Return the element at the given index, throws IndexOutOfBoundsException
	public T get(int index) {
		checkIndex(index);
		
		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		
		return current.value;
	}
	
	// Clear entire list
	public void clear() {
		head = null;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public LinkedListIterator<T> iterator() {
		return new LinkedListIterator<>(head);
	}
	
	private void checkIndex(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
	}
}


