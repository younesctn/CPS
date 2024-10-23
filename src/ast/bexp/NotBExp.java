package ast.bexp;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

/**
 * the class NotBExp is part of the AST, it is used the return the logic
 * NOT/NEGATION of a bexp
 */
public class NotBExp implements Ibexp, Serializable {
	private static final long serialVersionUID = 5L;

	private Ibexp bexp;

	public NotBExp(Ibexp bexp) {
		this.bexp = bexp;
	}

	/**
	 * Return true if the evaluation of bexp is false, simulates the logic
	 * NOT/NEGATION.
	 * 
	 * @param es
	 * @return
	 */
	public boolean eval(ExecutionStateI es) {
		return !bexp.eval(es);
	}

}
