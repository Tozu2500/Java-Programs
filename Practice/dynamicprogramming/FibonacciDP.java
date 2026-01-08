package com.tozu.dynamicprogramming;

public class FibonacciDP {
	
	// Bottom-up DP (Tabulation)
	public static int fibonacciTab(int n) {
		if (n <= 1) return n;
		
		int[] dp = new int[n + 1];
		dp[0] = 0;
		dp[1] = 1;
		
		for (int i = 2; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}
		
		return dp[n];
	}

	public static void main(String[] args) {
		int n = 15;
		System.out.println("Fibonacci(" + n + ") = " + fibonacciTab(n));
	}

}
