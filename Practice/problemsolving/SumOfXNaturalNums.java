package com.tozu.problemsolving;

public class SumOfXNaturalNums {
	
	static int findSum(int n) {
		int sum = 0;
		
		for (int i = 1; i <= n; i++) {
			sum += i;
		}
		
		return sum;
	}
	
	public static void main(String[] args) {
		int n = 44;
		System.out.println(findSum(n));
	}

}
