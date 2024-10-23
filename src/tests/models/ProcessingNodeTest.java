package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.ProcessingNode;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class ProcessingNodeTest {

	@SuppressWarnings("serial")
	static class TestPosition implements PositionI {
		private double x;
		private double y;

		public TestPosition(double x, double y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public double distance(PositionI p) {
			if (p instanceof TestPosition) {
				TestPosition other = (TestPosition) p;
				return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
			}
			return 0;
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

	// Classe de test pour NodeInfoI
	@SuppressWarnings("serial")
	static class TestNodeInfo implements NodeInfoI {
		private String identifier;
		private PositionI position;

		public TestNodeInfo(String identifier, PositionI position) {
			this.identifier = identifier;
			this.position = position;
		}

		@Override
		public String nodeIdentifier() {
			return identifier;
		}

		@Override
		public PositionI nodePosition() {
			return position;
		}

		@Override
		public EndPointDescriptorI endPointInfo() {

			return null;
		}

		@Override
		public double nodeRange() {

			return 0;
		}

		@Override
		public EndPointDescriptorI p2pEndPointInfo() {

			return null;
		}
	}

	// Classe de test pour SensorDataI
	@SuppressWarnings("serial")
	static class TestSensorData implements SensorDataI {
		private String sensorIdentifier;

		public TestSensorData(String sensorIdentifier) {
			this.sensorIdentifier = sensorIdentifier;
		}

		@Override
		public String getSensorIdentifier() {
			return sensorIdentifier;
		}

		public Double getSensorValue() {

			return null;
		}

		public boolean isValidSensorValue() {

			return false;
		}

		@Override
		public String getNodeIdentifier() {

			return null;
		}

		@Override
		public Class<? extends Serializable> getType() {

			return null;
		}

		@Override
		public Serializable getValue() {

			return null;
		}

		@Override
		public Instant getTimestamp() {

			return null;
		}
	}

	@Test
	public void testGetNodeIdentifier() {

		String nodeIdentifier = "Node1";
		ProcessingNode node = new ProcessingNode(nodeIdentifier, null, null, null);

		String result = node.getNodeIdentifier();

		assertEquals(nodeIdentifier, result, "The node identifier should match the input");
	}

	@Test
	public void testGetPosition() {

		PositionI position = new TestPosition(0, 0);
		ProcessingNode node = new ProcessingNode("Node1", position, null, null);

		PositionI result = node.getPosition();

		assertEquals(position, result, "The position should match the input");
	}

	@Test
	public void testGetNeighbours() {

		PositionI position1 = new TestPosition(0, 0);
		PositionI position2 = new TestPosition(1, 1);
		NodeInfoI neighbour1 = new TestNodeInfo("Neighbour1", position1);
		NodeInfoI neighbour2 = new TestNodeInfo("Neighbour2", position2);
		Set<NodeInfoI> neighbours = new HashSet<>();
		neighbours.add(neighbour1);
		neighbours.add(neighbour2);

		ProcessingNode node = new ProcessingNode("Node1", null, neighbours, null);

		Set<NodeInfoI> result = node.getNeighbours();

		assertEquals(neighbours, result, "The neighbours should match the input");
	}

	@Test
	public void testGetSensorData() {

		SensorDataI sensorData1 = new TestSensorData("Sensor1");
		SensorDataI sensorData2 = new TestSensorData("Sensor2");
		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(sensorData1);
		sensorDataSet.add(sensorData2);

		ProcessingNode node = new ProcessingNode("Node1", null, null, sensorDataSet);

		SensorDataI result = node.getSensorData("Sensor1");

		assertEquals(sensorData1, result, "The sensor data should match the input");

		SensorDataI nonExistentResult = node.getSensorData("NonExistentSensor");

		assertNull(nonExistentResult, "Should return null for non-existent sensor identifier");
	}
}
