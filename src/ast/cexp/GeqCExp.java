package ast.cexp;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

import ast.rand.IRand;

public class GeqCExp implements Icexp, Serializable {
	private static final long serialVersionUID = 10L;

	private IRand rand1;
	private IRand rand2;

	public GeqCExp(IRand rand1, IRand rand2) {
		this.rand1 = rand1;
		this.rand2 = rand2;
	}

	public boolean eval(ExecutionStateI es) {
		return rand1.eval(es) >= rand2.eval(es);
	}
}
