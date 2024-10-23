package ast.gather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

/**
 * Implements a recursive gathering strategy for sensor network data. This class
 * allows the aggregation of sensor data from a specific sensor ID and
 * recursively from other nested gather implementations.
 *
 * It serves to build complex data gathering logic that can combine data from
 * multiple sources or sensors within the network.
 */
public class RGather implements IGather, Serializable {
	private static final long serialVersionUID = 19L;

	private String sensorID;
	private IGather gather;

	/**
	 * Constructs a new recursive gather instance.
	 *
	 * @param sensorID The sensor ID for which data should be gathered initially.
	 * @param gather   Another gather instance which defines additional data
	 *                 gathering logic to be applied recursively.
	 */
	public RGather(String sensorID, IGather gather) {
		this.sensorID = sensorID;
		this.gather = gather;
	}

	/**
	 * Returns the sensor ID associated with this gather instance.
	 *
	 * @return the sensor ID used for initial data gathering.
	 */
	@Override
	public String getSensorID() {
		return this.sensorID;
	}

	/**
	 * Provides access to the nested gather instance.
	 *
	 * @return the nested gather instance used for recursive data gathering.
	 */
	public IGather getGather() {
		return this.gather;
	}

	/**
	 * Evaluates this gather instance against the provided execution state. This
	 * method gathers data for the specified sensor ID and then recursively gathers
	 * more data using the nested gather instance.
	 *
	 * @param es The execution state providing context for this evaluation,
	 *           typically including access to sensor data through the processing
	 *           node.
	 * @return a list of {@link SensorDataI} objects representing gathered data.
	 */
	@Override
	public List<SensorDataI> eval(ExecutionStateI es) {
		ArrayList<SensorDataI> v = new ArrayList<>();
		v.add(es.getProcessingNode().getSensorData(sensorID));
		v.addAll(gather.eval(es));
		return v;
	}
}
