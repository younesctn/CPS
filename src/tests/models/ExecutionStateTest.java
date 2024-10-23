package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import app.models.ExecutionState;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ProcessingNodeI;

public class ExecutionStateTest {

	// Implémentation concrète minimale de ProcessingNodeI pour les tests
	static class TestProcessingNode implements ProcessingNodeI {
		private final String uri;

		TestProcessingNode(String uri) {
			this.uri = uri;
		}

		public String getUri() {
			return uri;
		}

		@Override
		public String getNodeIdentifier() {

			return null;
		}

		@Override
		public PositionI getPosition() {

			return null;
		}

		@Override
		public Set<NodeInfoI> getNeighbours() {

			return null;
		}

		@Override
		public SensorDataI getSensorData(String sensorIdentifier) {

			return null;
		}
	}

	// Implémentation concrète minimale de QueryResultI pour les tests
	@SuppressWarnings("serial")
	static class TestQueryResult implements QueryResultI {
		@Override
		public boolean isBooleanRequest() {
			return false;
		}

		@Override
		public boolean isGatherRequest() {
			return false;
		}

		@Override
		public ArrayList<SensorDataI> gatheredSensorsValues() {
			return new ArrayList<>();
		}

		@Override
		public ArrayList<String> positiveSensorNodes() {
			return new ArrayList<>();
		}
	}

	@Test
	public void testGetProcessingNode() {

		ProcessingNodeI expectedNode = new TestProcessingNode("nodeURI");
		ExecutionState state = new ExecutionState(expectedNode, new TestQueryResult());

		ProcessingNodeI actualNode = state.getProcessingNode();

		assertEquals(expectedNode, actualNode);
	}

	@Test
	public void testGetCurrentResult() {

		QueryResultI expectedResult = new TestQueryResult();
		ExecutionState state = new ExecutionState(new TestProcessingNode("nodeURI"), expectedResult);

		QueryResultI actualResult = state.getCurrentResult();

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testIsDirectional() {

		ExecutionState state = new ExecutionState(new TestProcessingNode("nodeURI"), new TestQueryResult());

		assertFalse(state.isDirectional(), "Should not be directional initially");

		state.setDirectional();

		assertTrue(state.isDirectional(), "Should be directional after setting it");
	}

	@Test
	public void testIsFlooding() {

		ExecutionState state = new ExecutionState(new TestProcessingNode("nodeURI"), new TestQueryResult());

		assertFalse(state.isFlooding(), "Should not be flooding initially");

		state.setFlooding();

		assertTrue(state.isFlooding(), "Should be flooding after setting it");
	}

	@Test
	public void testDirections() {

		ExecutionState state = new ExecutionState(new TestProcessingNode("nodeURI"), new TestQueryResult());
		Set<Direction> expectedDirections = new HashSet<>(Arrays.asList(Direction.NE, Direction.SE));

		state.setDirections(expectedDirections);
		Set<Direction> actualDirections = state.getDirections();

		assertEquals(expectedDirections, actualDirections, "The directions should match the expected set");
	}

	@Test
	public void testIncrementHops() {

		ExecutionState state = new ExecutionState(new TestProcessingNode("nodeURI"), new TestQueryResult());
		int initialHops = state.getHops();

		state.incrementHops();
		int actualHops = state.getHops();

		assertEquals(initialHops + 1, actualHops, "Hops should be incremented by 1");
	}

	@Test
	public void testWithinMaximalDistance() {

		ExecutionState state = new ExecutionState(new TestProcessingNode("nodeURI"), new TestQueryResult());
		PositionI currentPosition = new TestPosition(0, 0);
		PositionI targetPosition = new TestPosition(3, 4);
		state.setPosition(currentPosition);
		state.setMaxDistance(5.0);

		boolean isWithinDistance = state.withinMaximalDistance(targetPosition);

		assertTrue(isWithinDistance, "The target position should be within the maximal distance");
	}

	@SuppressWarnings("serial")
	static class TestPosition implements PositionI {
		private final double x;
		private final double y;

		TestPosition(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		@Override
		public double distance(PositionI p) {
			double dx = this.x - ((TestPosition) p).getX();
			double dy = this.y - ((TestPosition) p).getY();
			return Math.sqrt(dx * dx + dy * dy);
		}

		@Override
		public Direction directionFrom(PositionI p) {

			return null;
		}

		@Override
		public boolean northOf(PositionI p) {

			return false;
		}

		@Override
		public boolean southOf(PositionI p) {

			return false;
		}

		@Override
		public boolean eastOf(PositionI p) {

			return false;
		}

		@Override
		public boolean westOf(PositionI p) {

			return false;
		}
	}
}
