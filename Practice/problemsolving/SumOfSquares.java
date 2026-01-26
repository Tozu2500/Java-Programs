package com.tozu.problemsolving;

/*
 * Given a positive integer n, we have to find the sum of squares of first n natural numbers.
 * 
 * */
public class SumOfSquares {
	
	public static int sum(int n) {
		int sum = 0;
		for (int i = 1; i <= n; i++) {
			sum += (i * i);
		}
		return sum;
	}

	public static void main(String[] args) {
		int n = 2;
		System.out.println(sum(n));
	}
}
