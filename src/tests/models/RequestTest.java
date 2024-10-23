package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Request;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.QueryI;

public class RequestTest {

	@SuppressWarnings("serial")
	static class TestConnectionInfo implements ConnectionInfoI {
		private String portURI;
		private String inboundPortURI;

		public TestConnectionInfo(String portURI, String inboundPortURI) {
			this.portURI = portURI;
			this.inboundPortURI = inboundPortURI;
		}

		public String getPortURI() {
			return portURI;
		}

		public String getInboundPortURI() {
			return inboundPortURI;
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

	@Test
	public void testConstructor() {

		QueryI queryCode = new TestQueryI("QueryCode1");
		ConnectionInfoI clientInfo = new TestConnectionInfo("ClientURI1", "ClientInboundURI1");
		String customURI = "CustomRequestURI";

		Request request = new Request(queryCode, clientInfo, customURI);

		assertEquals(customURI, request.requestURI(), "The custom URI should match the input");
		assertEquals(queryCode, request.getQueryCode(), "The query code should match the input");
		assertEquals(clientInfo, request.clientConnectionInfo(), "The client info should match the input");
		assertFalse(request.isAsynchronous(), "The request should be synchronous by default");
	}

	@Test
	public void testSetAsynchronous() {

		QueryI queryCode = new TestQueryI("QueryCode1");
		ConnectionInfoI clientInfo = new TestConnectionInfo("ClientURI1", "ClientInboundURI1");
		Request request = new Request(queryCode, clientInfo);

		request.setAsynchronous(true);

		assertTrue(request.isAsynchronous(), "The request should be set to asynchronous");
	}

	@Test
	public void testSetClient() {

		QueryI queryCode = new TestQueryI("QueryCode1");
		ConnectionInfoI initialClientInfo = new TestConnectionInfo("ClientURI1", "ClientInboundURI1");
		Request request = new Request(queryCode, initialClientInfo);

		ConnectionInfoI newClientInfo = new TestConnectionInfo("NewClientURI", "NewClientInboundURI");

		request.setClient(newClientInfo);

		assertEquals(newClientInfo, request.clientConnectionInfo(), "The client info should be updated");
	}
}
