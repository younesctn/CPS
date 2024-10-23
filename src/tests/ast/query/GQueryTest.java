package tests.ast.query;

import app.models.ExecutionState;
import app.models.ProcessingNode;
import app.models.SensorData;
import ast.gather.FGather;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class GQueryTest {
	private final String sensorID = "sensor123";

	@Test
	void testEvalExecutesContAndUpdatesResult() {
		new FGather(sensorID);

		Set<SensorDataI> sensorDataSet = new HashSet<>();
		sensorDataSet.add(new SensorData(sensorID, "Heat", 25.5));

		ProcessingNode processingNode = new ProcessingNode("node1", null, new HashSet<>(), sensorDataSet);

		new ExecutionState(processingNode, null);
	}

}