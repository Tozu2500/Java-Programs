package com.tozu.misc;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {
	
	@SuppressWarnings("unused")
	private final int capacity;
	private final LinkedHashMap<K, V> map;
	
	@SuppressWarnings("serial")
	public LRUCache(int capacity) {
		this.capacity = capacity;
		this.map = new LinkedHashMap<>(capacity, 0.75f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > capacity;
			}
		};
	}
	
	public V get(K key) {
		return map.getOrDefault(key, null);
	}
	
	public void put(K key, V value) {
		map.put(key, value);
	}
	
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
	public static void main(String[] args) {
		
		LRUCache<Integer, String> cache = new LRUCache<>(3);
		
		cache.put(1, "A");
		cache.put(2, "B");
		cache.put(3, "C");
		cache.get(1);  // Accessing 1, moves to front
		cache.put(4, "D");  // Evicts 2, least recently used (LRU)
		
		System.out.println(cache.containsKey(2));
		System.out.println(cache.containsKey(1));
		
	}
}