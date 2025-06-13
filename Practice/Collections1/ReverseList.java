package com.tozu.practice.collections.two;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReverseList {

	public static void main(String[] args) {
		
		List<Integer> list = Arrays.asList(10, 20, 30, 40, 50);
		Collections.reverse(list);
		System.out.println(list);

	}

}
