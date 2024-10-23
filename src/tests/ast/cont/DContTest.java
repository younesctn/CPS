package tests.ast.cont;

import app.models.ExecutionState;
import ast.cont.DCont;
import ast.dirs.Fdirs;
import ast.dirs.IDirs;
import ast.dirs.Rdirs;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DContTest {

	private DCont dCont;
	private ExecutionState es;
	private IDirs dirs;
	private final int maxHops = 5;

	@BeforeEach
	void setUp() {
		Fdirs fdirs = new Fdirs(Direction.NE); // Fdirs initialized with North-East direction

		// Configure Rdirs to add an additional direction
		Direction additionalDirection = Direction.SW; // Direction to be added
		dirs = new Rdirs(additionalDirection, fdirs); // Rdirs adds South-West to the set returned by Fdirs

		// Initialize DCont with Rdirs and maximum hops
		dCont = new DCont(dirs, maxHops);

		// Initialize execution state
		es = new ExecutionState(null, null);
	}

	@Test
	void testEvalUpdatesExecutionState() {
		// Execute eval on DCont
		dCont.eval(es);

		// Verify hops increment
		assertEquals(1, es.getHops(), "The number of hops should be incremented by 1.");

		// Verify max hops configuration
		assertEquals(maxHops, es.getMaxHops(), "The maximum number of hops should be updated.");

		// Verify directions update
		Set<Direction> expectedDirections = new HashSet<>();
		expectedDirections.add(Direction.NE);
		expectedDirections.add(Direction.SW);
		assertEquals(expectedDirections, es.getDirections(), "Directions should be updated correctly with NE and SW.");

		// Verify if state is marked as directional
		assertTrue(es.isDirectional(), "The state should be marked as directional.");
	}
}
