package problemsolving;

import java.util.Arrays;

public class StreamsAverage {

	public static void main(String[] args) {
		int arr[] = {5, 2, 7, 6, 5, 5, 10, 15, 13, 13, 14, 1, 3, 1, 2};
		
		for (int num : arr) {
			System.out.print("The numbers: " + num + ", ");
		}
		
		// Using streams to get the average from that array.
		double average = Arrays.stream(arr)
			.average()
			.orElse(0.0);
		
		System.out.println("Average: " + average);
	}

}
