package tests.ast.dirs;

import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import org.junit.jupiter.api.Test;
import ast.dirs.Fdirs;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

public class FdirsTest {

	@Test
	void testEvalSingleDirection() {
		// Initializing Fdirs with a specific direction
		Fdirs fdirs = new Fdirs(Direction.NE);

		// Evaluation
		Set<Direction> result = fdirs.eval();

		// Verifying that the returned set contains exactly the specified direction
		assertTrue(result.contains(Direction.NE), "The set should contain direction NE.");
		assertEquals(1, result.size(), "The set should contain exactly one direction.");

		// Repeating the test for each direction to ensure complete coverage
		Fdirs fdirsSE = new Fdirs(Direction.SE);
		Set<Direction> resultSE = fdirsSE.eval();
		assertTrue(resultSE.contains(Direction.SE), "The set should contain direction SE.");
		assertEquals(1, resultSE.size(), "The set should contain exactly one direction.");

		Fdirs fdirsNW = new Fdirs(Direction.NW);
		Set<Direction> resultNW = fdirsNW.eval();
		assertTrue(resultNW.contains(Direction.NW), "The set should contain direction NW.");
		assertEquals(1, resultNW.size(), "The set should contain exactly one direction.");

		Fdirs fdirsSW = new Fdirs(Direction.SW);
		Set<Direction> resultSW = fdirsSW.eval();
		assertTrue(resultSW.contains(Direction.SW), "The set should contain direction SW.");
		assertEquals(1, resultSW.size(), "The set should contain exactly one direction.");
	}
}
