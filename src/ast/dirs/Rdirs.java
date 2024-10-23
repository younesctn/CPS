package ast.dirs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;

/**
 * Represents a recursive direction component in a sensor network. This class
 * encapsulates a direction and allows for recursive aggregation of directions
 * from other direction components, facilitating the composition of multiple
 * directional evaluations into a single unified set.
 *
 * It implements {@link IDirs} to comply with the system's requirements for
 * handling directional data and is marked as {@link Serializable} for
 * distribution across different components in a network.
 */
public class Rdirs implements IDirs, Serializable {
	private static final long serialVersionUID = 17L;

	private Direction direction;
	private IDirs dirs;

	/**
	 * Constructs a new Rdirs instance with a specified direction and a nested
	 * direction component.
	 *
	 * @param direction The fixed direction that this instance will primarily
	 *                  represent.
	 * @param dirs      Another direction component which this instance will use to
	 *                  aggregate additional directions.
	 */
	public Rdirs(Direction direction, IDirs dirs) {
		this.direction = direction;
		this.dirs = dirs;
	}

	/**
	 * Evaluates and returns a set containing both the primary direction of this
	 * instance and any directions derived from the nested direction component. This
	 * method facilitates complex directional evaluations where multiple sources of
	 * directional data need to be considered and unified.
	 *
	 * @return A set of {@link Direction} objects representing the aggregated
	 *         directions from this instance and its nested component.
	 */
	public Set<Direction> eval() {
		Set<Direction> res = new HashSet<>();
		res.add(direction);
		res.addAll(dirs.eval());
		return res;
	}
}
