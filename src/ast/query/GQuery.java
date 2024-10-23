package ast.query;

import java.io.Serializable;
import java.util.ArrayList;

import app.models.QueryResult;
import ast.cont.ICont;
import ast.gather.IGather;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.QueryI;

/**
 * This class represents a composite query that integrates both gathering and
 * controlling behaviors within a sensor network. It combines data gathering and
 * control logic to execute complex operations based on the gathered data and
 * the current state.
 *
 * The class implements {@link QueryI} for compatibility with the sensor
 * network's request processing system and is marked as {@link Serializable} to
 * allow for serialization during network communications.
 */
public class GQuery implements QueryI, Serializable {
	private static final long serialVersionUID = 21L;
	private IGather gather;
	private ICont cont;

	/**
	 * Constructs a new GQuery with specified gathering and controlling components.
	 *
	 * @param gather The data gathering component of this query.
	 * @param cont   The control component that may modify the execution state based
	 *               on the gathered data.
	 */
	public GQuery(IGather gather, ICont cont) {
		this.gather = gather;
		this.cont = cont;
	}

	/**
	 * Evaluates this query within the context of the given execution state. This
	 * method first performs data gathering, then applies control logic, and finally
	 * integrates the results into the current execution state.
	 *
	 * @param es The current execution state of the sensor network.
	 * @return The updated query result after processing this query.
	 */
	public QueryResultI eval(ExecutionStateI es) {
		((QueryResult) es.getCurrentResult()).setGather();
		ArrayList<SensorDataI> nodes = (ArrayList<SensorDataI>) gather.eval(es);
		this.cont.eval(es);
		QueryResultI res = new QueryResult(nodes, new ArrayList<>());
		es.addToCurrentResult(res);
		return es.getCurrentResult();
	}
}
