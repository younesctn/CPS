package tests.ast.cexp;

import org.junit.jupiter.api.Test;
import ast.cexp.LeqCExp;
import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class LeqCExpTest {

	@Test
	public void testTrueInferiorityOrEquality_Inferiority() {
		CRand rand1 = new CRand(10.0);
		CRand rand2 = new CRand(20.0);
		LeqCExp leqCExp = new LeqCExp(rand1, rand2);

		boolean result = leqCExp.eval(null);

		assertTrue(result,
				"LeqCExp should return true when the value of rand1 is less than or equal to that of rand2.");
	}

	@Test
	public void testTrueInferiorityOrEquality_Equality() {
		CRand rand1 = new CRand(30.0);
		CRand rand2 = new CRand(30.0);
		LeqCExp leqCExp = new LeqCExp(rand1, rand2);

		boolean result = leqCExp.eval(null);

		assertTrue(result, "LeqCExp should return true when the values of rand1 and rand2 are equal.");
	}

	@Test
	public void testFalseSuperiority() {
		CRand rand1 = new CRand(50.0);
		CRand rand2 = new CRand(40.0);
		LeqCExp leqCExp = new LeqCExp(rand1, rand2);

		boolean result = leqCExp.eval(null);

		assertFalse(result, "LeqCExp should return false when the value of rand1 is greater than that of rand2.");
	}
}
