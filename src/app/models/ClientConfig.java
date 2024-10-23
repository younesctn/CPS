package app.models;

import java.util.List;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;

/**
 * Represents the configuration settings for a client in a sensor network. This
 * class encapsulates the configuration parameters required to setup and operate
 * a client, including connections and operational parameters.
 */
public class ClientConfig {
	private final String inboundPortRegister; // The port for registering the client
	private final String uriClock; // URI for the clock server
	private final int name; // A unique identifier for the client
	private final List<RequestI> requests; // List of requests the client will execute
	private final String requestNodeName; // Name of the node to request data from
	private final boolean isRequestAsync; // Indicates whether requests are asynchronous

	/**
	 * Constructs a new ClientConfig with specified parameters.
	 * 
	 * @param inboundPortRegister The inbound port on the registry for this client.
	 * @param uriClock            The URI of the clock server to synchronize
	 *                            time-sensitive operations.
	 * @param name                A numeric identifier for the client, used in
	 *                            logging and management.
	 * @param requests            A list of {@link RequestI} objects representing
	 *                            the requests the client will perform.
	 * @param requestNodeName     The default node from which the client will
	 *                            request data.
	 * @param isRequestAsync      Whether requests should be executed
	 *                            asynchronously.
	 */
	public ClientConfig(String inboundPortRegister, String uriClock, int name, List<RequestI> requests,
			String requestNodeName, boolean isRequestAsync) {
		this.inboundPortRegister = inboundPortRegister;
		this.uriClock = uriClock;
		this.name = name;
		this.requests = requests;
		this.requestNodeName = requestNodeName;
		this.isRequestAsync = isRequestAsync;
	}

	/**
	 * Returns the name of the request node associated with this client
	 * configuration.
	 * 
	 * @return The name of the node from which the client is configured to request
	 *         data.
	 */
	public String getRequestNodeName() {
		return requestNodeName;
	}

	/**
	 * Returns the inbound port on the registry for this client.
	 * 
	 * @return The inbound port as a string.
	 */
	public String getInboundPortRegister() {
		return inboundPortRegister;
	}

	/**
	 * Returns the URI of the clock server associated with this client.
	 * 
	 * @return The URI of the clock server as a string.
	 */
	public String getUriClock() {
		return uriClock;
	}

	/**
	 * Returns the numeric identifier of the client.
	 * 
	 * @return The client's identifier as an integer.
	 */
	public int getName() {
		return name;
	}

	/**
	 * Returns the list of requests configured for the client.
	 * 
	 * @return A list of {@link RequestI} instances, each representing a request the
	 *         client can perform.
	 */
	public List<RequestI> getRequests() {
		return requests;
	}

	/**
	 * Indicates whether the client is configured to execute requests
	 * asynchronously.
	 * 
	 * @return A boolean value indicating whether the client's requests are
	 *         asynchronous.
	 */
	public boolean getIsRequestAsync() {
		return isRequestAsync;
	}
}
