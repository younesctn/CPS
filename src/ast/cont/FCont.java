package ast.cont;

import app.models.ExecutionState;
import ast.base.IBase;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

/**
 * Represents a flooding continuation control expression. This class implements
 * the ICont interface for evaluating the continuation.
 */
public class FCont implements ICont, Serializable {
	private static final long serialVersionUID = 15L;

	private IBase base;
	private double distanceMax;

	/**
	 * Constructs a flooding continuation control expression with the specified base
	 * and maximum distance.
	 *
	 * @param base        The base to evaluate.
	 * @param distanceMax The maximum distance for flooding.
	 */
	public FCont(IBase base, double distanceMax) {
		this.base = base;
		this.distanceMax = distanceMax;
	}

	/**
	 * Gets the base for evaluation.
	 *
	 * @return The base.
	 */
	public IBase getBase() {
		return this.base;
	}

	/**
	 * Gets the maximum distance for flooding.
	 *
	 * @return The maximum distance.
	 */
	public double getDistanceMax() {
		return this.distanceMax;
	}

	/**
	 * Evaluates the flooding continuation control expression.
	 *
	 * @param es The execution state.
	 */
	@Override
	public void eval(ExecutionStateI es) {
		ExecutionState executionState = (ExecutionState) es;
		executionState.setContinuation(true);
		executionState.setMaxDistance(distanceMax);
		PositionI p = base.eval(es);
		executionState.setPosition(p);
		executionState.setFlooding();
	}
}
