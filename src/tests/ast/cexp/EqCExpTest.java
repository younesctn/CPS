package tests.ast.cexp;

import org.junit.jupiter.api.Test;

import ast.cexp.EqCExp;
import ast.rand.CRand;

import static org.junit.jupiter.api.Assertions.*;

public class EqCExpTest {

	@Test
	public void testEgaliteDesValeurs() {
		double valeur = 42.0;
		CRand rand1 = new CRand(valeur);
		CRand rand2 = new CRand(valeur);
		EqCExp eqCExp = new EqCExp(rand1, rand2);

		// Evaluation
		boolean resultat = eqCExp.eval(null);

		assertTrue(resultat,
				"EqCExp should return true when the two evaluated instances of CRand have the same value.");
	}

	@Test
	public void testInegaliteDesValeurs() {
		CRand rand1 = new CRand(42.0);
		CRand rand2 = new CRand(43.0);
		EqCExp eqCExp = new EqCExp(rand1, rand2);

		// Evaluation
		boolean resultat = eqCExp.eval(null);

		assertFalse(resultat,
				"EqCExp should return false when the two evaluated instances of CRand have different values.");
	}
}
