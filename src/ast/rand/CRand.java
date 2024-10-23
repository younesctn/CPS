package ast.rand;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import java.io.Serializable;

/**
 * Represents a constant random value generator within the sensor network. This
 * class provides a fixed value that simulates a random but unchanging sensor
 * measurement.
 *
 * It implements {@link IRand} for compatibility with interfaces requiring
 * random value generation and is {@link Serializable} to enable its usage
 * across distributed components.
 */
public class CRand implements IRand, Serializable {
	private static final long serialVersionUID = 22L;

	/** The constant value that this random generator provides. */
	private Double capteur;

	/**
	 * Constructs a new constant random generator with a specified value.
	 *
	 * @param capteur The constant value to be returned by this generator.
	 */
	public CRand(Double capteur) {
		this.capteur = capteur;
	}

	/**
	 * Evaluates and returns the constant value associated with this generator. This
	 * method does not use the current node's state but returns the pre-defined
	 * constant value.
	 *
	 * @param currentNode The current state of the execution node, not used in this
	 *                    implementation.
	 * @return The constant double value initially set for this generator.
	 */
	@Override
	public double eval(ExecutionStateI currentNode) {
		return this.capteur;
	}
}
