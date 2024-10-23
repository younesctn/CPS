package ast.gather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

/**
 * Implements a basic gathering strategy for sensor network data. This class
 * allows for straightforward data gathering from a specific sensor ID,
 * collecting a single type of sensor data from the execution state's processing
 * node.
 *
 * It simplifies the gathering process where only one piece of data is needed
 * without further aggregation or recursive gathering logic.
 */
public class FGather implements IGather, Serializable {
	private static final long serialVersionUID = 18L;
	private String sensorID;

	/**
	 * Constructs a new FGather instance for a specific sensor ID.
	 *
	 * @param sensorID The sensor ID for which data should be gathered.
	 */
	public FGather(String sensorID) {
		this.sensorID = sensorID;
	}

	/**
	 * Returns the sensor ID associated with this gather instance.
	 *
	 * @return the sensor ID used for data gathering.
	 */
	@Override
	public String getSensorID() {
		return this.sensorID;
	}

	/**
	 * Evaluates this gather instance against the provided execution state. This
	 * method retrieves sensor data for the specified sensor ID from the processing
	 * node defined in the execution state.
	 *
	 * @param es The execution state providing context for this evaluation, which
	 *           includes access to sensor data through the processing node.
	 * @return a list of {@link SensorDataI} objects representing the gathered data.
	 */
	@Override
	public List<SensorDataI> eval(ExecutionStateI es) {
		ArrayList<SensorDataI> v = new ArrayList<>();
		v.add(es.getProcessingNode().getSensorData(this.sensorID));
		return v;
	}
}
