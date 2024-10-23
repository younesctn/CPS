package ast.dirs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;

/**
 * Represents a fixed direction component in a sensor network. This class
 * encapsulates a single direction and provides functionality to retrieve this
 * direction as a set, allowing it to be used in contexts where directional
 * evaluations are required.
 *
 * It implements {@link IDirs} for compatibility with interfaces requiring
 * direction operations and is marked as {@link Serializable} to enable its
 * usage across distributed components.
 */
public class Fdirs implements IDirs, Serializable {
	private static final long serialVersionUID = 16L;
	private Direction direction;

	/**
	 * Constructs a new Fdirs instance with a specified direction.
	 *
	 * @param direction The fixed direction that this instance will represent.
	 */
	public Fdirs(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Evaluates and returns the set containing the single direction associated with
	 * this instance. This method is useful for operations that require direction
	 * information to be presented as a set, such as filtering or matching based on
	 * directional criteria.
	 *
	 * @return A set containing the single direction held by this instance.
	 */
	public Set<Direction> eval() {
		Set<Direction> res = new HashSet<>();
		res.add(this.direction);
		return res;
	}
}
