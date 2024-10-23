package app.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestResultCI;

/**
 * A connector class that implements {@link RequestResultCI} to handle the
 * delivery of query results from sensor nodes to client components. This
 * connector ensures that results of sensor queries are properly communicated
 * back to the requesting clients in a distributed sensor network environment.
 *
 * This class extends {@link AbstractConnector} and provides the necessary
 * implementation to forward the results received from sensor components to
 * client components.
 */
public class ConnectorSensorToClient extends AbstractConnector implements RequestResultCI {

	/**
	 * Receives and forwards the result of a sensor query from a sensor component to
	 * the appropriate client component. This method ensures that the result is
	 * relayed accurately and promptly, maintaining the integrity of data flow in
	 * sensor network operations.
	 *
	 * @param requestURI The unique identifier of the request for which the result
	 *                   is provided.
	 * @param result     The result of the query as a {@link QueryResultI} object.
	 * @throws Exception if there is an issue in handling or forwarding the query
	 *                   result.
	 */
	@Override
	public void acceptRequestResult(String requestURI, QueryResultI result) throws Exception {
		((RequestResultCI) this.offering).acceptRequestResult(requestURI, result);
	}
}
