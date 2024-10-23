package tests.ast.cexp;

import org.junit.jupiter.api.Test;
import ast.cexp.GCExp;
import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class GCExpTest {

	@Test
	public void testTrueSuperiority() {
		double largeValue = 45.0;
		double smallValue = 30.0;
		CRand rand1 = new CRand(largeValue);
		CRand rand2 = new CRand(smallValue);
		GCExp gcExp = new GCExp(rand1, rand2);

		boolean result = gcExp.eval(null);

		assertTrue(result, "GCExp should return true when the value of rand1 is greater than that of rand2.");
	}

	@Test
	public void testFalseSuperiority() {
		double smallValue = 20.0;
		double largeValue = 40.0;
		CRand rand1 = new CRand(smallValue);
		CRand rand2 = new CRand(largeValue);
		GCExp gcExp = new GCExp(rand1, rand2);

		boolean result = gcExp.eval(null);

		assertFalse(result, "GCExp should return false when the value of rand1 is less than that of rand2.");
	}

	@Test
	public void testEquality() {
		double equalValue = 25.0;
		CRand rand1 = new CRand(equalValue);
		CRand rand2 = new CRand(equalValue);
		GCExp gcExp = new GCExp(rand1, rand2);

		boolean result = gcExp.eval(null);

		assertFalse(result, "GCExp should return false when the values of rand1 and rand2 are equal.");
	}
}
