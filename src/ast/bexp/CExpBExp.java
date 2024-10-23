package ast.bexp;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

import ast.cexp.Icexp;

/**
 * The class CExpBExp must be used to convert a CExp into a BExp after
 * evaluation its value.
 */
public class CExpBExp implements Ibexp, Serializable {
	private static final long serialVersionUID = 4L;

	private Icexp cexp;

	public CExpBExp(Icexp cexp) {
		this.cexp = cexp;
	}

	/**
	 * return the evaluation of a ICexp which is a boolean, must be wrapped in a
	 * IBexp.
	 * 
	 * @param es
	 * @return
	 */
	@Override
	public boolean eval(ExecutionStateI es) {
		return cexp.eval(es);
	}

}
