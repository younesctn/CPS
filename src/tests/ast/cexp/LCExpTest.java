package tests.ast.cexp;

import org.junit.jupiter.api.Test;
import ast.cexp.LCExp;
import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class LCExpTest {

	@Test
	public void testTrueInferiority() {
		CRand rand1 = new CRand(10.0);
		CRand rand2 = new CRand(20.0);
		LCExp lcExp = new LCExp(rand1, rand2);

		boolean result = lcExp.eval(null);

		assertTrue(result, "LCExp should return true when the value of rand1 is less than that of rand2.");
	}

	@Test
	public void testFalseEquality() {
		CRand rand1 = new CRand(30.0);
		CRand rand2 = new CRand(30.0);
		LCExp lcExp = new LCExp(rand1, rand2);

		boolean result = lcExp.eval(null);

		assertFalse(result, "LCExp should return false when the values of rand1 and rand2 are equal.");
	}

	@Test
	public void testFalseSuperiority() {
		CRand rand1 = new CRand(50.0);
		CRand rand2 = new CRand(40.0);
		LCExp lcExp = new LCExp(rand1, rand2);

		boolean result = lcExp.eval(null);

		assertFalse(result, "LCExp should return false when the value of rand1 is greater than that of rand2.");
	}
}
