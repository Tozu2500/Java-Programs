package com.tozu.arrays;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class ArrayAverage {

	public static void main(String[] args) {
		
		int[] arr = new int[20];
		
		Random r = new Random();
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = r.nextInt(101);
		}
		
		System.out.println("Array: " + Arrays.toString(arr));
		
		int sum = 0;

		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		
		System.out.println("Sum: " + sum);
        
        double average = (double) sum / arr.length;
        
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.forLanguageTag("fi-FI"));
        DecimalFormat df = new DecimalFormat("0.000000", dfs);

        System.out.println("Array average: " + df.format(average));


	}

}
