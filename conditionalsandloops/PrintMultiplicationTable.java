package conditionalsandloops;

import java.util.Scanner;

public class PrintMultiplicationTable {
	
	private static int multiNum = 0;

	private static StringBuilder tableBuilder;
	
	@SuppressWarnings("unused")
	private static final String table = "";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a number between 1-10. It will print the multiplication table!");
		multiNum = scanner.nextInt();
		String table = printTable(multiNum);
		System.out.println(table);
		scanner.close();
	}
	
	private static String printTable(int multiNum) {
		if (multiNum > 11 || multiNum <= 0) {
			return "Invalid numbers";
		} else {
				tableBuilder = new StringBuilder();
				for (int i = 1; i < 11; i++) {
					tableBuilder.append(multiNum)
						.append(" x ")
						.append(i)
						.append(" = ")
						.append(multiNum * i)
						.append("\n");
			}
			return tableBuilder.toString();
		}
	}
}
