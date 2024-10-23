package ast.cexp;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

public interface Icexp {
	public boolean eval(ExecutionStateI es);
}
