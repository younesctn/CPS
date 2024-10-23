package tests.ast.query;

import app.models.ExecutionState;
import app.models.ProcessingNode;
import app.models.QueryResult;
import ast.bexp.Ibexp;
import ast.cont.ICont;
import ast.query.BQuery;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class BQueryTest {

	private BQuery bQuery;
	private ExecutionState es;
	private final String nodeId = "node1";

	@BeforeEach
	void setUp() {
		// Basic implementation of Ibexp
		Ibexp bexp = new Ibexp() {
			@Override
			public boolean eval(ExecutionStateI es) {
				return true; // Always simulating true for the test
			}
		};

		// Basic implementation of ICont
		ICont cont = new ICont() {
			@Override
			public void eval(ExecutionStateI es) {
				// No effect to simplify the test
			}
		};

		// Creating a simple ProcessingNode
		ProcessingNode processingNode = new ProcessingNode(nodeId, null, null, null);

		// Initializing the ExecutionState with a ProcessingNode and an empty
		// QueryResult
		es = new ExecutionState(processingNode, new QueryResult(new ArrayList<>(), new ArrayList<>()));

		// Initializing BQuery with bexp and cont
		bQuery = new BQuery(bexp, cont);
	}

	@Test
	void testEvalExecutesContAndUpdatesResult() {
		QueryResultI result = bQuery.eval(es);
		System.out.println("isBoolean: " + result.isBooleanRequest()); // Should print true if everything is working
																		// correctly

		assertNotNull(result, "The result should not be null.");
		assertTrue(result.isBooleanRequest(), "The result should be identified as a boolean request.");
		// Make sure the list of positive sensor nodes contains the correct node ID
		assertTrue(result.positiveSensorNodes().contains(nodeId), "The list should contain the node ID.");
	}

}
