package app.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestContinuationI;
import fr.sorbonne_u.cps.sensor_network.network.interfaces.SensorNodeP2PCI;

/**
 * A connector that implements the {@link SensorNodeP2PCI} interface to handle
 * peer-to-peer communication between sensor nodes. This connector enables
 * sensor nodes to establish and manage direct connections, execute requests,
 * and handle asynchronous tasks among themselves, promoting effective
 * decentralized network management.
 *
 * This class extends {@link AbstractConnector} and delegates the interaction
 * between sensor nodes through well-defined peer-to-peer communication
 * interfaces.
 */
public class ConnectorSensorToSensor extends AbstractConnector implements SensorNodeP2PCI {

	/**
	 * Requests disconnection from a neighboring sensor node.
	 *
	 * @param neighbour The node information for the neighbor from which
	 *                  disconnection is requested.
	 * @throws Exception if there is an issue during the disconnection process.
	 */
	@Override
	public void ask4Disconnection(NodeInfoI neighbour) throws Exception {
		((SensorNodeP2PCI) this.offering).ask4Disconnection(neighbour);
	}

	/**
	 * Requests a connection with a new neighboring sensor node.
	 *
	 * @param newNeighbour The node information for the new neighbor with which a
	 *                     connection is sought.
	 * @throws Exception if there is an issue during the connection process.
	 */
	@Override
	public void ask4Connection(NodeInfoI newNeighbour) throws Exception {
		((SensorNodeP2PCI) this.offering).ask4Connection(newNeighbour);
	}

	/**
	 * Executes a request synchronously on another sensor node.
	 *
	 * @param request The continuation request to be executed.
	 * @return The result of the request execution as a {@link QueryResultI}.
	 * @throws Exception if there is an issue executing the request.
	 */
	@Override
	public QueryResultI execute(RequestContinuationI request) throws Exception {
		return ((SensorNodeP2PCI) this.offering).execute(request);
	}

	/**
	 * Initiates asynchronous execution of a request on another sensor node.
	 *
	 * @param requestContinuation The continuation request to be executed
	 *                            asynchronously.
	 * @throws Exception if there is an issue initiating the asynchronous execution.
	 */
	@Override
	public void executeAsync(RequestContinuationI requestContinuation) throws Exception {
		((SensorNodeP2PCI) this.offering).execute(requestContinuation);
	}
}
