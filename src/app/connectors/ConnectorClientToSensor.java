package app.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.nodes.interfaces.RequestingCI;

/**
 * A connector class that facilitates communication between client components
 * and sensor nodes in a sensor network. It implements the {@link RequestingCI}
 * interface, allowing clients to send requests to sensors and retrieve data
 * asynchronously or synchronously.
 *
 * This class extends {@link AbstractConnector} and provides the concrete
 * implementation of executing requests either synchronously or asynchronously.
 */
public class ConnectorClientToSensor extends AbstractConnector implements RequestingCI {

	/**
	 * Executes a request synchronously and returns the result. This method forwards
	 * the execution call to the connected sensor component which processes the
	 * request and returns the result.
	 *
	 * @param request The request to be executed.
	 * @return The result of the request execution.
	 * @throws Exception if the execution process encounters any errors.
	 */
	@Override
	public QueryResultI execute(RequestI request) throws Exception {
		return ((RequestingCI) this.offering).execute(request);
	}

	/**
	 * Executes a request asynchronously. This method forwards the asynchronous
	 * execution call to the connected sensor component.
	 *
	 * @param request The request to be executed asynchronously.
	 * @throws Exception if the asynchronous execution process encounters any
	 *                   errors.
	 */
	@Override
	public void executeAsync(RequestI request) throws Exception {
		((RequestingCI) this.offering).executeAsync(request);
	}
}
