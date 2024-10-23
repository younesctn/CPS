package ast.bexp;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

/**
 * The class SBExp must be used to return the boolean value of a sensor, it
 * takes the sensor name to return its value through the eval(ExecutionStateI)
 * methode
 */

public class SBExp implements Ibexp, Serializable {

	private static final long serialVersionUID = -6946601082878718416L;
	private String sensorId;

	public SBExp(String sensorId) {
		this.sensorId = sensorId;
	}

	public boolean eval(ExecutionStateI es) {
		SensorDataI currentNodeSensor = es.getProcessingNode().getSensorData(sensorId);
		return (boolean) currentNodeSensor.getValue();
	}

}
