package tests.models;

import static org.junit.Assert.*;
import org.junit.Test;

import app.models.Descriptor;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;

public class DescriptorTest {

	@SuppressWarnings("serial")
	class TestEndPointDescriptor implements EndPointDescriptorI {
		private final String uri;
		private final String interfaceType;

		TestEndPointDescriptor(String uri, String interfaceType) {
			this.uri = uri;
			this.interfaceType = interfaceType;
		}

		public String getInboundPortURI() {
			return uri;
		}

		public String getInterfaceType() {
			return interfaceType;
		}
	}

	@SuppressWarnings("serial")
	class TestPosition implements PositionI {
		private final double x;
		private final double y;

		TestPosition(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		@Override
		public double distance(PositionI p) {
			return 0;
		}

		@Override
		public Direction directionFrom(PositionI p) {
			return null;
		}

		@Override
		public boolean northOf(PositionI p) {
			return false;
		}

		@Override
		public boolean southOf(PositionI p) {
			return false;
		}

		@Override
		public boolean eastOf(PositionI p) {
			return false;
		}

		@Override
		public boolean westOf(PositionI p) {
			return false;
		}
	}

	@Test
	public void testNodeIdentifier() {
		String expectedNodeIdentifier = "Node123";
		EndPointDescriptorI endPointInfo = new TestEndPointDescriptor("uri:endpoint", "type:endpoint");
		PositionI nodePosition = new TestPosition(10.0, 20.0);
		double nodeRange = 15.0;
		EndPointDescriptorI p2pEndPointInfo = new TestEndPointDescriptor("uri:p2p", "type:p2p");
		Descriptor descriptor = new Descriptor(expectedNodeIdentifier, endPointInfo, nodePosition, nodeRange,
				p2pEndPointInfo);

		String actualNodeIdentifier = descriptor.nodeIdentifier();

		assertEquals("The node identifier should match the expected value", expectedNodeIdentifier,
				actualNodeIdentifier);
	}

	@Test
	public void testEndPointInfo() {

		EndPointDescriptorI expectedEndPointInfo = new TestEndPointDescriptor("uri:endpoint", "type:endpoint");
		Descriptor descriptor = new Descriptor("Node123", expectedEndPointInfo, new TestPosition(10.0, 20.0), 15.0,
				new TestEndPointDescriptor("uri:p2p", "type:p2p"));

		EndPointDescriptorI actualEndPointInfo = descriptor.endPointInfo();

		assertEquals("The endpoint descriptor should match the expected value", expectedEndPointInfo,
				actualEndPointInfo);
	}

	@Test
	public void testNodePosition() {

		PositionI expectedPosition = new TestPosition(10.0, 20.0);
		Descriptor descriptor = new Descriptor("Node123", new TestEndPointDescriptor("uri:endpoint", "type:endpoint"),
				expectedPosition, 15.0, new TestEndPointDescriptor("uri:p2p", "type:p2p"));

		PositionI actualPosition = descriptor.nodePosition();

		assertEquals("The node position should match the expected value", ((TestPosition) expectedPosition).getX(),
				((TestPosition) actualPosition).getX(), 0.0);
		assertEquals("The node position should match the expected value", ((TestPosition) expectedPosition).getY(),
				((TestPosition) actualPosition).getY(), 0.0);
	}

	@Test
	public void testNodeRange() {

		double expectedNodeRange = 15.0;
		Descriptor descriptor = new Descriptor("Node123", new TestEndPointDescriptor("uri:endpoint", "type:endpoint"),
				new TestPosition(10.0, 20.0), expectedNodeRange, new TestEndPointDescriptor("uri:p2p", "type:p2p"));

		double actualNodeRange = descriptor.nodeRange();

		assertEquals("The node range should match the expected value", expectedNodeRange, actualNodeRange, 0.0);
	}

	@Test
	public void testP2PEndPointInfo() {

		EndPointDescriptorI expectedP2PEndPointInfo = new TestEndPointDescriptor("uri:p2p", "type:p2p");
		Descriptor descriptor = new Descriptor("Node123", new TestEndPointDescriptor("uri:endpoint", "type:endpoint"),
				new TestPosition(10.0, 20.0), 15.0, expectedP2PEndPointInfo);

		EndPointDescriptorI actualP2PEndPointInfo = descriptor.p2pEndPointInfo();

		assertEquals("The P2P endpoint descriptor should match the expected value", expectedP2PEndPointInfo,
				actualP2PEndPointInfo);
	}
}
