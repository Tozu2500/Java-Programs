import java.util.Scanner;

public class BinaryConversionCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Binary Conversion Calculator");
        System.out.println("1. Decimal to Binary");
        System.out.println("2. Binary to Decimal");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.print("Enter a decimal number: ");
            int decimal = scanner.nextInt();

            String binary = Integer.toBinaryString(decimal);
            System.out.println("Binary: " + binary);

        } else if (choice == 2) {
            System.out.print("Enter a binary number: ");
            String binary = scanner.next();

            int decimal = Integer.parseInt(binary, 2);
            System.out.println("Decimal: " + decimal);

        } else {
            System.out.println("Invalid option.");
        }

        scanner.close();
    }
}