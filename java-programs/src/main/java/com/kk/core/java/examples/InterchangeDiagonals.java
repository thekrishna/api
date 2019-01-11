package com.kk.core.java.examples;

import java.util.Scanner;

/**
 * This class accept a Matrix of Order MxN & Interchange the Diagonals. 
 * 
 * @author Krishna Kumar
 *
 */
public class InterchangeDiagonals {

	public static void main(String a[]) {
		try {
			final InterchangeDiagonals obj = new InterchangeDiagonals();
			final String matrix[][] = obj.readMatrix();
			
			System.out.println("\nMatrix before interchanging diagonals");
			obj.printMatrix(matrix);
			
			obj.swapDiagonal(matrix);
			
			System.out.println("\nMatrix after interchanging diagonals");
			obj.printMatrix(matrix);
		} catch (final Exception e) {
			System.out.println("Exception occured while interchanging the diagonals."+e.getLocalizedMessage());
		}
	}
	
	/**
	 * This method is used to read the matrix.
	 * 
	 * @param matrix
	 */
	String[][] readMatrix(){
		int n = 0;
		String matrix[][];
		try (final Scanner s = new Scanner(System.in)) {
			System.out.print("Enter the size of n in matrix : ");
			n = s.nextInt();

			matrix = new String[n][n];
			System.out.println("Enter all the elements of matrix : " + matrix.length);

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					matrix[i][j] = s.next();
				}
			}
		}

		return matrix;

	}
	
	/**
	 * This method is used to perform the swapping of diagonals.
	 * 
	 * @param matrix
	 */
	void swapDiagonal(final String[][] matrix) {
		for (int i = 0, j = matrix[i].length - i - 1; i < matrix.length; i++, j--) {
			final String temp = matrix[i][i];
			matrix[i][i] = matrix[i][j];
			matrix[i][j] = temp;
		}

	}

	/**
	 * This method is used to print the matrix.
	 * 
	 * @param matrix
	 */
	void printMatrix(final String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("");
		}
	}

}
