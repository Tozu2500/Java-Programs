package com.tozu.misc;

import java.util.ArrayList;
import java.util.List;

public class FlattenList {
	
	@SuppressWarnings("unchecked")
	public static List<Integer> flatten(List<Object> nested) {
		List<Integer> result = new ArrayList<>();
		
		for (Object element : nested) {
			if (element instanceof Integer) {
				result.add((Integer)element);
			} else if (element instanceof List) {
				result.addAll(flatten((List<Object>) element));
			}
		}
		
		return result;
	}

	public static void main(String[] args) {

		List<Object> nested = new ArrayList<>();
		nested.add(1);
		nested.add(List.of(2, 3, List.of(4, 5)));
		nested.add(6);
		nested.add(List.of(7, List.of(8, List.of(9))));
		
		System.out.println(nested);
		System.out.println("\n\nAfter flattening: " + flatten(nested));
	}

}
