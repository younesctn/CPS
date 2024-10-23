package tests.ast.bases;

import app.models.ExecutionState;
import app.models.Position;
import app.models.ProcessingNode;
import ast.base.RBase;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ProcessingNodeI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RBaseTest {

	@Test
	public void testEval() {
		Position expectedPosition = new Position(2.0, 3.0);
		Position notExpectedPosition = new Position(2.0, 2.0);
		ProcessingNodeI processingNode = new ProcessingNode("Test", new Position(2.0, 3.0), null, null);
		ExecutionStateI es = new ExecutionState(processingNode, null);
		RBase rBase = new RBase();
		Position result = (Position) rBase.eval(es);

		assertNotNull(result);
		assertTrue(result instanceof Position);
		System.out.println("result = (" + result.getx() + ", " + result.gety() + ") Expect = ("
				+ expectedPosition.getx() + ", " + expectedPosition.gety() + ")");
		assertEquals(expectedPosition, result);
		assertNotEquals(notExpectedPosition, result);

	}
}
