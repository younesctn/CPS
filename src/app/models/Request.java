package app.models;

import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.QueryI;

/**
 * Represents a request in a sensor network, encapsulating the details needed to
 * perform a query, including the client's connection info and the query code
 * itself. This class conforms to the {@link RequestI} interface, defining the
 * structure for requests in the sensor network system.
 */
public class Request implements RequestI {
	private static final long serialVersionUID = 1L;
	private final String uri; // Unique URI for this request, automatically generated
	private ConnectionInfoI client; // Connection information for the client issuing the request
	private QueryI queryCode; // The query code associated with this request
	private boolean isAsynchronous = false; // Indicates whether the request should be processed asynchronously
											// (initially false, meaning synchronous)

	/**
	 * Constructs a Request with specified query code and client connection
	 * information. Automatically assigns a unique URI to each request using
	 * {@link AbstractPort#generatePortURI()}.
	 *
	 * @param queryCode The {@link QueryI} code that specifies what operation is to
	 *                  be performed.
	 * @param client    The {@link ConnectionInfoI} providing details about the
	 *                  client's connection.
	 */
	public Request(QueryI queryCode, ConnectionInfoI client) {
		this.queryCode = queryCode;
		this.client = client;
		this.uri = AbstractPort.generatePortURI();
	}

	/**
	 * Constructs a Request with specified query code, client connection
	 * information, and URI. This constructor allows for a custom URI to be set for
	 * the request.
	 *
	 * @param queryCode The {@link QueryI} code that specifies what operation is to
	 *                  be performed.
	 * @param client    The {@link ConnectionInfoI} providing details about the
	 *                  client's connection.
	 * @param uri       The custom URI to set for the request.
	 */
	public Request(QueryI queryCode, ConnectionInfoI client, String uri) {
		this.queryCode = queryCode;
		this.client = client;
		this.uri = uri;
	}

	/**
	 * Retrieves the unique URI for this request.
	 *
	 * @return A string representing the unique URI of the request.
	 */
	@Override
	public String requestURI() {
		return uri;
	}

	/**
	 * Retrieves the query code associated with this request.
	 *
	 * @return The {@link QueryI} instance defining the operation of this request.
	 */
	@Override
	public QueryI getQueryCode() {
		return queryCode;
	}

	/**
	 * Indicates whether the request is to be processed asynchronously. By default,
	 * this implementation returns false, indicating synchronous processing.
	 *
	 * @return A boolean value, indicating whether the request supports asynchronous
	 *         processing.
	 */
	@Override
	public boolean isAsynchronous() {
		return isAsynchronous;
	}

	/**
	 * Sets the asynchronous processing mode for this request.
	 *
	 * @param isAsynchronous True to enable asynchronous processing, false for
	 *                       synchronous processing.
	 */
	public void setAsynchronous(boolean isAsynchronous) {
		this.isAsynchronous = isAsynchronous;
	}

	/**
	 * Retrieves the client connection information associated with this request.
	 *
	 * @return The {@link ConnectionInfoI} containing details about how the client
	 *         is connected.
	 */
	@Override
	public ConnectionInfoI clientConnectionInfo() {
		return client;
	}

	/**
	 * Sets the client connection information for this request.
	 *
	 * @param client The {@link ConnectionInfoI} containing details about the
	 *               client's connection.
	 */
	public void setClient(ConnectionInfoI client) {
		this.client = client;
	}
}
