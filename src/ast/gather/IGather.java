package ast.gather;

import java.util.List;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

public interface IGather {

	public String getSensorID();

	public List<SensorDataI> eval(ExecutionStateI es);
}
