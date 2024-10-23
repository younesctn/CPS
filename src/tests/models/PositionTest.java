package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Position;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;

public class PositionTest {

	@Test
	public void testDistance() {

		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(3, 4); // A known distance of 5 from pos1

		double distance = pos1.distance(pos2);

		assertEquals(5.0, distance, "The distance should be 5.0");
	}

	@Test
	public void testDirectionFrom() {

		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(3, 4);
		Position pos3 = new Position(-3, -4);

		Direction dir1 = pos1.directionFrom(pos2);
		Direction dir2 = pos1.directionFrom(pos3);

		assertEquals(Direction.NE, dir1, "Direction from pos1 to pos2 should be NE");
		assertEquals(Direction.SW, dir2, "Direction from pos1 to pos3 should be SW");
	}

	@Test
	public void testNorthOf() {

		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(0, -1);

		boolean isNorthOf = pos2.northOf(pos1);

		assertTrue(isNorthOf, "pos1 should be north of pos2");
	}

	@Test
	public void testSouthOf() {

		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(0, 1);

		boolean isSouthOf = pos2.southOf(pos1);

		assertTrue(isSouthOf, "pos1 should be south of pos2");
	}

	@Test
	public void testEastOf() {

		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(-1, 0);

		boolean isEastOf = pos2.eastOf(pos1);

		assertTrue(isEastOf, "pos1 should be east of pos2");
	}

	@Test
	public void testWestOf() {

		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(1, 0);

		boolean isWestOf = pos2.westOf(pos1);

		assertTrue(isWestOf, "pos1 should be west of pos2");
	}
}
