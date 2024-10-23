package ast.rand;

import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

public interface IRand {
	public double eval(ExecutionStateI curentNode);
}
