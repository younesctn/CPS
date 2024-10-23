package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import app.models.Request;
import app.models.RequestContinuation;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestContinuationI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ProcessingNodeI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.QueryI;

public class RequestContinuationTest {

	@SuppressWarnings("serial")
	static class TestExecutionState implements ExecutionStateI {
		private String state;

		public TestExecutionState(String state) {
			this.state = state;
		}

		public String getExecutionState() {
			return state;
		}

		@Override
		public ProcessingNodeI getProcessingNode() {

			return null;
		}

		@Override
		public void updateProcessingNode(ProcessingNodeI pn) {

		}

		@Override
		public QueryResultI getCurrentResult() {

			return null;
		}

		@Override
		public void addToCurrentResult(QueryResultI result) {

		}

		@Override
		public boolean isContinuationSet() {

			return false;
		}

		@Override
		public boolean isDirectional() {

			return false;
		}

		@Override
		public Set<Direction> getDirections() {

			return null;
		}

		@Override
		public boolean noMoreHops() {

			return false;
		}

		@Override
		public void incrementHops() {

		}

		@Override
		public boolean isFlooding() {

			return false;
		}

		@Override
		public boolean withinMaximalDistance(PositionI p) {

			return false;
		}
	}

	@SuppressWarnings("serial")
	static class TestQueryI implements QueryI {
		private String code;

		public TestQueryI(String code) {
			this.code = code;
		}

		public String getQueryCode() {
			return code;
		}
	}

	@SuppressWarnings("serial")
	class TestConnectionInfo implements ConnectionInfoI {
		private String clientURI;
		private String clientInboundPortURI;

		public TestConnectionInfo(String clientURI, String clientInboundPortURI) {
			this.clientURI = clientURI;
			this.clientInboundPortURI = clientInboundPortURI;
		}

		public String getClientURI() {
			return clientURI;
		}

		public String getClientInboundPortURI() {
			return clientInboundPortURI;
		}

		@Override
		public String nodeIdentifier() {

			return null;
		}

		@Override
		public EndPointDescriptorI endPointInfo() {

			return null;
		}
	}

	@Test
	public void testConstructorFromRequestI() {

		QueryI queryCode = new TestQueryI("QueryCode1");
		ConnectionInfoI clientInfo = new TestConnectionInfo("ClientURI1", "ClientInboundURI1");
		RequestI request = new Request(queryCode, clientInfo);
		ExecutionStateI executionState = new TestExecutionState("State1");
		String uri = "CustomURI1";

		RequestContinuation requestContinuation = new RequestContinuation(request, executionState, uri);

		assertEquals(uri, requestContinuation.requestURI(), "The custom URI should match the input");
		assertEquals(queryCode, requestContinuation.getQueryCode(), "The query code should match the input");
		assertEquals(clientInfo, requestContinuation.clientConnectionInfo(), "The client info should match the input");
		assertEquals(executionState, requestContinuation.getExecutionState(),
				"The execution state should match the input");
		assertTrue(requestContinuation.isLeaf(), "The request should initially be a leaf");
	}

	@Test
	public void testConstructorFromRequestContinuationI() {

		QueryI queryCode = new TestQueryI("QueryCode1");
		ConnectionInfoI clientInfo = new TestConnectionInfo("ClientURI1", "ClientInboundURI1");
		ExecutionStateI initialState = new TestExecutionState("InitialState");
		RequestContinuationI initialRequestContinuation = new RequestContinuation(new Request(queryCode, clientInfo),
				initialState, "InitialURI");

		ExecutionStateI newState = new TestExecutionState("NewState");
		String newUri = "NewURI";

		RequestContinuation requestContinuation = new RequestContinuation(initialRequestContinuation, newState, newUri);

		assertEquals(newUri, requestContinuation.requestURI(), "The new URI should match the input");
		assertEquals(queryCode, requestContinuation.getQueryCode(), "The query code should match the input");
		assertEquals(clientInfo, requestContinuation.clientConnectionInfo(), "The client info should match the input");
		assertEquals(newState, requestContinuation.getExecutionState(), "The execution state should match the input");
		assertTrue(requestContinuation.isLeaf(), "The request should initially be a leaf");
	}

	@Test
	public void testIsLeafAndSetLeaf() {

		QueryI queryCode = new TestQueryI("QueryCode1");
		ConnectionInfoI clientInfo = new TestConnectionInfo("ClientURI1", "ClientInboundURI1");
		ExecutionStateI executionState = new TestExecutionState("State1");
		RequestContinuation requestContinuation = new RequestContinuation(new Request(queryCode, clientInfo),
				executionState, "CustomURI");

		assertTrue(requestContinuation.isLeaf(), "The request should initially be a leaf");

		requestContinuation.setLeaf();

		assertFalse(requestContinuation.isLeaf(), "The request should no longer be a leaf after setting leaf");
	}
}
