package tests.ast.rand;

import app.models.ExecutionState;
import app.models.Position;
import app.models.ProcessingNode;
import app.models.SensorData;
import ast.rand.SRand;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SRandTest {

	@Test
	public void testEvalWithValidSensorData() {
		// Set up sensor data
		String sensorId = "temperature";
		double expectedValue = 42.0;
		SensorData sensorData = new SensorData("node1", sensorId, expectedValue);

		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(sensorData);

		ProcessingNode processingNode = new ProcessingNode("node1", new Position(0, 0), null, sensorDataSet);
		ExecutionState executionState = new ExecutionState(processingNode, null);

		SRand sRand = new SRand(sensorId);
		double result = sRand.eval(executionState);

		assertEquals(expectedValue, result, 0.0, "The eval method should return the correct sensor value.");
	}

	@Test
	public void testEvalWithMissingSensorData() {
		// Setup with no matching sensor data
		String sensorId = "sensor1";
		String nonExistentSensorId = "sensor2";
		double sensorValue = 50.0;
		SensorData sensorData = new SensorData(sensorId, "temperature", sensorValue);

		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(sensorData);

		ProcessingNode processingNode = new ProcessingNode("node1", new Position(0, 0), null, sensorDataSet);
		ExecutionState executionState = new ExecutionState(processingNode, null);

		SRand sRand = new SRand(nonExistentSensorId);
		assertThrows(NullPointerException.class, () -> sRand.eval(executionState),
				"Should throw NullPointerException if sensor data does not exist.");
	}

	@Test
	public void testEvalWithIncorrectType() {
		// Setup sensor data with incorrect type (not double)
		String sensorId = "Temperature";
		String incorrectTypeValue = "not_a_double";
		SensorData sensorData = new SensorData("node1", sensorId, incorrectTypeValue);

		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(sensorData);

		ProcessingNode processingNode = new ProcessingNode("node1", new Position(0, 0), null, sensorDataSet);
		ExecutionState executionState = new ExecutionState(processingNode, null);

		SRand sRand = new SRand(sensorId);
		assertThrows(ClassCastException.class, () -> sRand.eval(executionState),
				"Should throw ClassCastException if sensor data value is not a double.");
	}
}
