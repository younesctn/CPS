package ast.cont;

import app.models.ExecutionState;
import ast.dirs.IDirs;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

/**
 * Represents a directional continuation control expression. This class
 * implements the ICont interface for evaluating the continuation.
 */
public class DCont implements ICont, Serializable {
	private static final long serialVersionUID = 13L;

	private IDirs directions;
	private int maxSauts;

	/**
	 * Constructs a directional continuation control expression with specified
	 * directions and maximum jumps.
	 *
	 * @param directions The directions to follow.
	 * @param maxSauts   The maximum number of jumps allowed.
	 */
	public DCont(IDirs directions, int maxSauts) {
		this.directions = directions;
		this.maxSauts = maxSauts;
	}

	/**
	 * Evaluates the directional continuation control expression.
	 *
	 * @param es The execution state.
	 */
	@Override
	public void eval(ExecutionStateI es) {
		ExecutionState executionState = (ExecutionState) es;
		executionState.setContinuation(true);
		executionState.incrementHops();
		executionState.setMaxSauts(maxSauts);
		executionState.setDirectional();
		executionState.setDirections(this.directions.eval());
	}
}
