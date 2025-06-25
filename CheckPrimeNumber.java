package mathandlogic;

import java.util.Scanner;

public class CheckPrimeNumber {

	private static boolean isPrime = false;
	private static int userNum;
	
	public static void main(String[] args) {
		System.out.println("Welcome! This program checks whether or not a number you entered is a prime number or not!");
		Scanner scanner = new Scanner(System.in);
		userNum = scanner.nextInt();
		scanner.close(); // Free resources
		
		isPrime(userNum);
		System.out.println("Answer for " + userNum + " is " + isPrime);
	}

	private static boolean isPrime(int userNum) {
		if (userNum <= 1) {
			return false;
		}
		for (int i = 2; i <= Math.sqrt(userNum); i++) {
			if (userNum % i == 0) return false;
		}
		return true;
	}
}
