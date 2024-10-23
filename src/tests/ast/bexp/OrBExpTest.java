package tests.ast.bexp;

import app.models.ExecutionState;
import app.models.Position;
import app.models.ProcessingNode;
import app.models.SensorData;
import ast.bexp.OrBExp;
import ast.bexp.SBExp;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrBExpTest {

	@Test
	public void testOrEvaluation() {
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

		// Test OR scenarios
		OrBExp orTrueTrue = new OrBExp(trueExp, trueExp);
		assertTrue(orTrueTrue.eval(es), "OR expression should be true when both expressions are true.");

		OrBExp orTrueFalse = new OrBExp(trueExp, falseExp);
		assertTrue(orTrueFalse.eval(es), "OR expression should be true when one of the expressions is true.");

		OrBExp orFalseTrue = new OrBExp(falseExp, trueExp);
		assertTrue(orFalseTrue.eval(es), "OR expression should be true when one of the expressions is true.");

		OrBExp orFalseFalse = new OrBExp(falseExp, falseExp);
		assertFalse(orFalseFalse.eval(es), "OR expression should be false when both expressions are false.");
	}
}
