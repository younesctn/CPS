package tests.ast.gather;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import app.models.ExecutionState;
import app.models.ProcessingNode;
import app.models.SensorData;
import ast.gather.FGather;
import ast.gather.IGather;
import ast.gather.RGather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class RGatherTest {

	private RGather rGather;
	private ExecutionStateI es;
	private final String sensorID = "sensor123";
	private IGather simpleGather;

	@BeforeEach
	void setUp() {
		// Creating a ProcessingNode with sensor data
		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(new SensorData(sensorID, "Temperature", 25.5)); // Fictitious value for testing purposes

		ProcessingNode processingNode = new ProcessingNode("node1", null, new HashSet<>(), sensorDataSet);

		// Configuring ExecutionState to use our ProcessingNode
		es = new ExecutionState(processingNode, null);

		// Using a simple instance of FGather as the gather
		simpleGather = new FGather("sensor456"); // Make sure sensor456 is a valid ID in your ProcessingNode

		// Initializing RGather with simpleGather and sensorID
		rGather = new RGather(sensorID, simpleGather);
	}

	@Test
	void testEvalCombinesResultsCorrectly() {
		// Executing eval to retrieve combined sensor data
		List<SensorDataI> result = rGather.eval(es);

		// Verifying that the result contains the correct sensor data
		assertNotNull(result, "The list of sensor data should not be null.");
		assertEquals(2, result.size(), "The list should contain exactly two sensor data.");
	}
}
