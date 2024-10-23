package tests.ast.dirs;

import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ast.dirs.IDirs;
import ast.dirs.Rdirs;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RdirsTest {

	private Rdirs rdirs;
	private IDirs baseDirs;
	private Direction addedDirection;

	@BeforeEach
	void setUp() {
		// Creating a base implementation of IDirs that returns a fixed set of
		// directions
		baseDirs = new IDirs() {
			@Override
			public Set<Direction> eval() {
				Set<Direction> baseDirections = new HashSet<>();
				baseDirections.add(Direction.NE);
				baseDirections.add(Direction.SE);
				return baseDirections;
			}
		};

		// Direction to be added
		addedDirection = Direction.NW;

		// Creating Rdirs with the added direction and the base implementation
		rdirs = new Rdirs(addedDirection, baseDirs);
	}

	@Test
	void testEvalCombinesDirectionsCorrectly() {
		// Evaluation
		Set<Direction> result = rdirs.eval();

		// Verifying that the result contains all expected directions
		assertTrue(result.contains(Direction.NE), "The result should contain NE.");
		assertTrue(result.contains(Direction.SE), "The result should contain SE.");
		assertTrue(result.contains(addedDirection), "The result should contain NW.");
		assertEquals(3, result.size(), "The result should contain exactly three directions.");
	}
}
