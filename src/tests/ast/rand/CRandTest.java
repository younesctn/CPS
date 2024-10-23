package tests.ast.rand;

import org.junit.jupiter.api.Test;

import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class CRandTest {

	@Test
	public void testEvalReturnsCorrectValue() {

		double expectedValue = 42.0;
		CRand cRand = new CRand(expectedValue);

		double result = cRand.eval(null);

		assertEquals(expectedValue, result, 0.0, "The eval method should return the exact value set at construction.");
	}

	@Test
	public void testEvalWithNegativeValue() {

		double negativeValue = -42.0;
		CRand cRand = new CRand(negativeValue);

		double result = cRand.eval(null);

		assertEquals(negativeValue, result, 0.0, "The eval method should correctly return a negative value.");
	}

	@Test
	public void testEvalWithZeroValue() {

		double zeroValue = 0.0;
		CRand cRand = new CRand(zeroValue);

		double result = cRand.eval(null);

		assertEquals(zeroValue, result, 0.0, "The eval method should correctly return zero.");
	}

	@Test
	public void testEvalWithVeryLargeValue() {

		double largeValue = Double.MAX_VALUE;
		CRand cRand = new CRand(largeValue);

		double result = cRand.eval(null);

		assertEquals(largeValue, result, 0.0, "The eval method should correctly handle very large values.");
	}

	@Test
	public void testEvalWithVerySmallValue() {

		double smallValue = Double.MIN_VALUE;
		CRand cRand = new CRand(smallValue);

		double result = cRand.eval(null);

		assertEquals(smallValue, result, 0.0, "The eval method should correctly handle very small values.");
	}
}
