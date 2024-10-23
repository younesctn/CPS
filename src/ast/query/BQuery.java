package ast.query;

import java.io.Serializable;
import java.util.ArrayList;

import app.models.QueryResult;
import ast.bexp.Ibexp;
import ast.cont.ICont;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.QueryI;

/**
 * Represents a Boolean query within a sensor network. This type of query
 * evaluates a Boolean expression and applies a control mechanism based on the
 * result of the Boolean evaluation.
 *
 * The class implements {@link QueryI} for compatibility with the sensor
 * network's request processing system and is marked as {@link Serializable} to
 * facilitate its transmission over the network.
 */
public class BQuery implements QueryI, Serializable {
	private static final long serialVersionUID = 20L;
	private Ibexp bexp;
	private ICont cont;

	/**
	 * Constructs a new BQuery with specified Boolean expression and control
	 * components.
	 *
	 * @param bexp The Boolean expression component of this query.
	 * @param cont The control component that modifies the execution state based on
	 *             the outcome of the Boolean expression.
	 */
	public BQuery(Ibexp bexp, ICont cont) {
		this.bexp = bexp;
		this.cont = cont;
	}

	/**
	 * Evaluates this query within the context of the given execution state. The
	 * method performs a Boolean evaluation, applies control logic based on the
	 * evaluation result, and then updates the execution state with the outcome.
	 *
	 * @param es The current execution state of the sensor network.
	 * @return The updated query result after processing this query, highlighting
	 *         changes made to the state.
	 */
	public QueryResultI eval(ExecutionStateI es) {
		ArrayList<String> nodes = new ArrayList<>();
		boolean bool = bexp.eval(es);
		if (bool) {
			nodes.add(es.getProcessingNode().getNodeIdentifier());
		}
		this.cont.eval(es);
		QueryResult res = new QueryResult(new ArrayList<>(), nodes);
		es.addToCurrentResult(res);
		((QueryResult) es.getCurrentResult()).setBoolean();
		return es.getCurrentResult();
	}
}
