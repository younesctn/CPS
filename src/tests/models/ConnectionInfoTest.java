package tests.models;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import app.models.ConnectionInfo;
import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;

public class ConnectionInfoTest {

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

    @Test
    public void testNodeIdentifier() {
        String expectedNodeIdentifier = "Node123";
        EndPointDescriptorI endPointDescriptor = new TestEndPointDescriptor("inbound:1234", "TestInterface");
        ConnectionInfo connectionInfo = new ConnectionInfo(expectedNodeIdentifier, endPointDescriptor);

        String actualNodeIdentifier = connectionInfo.nodeIdentifier();

        assertEquals("The node identifier should match the expected value", expectedNodeIdentifier, actualNodeIdentifier);
    }

    @Test
    public void testEndPointInfo() {
        String nodeIdentifier = "Node123";
        EndPointDescriptorI expectedEndPointDescriptor = new TestEndPointDescriptor("inbound:1234", "TestInterface");
        ConnectionInfo connectionInfo = new ConnectionInfo(nodeIdentifier, expectedEndPointDescriptor);

        EndPointDescriptorI actualEndPointDescriptor = connectionInfo.endPointInfo();

        assertEquals("The endpoint descriptor should match the expected value", expectedEndPointDescriptor, actualEndPointDescriptor);
    }
}

