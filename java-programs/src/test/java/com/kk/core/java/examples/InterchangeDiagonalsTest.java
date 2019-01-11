package com.kk.core.java.examples;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Krishna Kumar
 *
 */
public class InterchangeDiagonalsTest {
	
	private static InterchangeDiagonals interchangeDiagonals ;
	
	@BeforeClass
	public static void  init() {
		interchangeDiagonals = new InterchangeDiagonals();
	}

	@Test
	public void test_valid_diagonal_interchange() {
		final String matrix[][] = { { "A", "B", "C", "D", "E" }, { "F", "G", "H", "I", "J" }, { "K", "L", "M", "N", "O" },
				{ "P", "Q", "R", "S", "T" }, { "U", "V", "W", "X", "Y" } };
		
		System.out.println("\nMatrix before interchanging diagonals");
		interchangeDiagonals.printMatrix(matrix);
		
		interchangeDiagonals.swapDiagonal(matrix);
		
		System.out.println("\nMatrix after interchanging diagonals");
		interchangeDiagonals.printMatrix(matrix);
	}
	

}
