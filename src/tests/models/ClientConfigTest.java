package tests.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import app.models.ClientConfig;

import java.util.List;
import java.util.ArrayList;

import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;

class ClientConfigTest {

    @Test
    void testClientConfig() {
        String inboundPortRegister = "inbound:1234";
        String uriClock = "uri:clock";
        int name = 42;
        List<RequestI> requests = new ArrayList<>();
        String requestNodeName = "Node1";
        boolean isRequestAsync = true;

        ClientConfig clientConfig = new ClientConfig(inboundPortRegister, uriClock, name, requests, requestNodeName, isRequestAsync);

        assertEquals(inboundPortRegister, clientConfig.getInboundPortRegister(), "The inbound port register should match the expected value");
        assertEquals(uriClock, clientConfig.getUriClock(), "The URI clock should match the expected value");
        assertEquals(name, clientConfig.getName(), "The name should match the expected value");
        assertEquals(requests, clientConfig.getRequests(), "The list of requests should match the expected value");
        assertEquals(requestNodeName, clientConfig.getRequestNodeName(), "The request node name should match the expected value");
        assertEquals(isRequestAsync, clientConfig.getIsRequestAsync(), "The isRequestAsync flag should match the expected value");
    }
}
