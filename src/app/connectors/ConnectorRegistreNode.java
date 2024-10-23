package app.connectors;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.RegistrationCI;

/**
 * A connector class that implements {@link RegistrationCI} to handle
 * registration and management of sensor nodes in a sensor network. This
 * connector provides methods to register nodes, check registration status, find
 * neighboring nodes, and unregister nodes.
 *
 * This class extends {@link AbstractConnector} and delegates method calls to
 * the registry component it is connected to, which performs the actual
 * registration and management tasks.
 */
public class ConnectorRegistreNode extends AbstractConnector implements RegistrationCI {

	/**
	 * Checks if a node is already registered in the network by its identifier.
	 *
	 * @param nodeIdentifier The unique identifier of the node to check.
	 * @return {@code true} if the node is registered, {@code false} otherwise.
	 * @throws Exception if there is an issue accessing the registration status.
	 */
	@Override
	public boolean registered(String nodeIdentifier) throws Exception {
		return ((RegistrationCI) this.offering).registered(nodeIdentifier);
	}

	/**
	 * Registers a new node in the network and identifies its potential neighbors
	 * based on specific criteria.
	 *
	 * @param nodeInfo The information about the node to be registered.
	 * @return A set of {@link NodeInfoI} representing the neighbors of the newly
	 *         registered node.
	 * @throws Exception if there is an issue registering the node.
	 */
	@Override
	public Set<NodeInfoI> register(NodeInfoI nodeInfo) throws Exception {
		return ((RegistrationCI) this.offering).register(nodeInfo);
	}

	/**
	 * Finds a new neighbor for the specified node based on a given direction.
	 *
	 * @param nodeInfo The node information for which a new neighbor is being
	 *                 sought.
	 * @param d        The direction in which to look for a neighbor.
	 * @return {@link NodeInfoI} representing the new neighbor, or {@code null} if
	 *         no neighbor is found.
	 * @throws Exception if there is an issue finding a new neighbor.
	 */
	@Override
	public NodeInfoI findNewNeighbour(NodeInfoI nodeInfo, Direction d) throws Exception {
		return ((RegistrationCI) this.offering).findNewNeighbour(nodeInfo, d);
	}

	/**
	 * Unregisters a node from the network using its identifier.
	 *
	 * @param nodeIdentifier The unique identifier of the node to be unregistered.
	 * @throws Exception if there is an issue during the unregistration process.
	 */
	@Override
	public void unregister(String nodeIdentifier) throws Exception {
		((RegistrationCI) this.offering).unregister(nodeIdentifier);
	}
}
