package com.tozu.datastructures;

/*
 * Single nodes in a singly linked list.
 * They hold a value and reference to the next node.
 * */
public class Node<T> {
	
	T value;
	Node<T> next;
	
	public Node(T value) {
		this.value = value;
		this.next = null;
	}
	
	public T getValue() {
		return value;
	}
	
	public Node<T> getNext() {
		return next;
	}

}
