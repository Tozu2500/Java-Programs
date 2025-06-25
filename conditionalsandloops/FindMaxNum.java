package conditionalsandloops;

import java.util.Scanner;

public class FindMaxNum {
	
	private static Scanner scanner;
	
	@SuppressWarnings("unused")
	private int num1;
	@SuppressWarnings("unused")
	private int num2;
	@SuppressWarnings("unused")
	private int num3;
	
	public static void main(String[] args) {
		System.out.println("Choose 3 nums, I find the max!");
		
		scanner = new Scanner(System.in);
		int num1 = scanner.nextInt();
		scanner.nextLine();
		System.out.println("num1 is " + num1);
		int num2 = scanner.nextInt();
		scanner.nextLine();
		System.out.println("num2 is " + num2);
		int num3 = scanner.nextInt();
		System.out.println("num3 is " + num3);
		scanner.close();
		
		try {
			Thread.sleep(1000);
			maxOfThree(num1, num2, num3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(num3 + " Is the max");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Automatic program closing! See you, thanks for playing!");
		System.exit(0);
	}

	private static int maxOfThree(int num1, int num2, int num3) {
		if (num1 >= num2 && num1 >= num3) {
			return num1;
		} else if (num2 >= num1 && num2 >= num3) {
			return num2;
		} else {
			return num3;
		}
	}
}
