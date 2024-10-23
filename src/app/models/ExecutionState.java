package app.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ProcessingNodeI;

/**
 * Represents the state of a request's execution within a sensor network. This
 * class tracks various parameters including the processing node, the query
 * results, and the request's current position and distance constraints.
 */
public class ExecutionState implements ExecutionStateI, Cloneable {
	private static final long serialVersionUID = 6009720675170853565L;
	private ProcessingNodeI pn; // The processing node where the request is being executed
	private QueryResultI queryResult; // The current query result
	private boolean directional = false; // Indicates whether the request is directional
	private boolean flooding = false; // Indicates whether the request is flooding
	private boolean isContinuation = false; // Indicates whether this state is a continuation of another request
	private Set<Direction> directions; // The set of directions for the request
	private int hops = 0; // The current hop count
	private int maxhops; // The maximum number of hops allowed
	private Double maxDistance; // The maximum distance allowed for the request
	private PositionI p; // The current position of the request

	/**
	 * Constructs an ExecutionState with the specified processing node and query
	 * result.
	 *
	 * @param pn          The processing node where the request is being executed.
	 * @param queryResult The initial query result for the request.
	 */
	public ExecutionState(ProcessingNodeI pn, QueryResultI queryResult) {
		this.pn = pn;
		this.queryResult = queryResult;
	}

	@Override
	public ProcessingNodeI getProcessingNode() {
		return this.pn;
	}

	@Override
	public void updateProcessingNode(ProcessingNodeI pn) {
		this.pn = pn;
	}

	@Override
	public QueryResultI getCurrentResult() {
		return queryResult;
	}

	@Override
	public void addToCurrentResult(QueryResultI result) {
		queryResult.gatheredSensorsValues().addAll(result.gatheredSensorsValues());
		queryResult.positiveSensorNodes().addAll(result.positiveSensorNodes());
	}

	@Override
	public boolean isDirectional() {
		return this.directional;
	}

	@Override
	public Set<Direction> getDirections() {
		return this.directions;
	}

	@Override
	public boolean noMoreHops() {
		return this.hops >= this.maxhops;
	}

	@Override
	public void incrementHops() {
		this.hops++;
	}

	@Override
	public boolean isFlooding() {
		return this.flooding;
	}

	@Override
	public boolean withinMaximalDistance(PositionI p) {
		return this.p.distance(p) <= maxDistance;
	}

	/**
	 * Sets the maximum allowed distance for the request.
	 *
	 * @param max The maximum distance allowed.
	 */
	public void setMaxDistance(double max) {
		this.maxDistance = max;
	}

	/**
	 * Enables flooding mode for the request.
	 */
	public void setFlooding() {
		this.flooding = true;
	}

	/**
	 * Enables directional mode for the request.
	 */
	public void setDirectional() {
		this.directional = true;
	}

	/**
	 * Sets the maximum allowed number of hops for the request.
	 *
	 * @param sauts The maximum number of hops allowed.
	 */
	public void setMaxSauts(int sauts) {
		this.maxhops = sauts;
	}

	/**
	 * Sets the directions for the request.
	 *
	 * @param directions The set of directions to set.
	 * @return The set of directions set.
	 */
	public Set<Direction> setDirections(Set<Direction> directions) {
		return this.directions = directions;
	}

	/**
	 * Sets the position for the request.
	 *
	 * @param p The position to set.
	 */
	public void setPosition(PositionI p) {
		this.p = p;
	}

	/**
	 * Clones the execution state.
	 *
	 * @return A clone of the execution state.
	 * @throws CloneNotSupportedException If cloning is not supported.
	 */
	@Override
	public ExecutionState clone() throws CloneNotSupportedException {
		ExecutionState cloned = (ExecutionState) super.clone();
		if (this.directions != null) {
			cloned.directions = new HashSet<>(this.directions);
		}
		cloned.queryResult = ((QueryResult) this.queryResult).clone();
		cloned.directional = this.directional;
		cloned.flooding = this.flooding;
		return cloned;
	}

	/**
	 * Retrieves the maximum number of hops allowed for the request.
	 *
	 * @return The maximum hops allowed as an integer.
	 */
	public Integer getMaxHops() {
		return this.maxhops;
	}

	/**
	 * Retrieves the current hop count of the request.
	 *
	 * @return The current hop count as an integer.
	 */
	public Integer getHops() {
		return hops;
	}

	/**
	 * Retrieves the maximum distance allowed for the request.
	 *
	 * @return The maximum distance allowed as a double.
	 */
	public Double getMaxDistance() {
		return this.maxDistance;
	}

	/**
	 * Retrieves the current position of the request.
	 *
	 * @return The current position as a {@link PositionI} object.
	 */
	public PositionI getPosition() {
		return this.p;
	}

	@Override
	public boolean isContinuationSet() {
		return this.isContinuation;
	}

	/**
	 * Sets whether this state is a continuation of another request.
	 *
	 * @param isContinuation Indicates whether this is a continuation of another
	 *                       request.
	 */
	public void setContinuation(boolean isContinuation) {
		this.isContinuation = isContinuation;
	}

	/**
	 * Constructs a string representation of the current query result.
	 *
	 * @return A string describing the current query result.
	 */
	public String buildQueryResultString() {
		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append("request result :");

		if (queryResult.isBooleanRequest()) {
			resultBuilder.append(queryResult.positiveSensorNodes().toString());
		} else if (queryResult.isGatherRequest()) {
			resultBuilder.append(queryResult.gatheredSensorsValues().toString());
		}

		return resultBuilder.toString();
	}

	/**
	 * Resets the query result to an empty state.
	 */
	public void resetQuery() {
		this.queryResult = new QueryResult(new ArrayList<>(), new ArrayList<>());
	}
}
