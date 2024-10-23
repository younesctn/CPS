package app.models;

import fr.sorbonne_u.cps.sensor_network.interfaces.EndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;

/**
 * Represents the information descriptor for a node within a sensor network.
 * This class implements the {@link NodeInfoI} interface, providing details such
 * as the node identifier, network endpoint information, positional data, and
 * the node's range. Additionally, it handles peer-to-peer endpoint information
 * for network communications.
 */
public class Descriptor implements NodeInfoI {

	private static final long serialVersionUID = 1L;

	private final String nodeIdentifier; // Unique identifier for the node
	private final EndPointDescriptorI endPointInfo; // Network endpoint information for standard communications
	private final PositionI nodePosition; // Geographical or logical position of the node
	private final double nodeRange; // The operational range of the node, e.g., for communication or sensor range
	private final EndPointDescriptorI p2pEndPointInfo;// Endpoint descriptor for peer-to-peer communications

	/**
	 * Constructs a Descriptor with detailed information about a network node.
	 *
	 * @param nodeIdentifier  The unique identifier of the node.
	 * @param endPointInfo    The standard endpoint descriptor for the node.
	 * @param nodePosition    The position of the node as a {@link PositionI}
	 *                        object.
	 * @param nodeRange       The communication or sensor range of the node.
	 * @param p2pEndPointInfo The endpoint descriptor for peer-to-peer
	 *                        communications.
	 */
	public Descriptor(String nodeIdentifier, EndPointDescriptorI endPointInfo, PositionI nodePosition, double nodeRange,
			EndPointDescriptorI p2pEndPointInfo) {
		this.nodeIdentifier = nodeIdentifier;
		this.endPointInfo = endPointInfo;
		this.nodePosition = nodePosition;
		this.nodeRange = nodeRange;
		this.p2pEndPointInfo = p2pEndPointInfo;
	}

	/**
	 * Retrieves the unique identifier for the node.
	 * 
	 * @return A string representing the node's unique identifier.
	 */
	@Override
	public String nodeIdentifier() {
		return nodeIdentifier;
	}

	/**
	 * Retrieves the endpoint descriptor for standard network communications.
	 * 
	 * @return An {@link EndPointDescriptorI} object containing the endpoint
	 *         information.
	 */
	@Override
	public EndPointDescriptorI endPointInfo() {
		return endPointInfo;
	}

	/**
	 * Retrieves the position of the node.
	 * 
	 * @return A {@link PositionI} object representing the node's position.
	 */
	@Override
	public PositionI nodePosition() {
		return nodePosition;
	}

	/**
	 * Retrieves the operational range of the node.
	 * 
	 * @return A double value representing the node's range.
	 */
	@Override
	public double nodeRange() {
		return nodeRange;
	}

	/**
	 * Retrieves the endpoint descriptor for peer-to-peer communications.
	 * 
	 * @return An {@link EndPointDescriptorI} object for the node's peer-to-peer
	 *         communications.
	 */
	@Override
	public EndPointDescriptorI p2pEndPointInfo() {
		return p2pEndPointInfo;
	}
}
