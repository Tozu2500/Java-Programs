package conditionalsandloops;

import java.util.Random;
import java.util.Scanner;

public class SwapTwoNumbers {
	
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    
    public static void main(String[] args) {
    	System.out.println("Use '1' to choose a num, use '2' to randomly get one.");
    	
    	try {
    		Thread.sleep(1500);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	
    	int userNum = 0;
    	int randomNum = 0;
    	
    	System.out.println("Enter '1' to choose a num, use '2' to randomly get one.");
    	int choice = scanner.nextInt();
    	scanner.nextLine();
    	
    	if (choice == 1) {
    		userNum = getValidInt();
    		userNum = random.nextInt(100) + 1;
    	} else if (choice == 2) {
    		randomNum = getValidInt();
    		randomNum = random.nextInt(100) + 1;
    	} else {
    		System.out.println("Invalid choice, restarting.");
    		main(new String[] {});
    		scanner.close();
    		return;
    	}
    	System.out.println("Your num: " + userNum + " and my (random chosen) num: " + randomNum + " Let's SWAP them!");
    	swap(userNum, randomNum);
    	
    }
    
    private static int getValidInt() {
    	while(true) {
    		System.out.println("Enter a whole number");
    		String input = scanner.nextLine().trim();
    		
    		if (input.isEmpty() || input.isBlank()) {
    			System.out.println("You gave no input. Try again!");
    			continue;
    		}
    		
    		if (!input.matches("-?\\d+")) {
    			System.out.println("That's not a valid number! Try again.");
    			continue;
    		}
    		
    		return Integer.parseInt(input);
    	}
    }
    
    private static void swap(int userNum, int randomNum) {
		System.out.println("Integer 'userNum' = " + userNum);
		System.out.println("Integer 'randomNum' = " + randomNum);
		
		int temp = userNum;
		userNum = randomNum;
		randomNum = temp;

		System.out.println("After swap, they are upside down!");
		scanner.close();
		System.exit(0);
    }
}