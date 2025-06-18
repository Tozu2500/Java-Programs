package practice.lrucache;

import java.util.HashMap;

public class LRUCache {
	
	private class Node {
		int key;
		int value;
		Node prev;
		Node next;
		
		Node(int key, int value) {
			this.key = key;
			this.value = value;
		}
	}
	
	private final int capacity;
	private final HashMap<Integer, Node> map;
	private final Node head;
	private final Node tail;
	
	public LRUCache(int capacity) {
		this.capacity = capacity;
		this.map = new HashMap<>();
		
		// Dummy head, avoiding null checks
		head = new Node(0, 0);
		tail = new Node(0, 0);
		head.next = tail;
		tail.prev = head;
	}
	

	public int get(int key) {
		if (!map.containsKey(key)) return -1;
		
		Node node = map.get(key);
		remove(node);
		insertToFront(node);
		return node.value;
	}
	
	public void put(int key, int value) {
		if (map.containsKey(key)) {
			remove(map.get(key));
		}
		
		if (map.size() == capacity) {
			// Remove least recent node
			Node lru = tail.prev;
			remove(lru);
		}
		
		Node newNode = new Node(key, value);
		insertToFront(newNode);
	}

	
	// Remove a node from the list and the map
	private void remove(Node node) {
		map.remove(node.key);
		node.prev.next = node.next;
		node.next.prev = node.prev;
	}
	
	// Insert node right after head (the most recent one)
	private void insertToFront(Node node) {
		map.put(node.key, node);
		node.next = head.next;
		node.prev = head;
		head.next.prev = node;
		head.next = node;
	}
	
	// Testing
	public void printCache() {
		Node curr = head.next;
		System.out.print("Cache: ");
		while (curr != tail) {
			System.out.print("(" + curr.key + ":" + curr.value + ") ");
			curr = curr.next;
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		LRUCache cache = new LRUCache(3);
		
		cache.put(1, 10);
		cache.put(2, 20);
		cache.put(3, 30);
		cache.printCache();
		
		cache.get(2);
		cache.printCache();
		
		cache.put(4, 40);
		cache.printCache();
		
		System.out.println("Get 1: " + cache.get(1));
		System.out.println("Get 3: " + cache.get(3));
		cache.printCache();
	}

}
