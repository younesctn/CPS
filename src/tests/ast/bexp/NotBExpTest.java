package tests.ast.bexp;

import app.models.ExecutionState;
import app.models.Position;
import app.models.ProcessingNode;
import app.models.SensorData;
import ast.bexp.NotBExp;
import ast.bexp.SBExp;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NotBExpTest {

	@Test
	public void testNotEvaluation() {
		// Create SensorData instances
		SensorData sensorTrue = new SensorData("sensor1", "type1", true);
		SensorData sensorFalse = new SensorData("sensor2", "type2", false);

		// Create a set of SensorData for the ProcessingNode
		Set<SensorDataI> sensorDataISet = new HashSet<>();
		sensorDataISet.add(sensorTrue);
		sensorDataISet.add(sensorFalse);

		// Create ProcessingNode
		ProcessingNode processingNode = new ProcessingNode("nodeTest", new Position(2.0, 3.0), null, sensorDataISet);

		// Create ExecutionState
		ExecutionState es = new ExecutionState(processingNode, null);

		// Create SBExp instances for true and false scenarios
		SBExp trueExp = new SBExp("type1");
		SBExp falseExp = new SBExp("type2");

		// Test NOT of true (should be false)
		NotBExp notTrue = new NotBExp(trueExp);
		assertFalse(notTrue.eval(es), "NOT expression should be false when internal expression is true.");

		// Test NOT of false (should be true)
		NotBExp notFalse = new NotBExp(falseExp);
		assertTrue(notFalse.eval(es), "NOT expression should be true when internal expression is false.");
	}
}
