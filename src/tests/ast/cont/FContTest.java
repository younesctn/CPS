package tests.ast.cont;

import app.models.ExecutionState;
import app.models.Position;
import ast.base.IBase;
import ast.cont.FCont;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FContTest {

	private FCont fCont;
	private ExecutionState es;
	private IBase mockBase;
	private final double maxDistance = 100.0;
	private PositionI expectedPosition;

	@BeforeEach
	void setUp() {
		// Configuring an expected position
		expectedPosition = new Position(50.0, 50.0);

		// Creating a simple mock for IBase
		mockBase = new IBase() {
			@Override
			public PositionI eval(ExecutionStateI es) {
				return expectedPosition;
			}
		};

		// Initializing FCont with the mock of IBase and a maximum distance
		fCont = new FCont(mockBase, maxDistance);

		// Initializing the execution state
		es = new ExecutionState(null, null);
	}

	@Test
	void testEvalSetsExecutionStateCorrectly() {
		// Executing eval
		fCont.eval(es);

		// Verifying that the maximum distance is correctly configured
		assertEquals(maxDistance, es.getMaxDistance(), "The maximum distance should be updated.");

		// Verifying that the position is correctly configured
		assertEquals(expectedPosition, es.getPosition(), "The position should be updated.");

		// Verifying that the flooding state is activated
		assertTrue(es.isFlooding(), "The flooding state should be activated.");
	}
}
