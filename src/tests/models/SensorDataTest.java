package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

import app.models.SensorData;

class SensorDataTest {

	@Test
	void testConstructorAndGetters() {

		String nodeIdentifier = "Node1";
		String sensorIdentifier = "Sensor1";
		Double value = 25.5;
		Instant beforeCreation = Instant.now();

		SensorData sensorData = new SensorData(nodeIdentifier, sensorIdentifier, value);
		Instant afterCreation = Instant.now();

		assertEquals(nodeIdentifier, sensorData.getNodeIdentifier(), "Node identifier should match the input.");
		assertEquals(sensorIdentifier, sensorData.getSensorIdentifier(), "Sensor identifier should match the input.");
		assertEquals(value, sensorData.getValue(), "Value should match the input.");

		// Check timestamp is within the range of time the object was created
		assertTrue(!sensorData.getTimestamp().isBefore(beforeCreation),
				"Timestamp should be after or equal to the start time.");
		assertTrue(!sensorData.getTimestamp().isAfter(afterCreation),
				"Timestamp should be before or equal to the end time.");
	}

	@Test
	void testEquals() {

		SensorData data1 = new SensorData("Node1", "Sensor1", 25.5);
		SensorData data2 = new SensorData("Node1", "Sensor1", 25.5);
		SensorData data3 = new SensorData("Node2", "Sensor1", 25.5);
		SensorData data4 = new SensorData("Node1", "Sensor2", 25.5);
		SensorData data5 = new SensorData("Node1", "Sensor1", 30.0);

		assertEquals(data1, data2, "Equal sensor data objects should return true.");
		assertNotEquals(data1, data3, "Different node identifiers should return false.");
		assertNotEquals(data1, data4, "Different sensor identifiers should return false.");
		assertNotEquals(data1, data5, "Different values should return false.");
	}

	@Test
	void testClone() throws CloneNotSupportedException {

		SensorData originalData = new SensorData("Node1", "Sensor1", 25.5);

		SensorData clonedData = originalData.clone();

		assertEquals(originalData, clonedData, "Cloned data should be equal to the original data.");
		assertNotSame(originalData, clonedData, "Cloned data should not be the same object instance as the original.");
	}

	@Test
	void testToString() {

		SensorData data = new SensorData("Node1", "Sensor1", 25.5);

		String result = data.toString();

		assertEquals("Node1: Sensor1(25.5)", result, "The string representation should match the expected format.");
	}
}
