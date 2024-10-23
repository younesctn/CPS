package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import app.models.QueryResult;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;

public class QueryResultTest {

	@Test
	public void testSetGather() {

		QueryResult queryResult = new QueryResult(new ArrayList<>(), new ArrayList<>());

		queryResult.setGather();

		assertTrue(queryResult.isGatherRequest(), "The query should be a gather request");
		assertFalse(queryResult.isBooleanRequest(), "The query should not be a boolean request");
	}

	@Test
	public void testSetBoolean() {

		QueryResult queryResult = new QueryResult(new ArrayList<>(), new ArrayList<>());

		queryResult.setBoolean();

		assertTrue(queryResult.isBooleanRequest(), "The query should be a boolean request");
		assertFalse(queryResult.isGatherRequest(), "The query should not be a gather request");
	}

	@Test
	public void testClone() throws CloneNotSupportedException {

		ArrayList<SensorDataI> sensorDataList = new ArrayList<>();
		ArrayList<String> sensitiveNodes = new ArrayList<>();
		QueryResult queryResult = new QueryResult(sensorDataList, sensitiveNodes);
		queryResult.setGather();

		QueryResult clonedResult = queryResult.clone();

		assertNotSame(queryResult, clonedResult, "The cloned result should not be the same instance as the original");
		assertEquals(queryResult, clonedResult, "The cloned result should be equal to the original");
		assertNotSame(queryResult.gatheredSensorsValues(), clonedResult.gatheredSensorsValues(),
				"The sensor data list should be a deep copy");
		assertNotSame(queryResult.positiveSensorNodes(), clonedResult.positiveSensorNodes(),
				"The sensitive nodes list should be a deep copy");
	}

	@Test
	public void testEquals() {

		ArrayList<SensorDataI> sensorDataList1 = new ArrayList<>();
		ArrayList<SensorDataI> sensorDataList2 = new ArrayList<>();
		ArrayList<String> sensitiveNodes1 = new ArrayList<>();
		ArrayList<String> sensitiveNodes2 = new ArrayList<>();

		QueryResult queryResult1 = new QueryResult(sensorDataList1, sensitiveNodes1);
		QueryResult queryResult2 = new QueryResult(sensorDataList2, sensitiveNodes2);

		assertEquals(queryResult1, queryResult2, "The two query results should be equal since they have the same data");

		sensorDataList1.add(new TestSensorData("Sensor1"));

		assertNotEquals(queryResult1, queryResult2, "The two query results should not be equal after modifying one");
	}

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
}
