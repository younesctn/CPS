package app.models;

import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;

/**
 * Implements the ConnectionInfoI interface to provide connection details for a
 * sensor network node. This class encapsulates the unique identifier of a node
 * along with its endpoint descriptor information, facilitating network
 * communication setup.
 */
public class ConnectionInfo implements ConnectionInfoI {

	private static final long serialVersionUID = 1L;
	private final String nodeIdentifier;
	private final EndPointDescriptorI endPointDescriptor;

	/**
	 * Constructs a new ConnectionInfo with specified node identifier and endpoint
	 * descriptor.
	 *
	 * @param nodeIdentifier     the unique identifier of the node.
	 * @param endPointDescriptor the endpoint descriptor providing communication
	 *                           details for the node.
	 */
	public ConnectionInfo(String nodeIdentifier, EndPointDescriptorI endPointDescriptor) {
		this.nodeIdentifier = nodeIdentifier;
		this.endPointDescriptor = endPointDescriptor;
	}

	/**
	 * Returns the unique identifier of the node.
	 *
	 * @return a string representing the node's unique identifier.
	 */
	@Override
	public String nodeIdentifier() {
		return this.nodeIdentifier;
	}

	/**
	 * Returns the endpoint descriptor associated with this node.
	 *
	 * This method provides access to the network communication details encapsulated
	 * by the endpoint descriptor.
	 *
	 * @return the endpoint descriptor instance associated with this node.
	 */
	@Override
	public EndPointDescriptorI endPointInfo() {
		return this.endPointDescriptor;
	}
}
