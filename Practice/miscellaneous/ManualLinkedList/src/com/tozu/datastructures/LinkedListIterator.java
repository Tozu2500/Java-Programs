package com.tozu.datastructures;

import java.util.NoSuchElementException;

public class LinkedListIterator<T> {
	
	private Node<T> current;
	
	public LinkedListIterator(Node<T> start) {
		this.current = start;
	}
	
	public boolean hasNext() {
		return current != null;
	}
	
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more elements!");
		}
		T value = current.value;
		current = current.next;
		return value;
	}

}
