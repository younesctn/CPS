package ast.bexp;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

/**
 * the class OrBExp is part of the AST, it is used the return the logic OR
 * between two Ibexp ( can be understood as booleans)
 */
public class OrBExp implements Ibexp, Serializable {
	private static final long serialVersionUID = 6L;

	private Ibexp bexp1;
	private Ibexp bexp2;

	public OrBExp(Ibexp bexp1, Ibexp bexp2) {
		this.bexp1 = bexp1;
		this.bexp2 = bexp2;
	}

	/**
	 * Return true if both the evaluation of bexp1 or bexp2 is true, simulates the
	 * logic OR.
	 * 
	 * @param es
	 * @return
	 */
	public boolean eval(ExecutionStateI es) {
		return bexp1.eval(es) || bexp2.eval(es);
	}

}
