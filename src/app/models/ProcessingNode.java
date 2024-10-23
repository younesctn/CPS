package app.models;

import java.io.Serializable;
import java.util.Set;

import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ProcessingNodeI;

/**
 * Represents a processing node within a sensor network, managing sensor data
 * and interactions with neighboring nodes. This class encapsulates
 * functionality for handling and responding to data requests, including the
 * propagation of such requests to neighboring nodes.
 */
public class ProcessingNode implements ProcessingNodeI, Serializable {

	private static final long serialVersionUID = 4884492601373245328L;
	private String nodeIdentifier;
	private PositionI position;
	private Set<NodeInfoI> neighbours;
	private Set<SensorDataI> sensorData;

	/**
	 * Constructs a ProcessingNode with specific properties.
	 *
	 * @param nodeIdentifier Unique identifier for the processing node.
	 * @param position       Geographical or logical position of the node within the
	 *                       network.
	 * @param neighbours     A set of neighboring nodes represented as
	 *                       {@link NodeInfoI}.
	 * @param sensorData     A set of sensor data points associated with this node.
	 */
	public ProcessingNode(String nodeIdentifier, PositionI position, Set<NodeInfoI> neighbours,
			Set<SensorDataI> sensorData) {
		this.nodeIdentifier = nodeIdentifier;
		this.position = position;
		this.neighbours = neighbours;
		this.sensorData = sensorData;
	}

	/**
	 * Retrieves the unique identifier of this processing node.
	 * 
	 * @return The node's unique identifier as a string.
	 */
	@Override
	public String getNodeIdentifier() {
		return nodeIdentifier;
	}

	/**
	 * Retrieves the position of the processing node.
	 * 
	 * @return The position as {@link PositionI}.
	 */
	@Override
	public PositionI getPosition() {
		return position;
	}

	/**
	 * Retrieves a set of neighboring nodes.
	 * 
	 * @return A set of nodes information as {@link NodeInfoI}.
	 */
	@Override
	public Set<NodeInfoI> getNeighbours() {
		return neighbours;
	}

	/**
	 * Retrieves specific sensor data by its identifier.
	 * 
	 * @param sensorIdentifier The unique identifier of the sensor data to retrieve.
	 * @return The sensor data as {@link SensorDataI}, or null if not found.
	 */
	@Override
	public SensorDataI getSensorData(String sensorIdentifier) {
		for (SensorDataI sensorData : this.sensorData) {
			if (sensorData.getSensorIdentifier().equals(sensorIdentifier)) {
				return sensorData;
			}
		}
		return null;
	}
}
