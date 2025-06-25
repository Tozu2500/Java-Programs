package mathandlogic;

import java.util.Scanner;

public class FactorialOfNum {
	
	public static void main(String[] args) {
		System.out.println("Enter a number, we will calculate the factorial!");
		Scanner scanner = new Scanner(System.in);
		long num = scanner.nextInt();
		scanner.close();
		
		System.out.print("You picked: " + num + "\n\n");
		long result = factorial(num);
		System.out.println("Result: " + result);
	}

	private static long factorial(long num) {
		long result = num;
		for (int i = 2; i <= num; i++) {
			result = result * i;
		}
		return result;
	}

}
