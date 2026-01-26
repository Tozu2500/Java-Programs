package com.tozu.arrays;

import java.util.Scanner;

public class MatrixAddition {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter number of rows: ");
		int rows = scanner.nextInt();
		
		System.out.print("Enter number of columns: ");
		int cols = scanner.nextInt();
		
		int[][] A = new int[rows][cols];
		int[][] B = new int[rows][cols];
		int[][] C = new int[rows][cols];
		
		System.out.println("\nEnter elements of Matrix A:");
		fillMatrix(scanner, A);
		
		System.out.println("\nEnter elements of Matrix B:");
		fillMatrix(scanner, B);
		
		addMatrices(A, B, C);
		
		System.out.println("\nResult of A + B:");
		printMatrix(C);
		
		scanner.close();
	}
	
	public static void fillMatrix(Scanner scanner, int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = scanner.nextInt();
			}
		}
	}
	
	public static void addMatrices(int[][] A, int[][] B, int[][] C) {
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				C[i][j] = A[i][j] + B[i][j];
			}
		}
	}
	
	public static void printMatrix(int[][] matrix) {
		for (int[] row : matrix) {
			for (int value : row) {
				System.out.print(value + " ");
			}
			System.out.println();
		}
	}

}
