package com.tozu.bitmanipulation;

public class SetBit {

	/**
	 * Sets the bit at the given position in the number
	 * 
	 * @param number    The original integer value
	 * @param position  The bit position to set (0 = least significant bit)
	 * @return 			The new integer value with the bit set
	 * */
	
	public static int setBit(int number, int position) {
		return number | (1 << position);
	}
	
	public static void main(String[] args) {
		int number = 334343335; // Binary 1010
		int position = 1;

		int result = setBit(number, position);
		System.out.println("Original number: " + number + " (binary " + Integer.toBinaryString(number) + ")");
		System.out.println("After setting bit at position " + position + ": " + result + " (binary " + Integer.toBinaryString(result) + ")");
	}

}
