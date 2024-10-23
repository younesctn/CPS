package ast.cont;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

import app.models.ExecutionState;

/**
 * Represents an end continuation control expression. This class implements the
 * ICont interface for evaluating the end of continuation.
 */
public class ECont implements ICont, Serializable {
	private static final long serialVersionUID = 14L;

	/**
	 * Evaluates the end continuation control expression.
	 *
	 * @param es The execution state.
	 */
	@Override
	public void eval(ExecutionStateI es) {
		((ExecutionState) es).setContinuation(false);
	}
}
