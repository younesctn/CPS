package tests.ast.cexp;

import org.junit.jupiter.api.Test;
import ast.cexp.GeqCExp;
import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class GeqCExpTest {

	@Test
	public void testTrueSuperiorityOrEquality_Superiority() {
		CRand rand1 = new CRand(50.0);
		CRand rand2 = new CRand(30.0);
		GeqCExp geqCExp = new GeqCExp(rand1, rand2);

		boolean result = geqCExp.eval(null);

		assertTrue(result, "GeqCExp should return true when the value of rand1 is greater than that of rand2.");
	}

	@Test
	public void testTrueSuperiorityOrEquality_Equality() {
		CRand rand1 = new CRand(40.0);
		CRand rand2 = new CRand(40.0);
		GeqCExp geqCExp = new GeqCExp(rand1, rand2);

		boolean result = geqCExp.eval(null);

		assertTrue(result, "GeqCExp should return true when the values of rand1 and rand2 are equal.");
	}

	@Test
	public void testFalseInferiority() {
		CRand rand1 = new CRand(20.0);
		CRand rand2 = new CRand(40.0);
		GeqCExp geqCExp = new GeqCExp(rand1, rand2);

		boolean result = geqCExp.eval(null);

		assertFalse(result, "GeqCExp should return false when the value of rand1 is less than that of rand2.");
	}
}
