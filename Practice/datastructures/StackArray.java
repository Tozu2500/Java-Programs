package com.tozu.datastructures;

public class StackArray<T> {
	
	private T[] data;
	private int top;
	
	@SuppressWarnings("unchecked")
	public StackArray(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be positive");
		}
		data = (T[]) new Object[capacity];
		top = -1;
	}
	
	public boolean isEmpty() {
		return top == -1;
	}
	
	public boolean isFull() {
		return top == data.length - 1;
	}
	
	public int size() {
		return top + 1;
	}
	
	public void push(T value) {
		if (isFull()) {
			throw new IllegalStateException("Stack is full");
		}
		data[++top] = value;
	}
	
	public T pop() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty");
		}
		T value = data[top];
		data[top] = null;
		top--;
		return value;
	}
	
	public T peek() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty");
		}
		return data[top];
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("StackArray[");
		for (int i = 0; i <= top; i++) {
			sb.append(data[i]);
			if (i < top) sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	public static void main(String[] args) {
		StackArray<Integer> stack = new StackArray<>(5);
		
		stack.push(10);
		stack.push(20);
		stack.push(30);
		
		System.out.println(stack);
		System.out.println("Top element: " + stack.peek());
		
		stack.pop();
		System.out.println("After pop(): " + stack);
	}
}
