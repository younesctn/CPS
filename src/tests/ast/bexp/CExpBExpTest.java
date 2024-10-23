package tests.ast.bexp;

import org.junit.jupiter.api.Test;

import ast.bexp.CExpBExp;
import ast.cexp.GeqCExp;
import ast.cexp.LeqCExp;
import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class CExpBExpTest {

	@Test
	public void testCExpBExpTrue() {
		double trueValue = 1.0;
		CRand randTrue = new CRand(trueValue);
		GeqCExp geqCExpTrue = new GeqCExp(randTrue, randTrue);

		CExpBExp cExpBExpTrue = new CExpBExp(geqCExpTrue);

		// Evaluation
		boolean result = cExpBExpTrue.eval(null);

		assertTrue(result, "CExpBExp shoud return true when the evaluated expression CExp is true.");
	}

	@Test
	public void testCExpBExpFalse() {
		double trueValue = 1.0;
		double falseValue = 0.0;
		CRand randTrue = new CRand(trueValue);
		CRand randFalse = new CRand(falseValue);
		LeqCExp geqCExpFalse = new LeqCExp(randTrue, randFalse);

		CExpBExp cExpBExpFalse = new CExpBExp(geqCExpFalse);

		// Evaluation
		boolean result = cExpBExpFalse.eval(null);

		assertFalse(result, "CExpBExp shoud return false when the evaluated expression CExp is false.");
	}
}
