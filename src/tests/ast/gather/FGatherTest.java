package tests.ast.gather;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import app.models.ExecutionState;
import app.models.ProcessingNode;
import app.models.SensorData;
import ast.gather.FGather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FGatherTest {

	private FGather fGather;
	private ExecutionStateI es;
	private final String sensorID = "Temperature";

	@BeforeEach
	void setUp() {
		// Configuring FGather with a sensorID
		fGather = new FGather(sensorID);

		// Creating a ProcessingNode with sensor data
		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(new SensorData("node1", sensorID, 25.5)); // Fictitious value for testing purposes

		ProcessingNode processingNode = new ProcessingNode("node1", null, new HashSet<>(), sensorDataSet);

		// Configuring ExecutionState to use our ProcessingNode
		es = new ExecutionState(processingNode, null);
	}

	@Test
	void testEvalReturnsCorrectSensorData() {
		// Executing eval to retrieve sensor data
		List<SensorDataI> result = fGather.eval(es);

		// Verifying that the result contains the correct sensor data
		assertNotNull(result, "The list of sensor data should not be null.");
		assertEquals(1, result.size(), "The list should contain exactly one sensor data.");
		System.out.println(result);
		SensorDataI data = result.get(0);
		assertNotNull(data, "The sensor data should not be null.");
		assertEquals(sensorID, data.getSensorIdentifier(), "The sensor identifier should match the specified one.");
		assertEquals(25.5, data.getValue(), "The value of the sensor data should match the defined fictitious value.");
	}
}
