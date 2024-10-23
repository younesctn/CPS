package ast.base;

import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import java.io.Serializable;

/**
 * Represents a base node in an abstract syntax tree (AST) for sensor network
 * simulations. This class stores the position of a node and provides a method
 * to evaluate and retrieve this position based on the execution state,
 * typically used in simulations to manage and query node positions dynamically.
 *
 * Implements {@link Serializable} to allow for object serialization as nodes
 * might need to be transmitted or stored in a distributed environment.
 */
public class ABase implements IBase, Serializable {

	private static final long serialVersionUID = 1L;

	private PositionI position;

	/**
	 * Constructs a new ABase instance with a specified position.
	 *
	 * @param position the position of this base node within the sensor network.
	 *                 Must implement {@link PositionI}, providing methods to
	 *                 manipulate and retrieve location data.
	 */
	public ABase(PositionI position) {
		this.position = position;
	}

	/**
	 * Evaluates this node based on the provided execution state and returns its
	 * position. In the current implementation, this simply returns the stored
	 * position without any modifications, but it can be extended to include dynamic
	 * behavior based on the execution state.
	 *
	 * @param es the execution state against which the node is evaluated, providing
	 *           context that could influence the node's behavior or properties.
	 * @return the {@link PositionI} of this node, representing its location in the
	 *         sensor network.
	 */
	public PositionI eval(ExecutionStateI es) {
		return this.position;
	}
}
