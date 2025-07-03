package problemsolving;

import java.util.Random;

// Given two integers, n and m. The task is to check the relation between n and m. Return "lesser", if n < m,
// "equal", if n == m, and "greater" if n > m.
public class DecisionMaking {

	public static void main(String[] args) {
		Random random = new Random();
		int n = Math.abs(random.nextInt() % 1000 + 1);
		int m = Math.abs(random.nextInt() % 1000 + 1);
		
		System.out.println("The numbers: " + n + " " + m);
		
		String output = checkNums(n, m);
		System.out.println("The output was: " + output);
	}
	
	private static String checkNums(int n, int m) {
		if (n < m) {
			return "Lesser";
		} else if (n > m) {
			return "Greater";
		} else {
			return "Equal";
		}
	}
	
}
