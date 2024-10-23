package app.components;

import app.components.Sensor;
import app.connectors.ConnectorRegistreNode;
import app.connectors.ConnectorSensorToClient;
import app.connectors.ConnectorSensorToSensor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import app.gui.GraphicalNetworkInterface;
import app.models.Bcm4javaEndPointDescriptor;
import app.models.Descriptor;
import app.models.ExecutionState;
import app.models.ProcessingNode;
import app.models.QueryResult;
import app.models.RequestContinuation;
import app.models.SensorConfig;
import app.ports.URINodeInboundPortForClient;
import app.ports.URINodeInboundPortForNode;
import app.ports.URINodeOutBoundPortToNode;
import app.ports.URINodeOutBoundPortToRegister;
import app.ports.URINodeOutboundPortToClient;
import ast.query.BQuery;
import ast.query.GQuery;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.helpers.TracerI;
import fr.sorbonne_u.cps.sensor_network.interfaces.BCM4JavaEndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestContinuationI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestResultCI;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.network.interfaces.SensorNodeP2PCI;
import fr.sorbonne_u.cps.sensor_network.network.interfaces.SensorNodeP2PImplI;
import fr.sorbonne_u.cps.sensor_network.nodes.interfaces.RequestingCI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.RegistrationCI;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;

// -----------------------------------------------------------------------------
/**
 * The {@code Sensor} class represents a network sensor node within a
 * distributed sensor network. It handles tasks such as registering with a
 * registry, managing connections to other nodes, and processing sensor data
 * queries.
 * <p>
 * This class offers interfaces to handle sensor data requests and node
 * connections, and requires interfaces to connect to other nodes, the
 * registration service, and the clock server. The sensor node operates within
 * an accelerated time context provided by a clock server and interacts with
 * other nodes in the network to perform distributed queries and data
 * aggregation.
 * </p>
 *
 * @author Malek Bouzarkouna, Younes Chetouani, Mohamed Amine Zemali
 * @version 1.0
 * @see fr.sorbonne_u.components.AbstractComponent
 * @see fr.sorbonne_u.cps.sensor_network.network.interfaces.SensorNodeP2PImplI
 */

@OfferedInterfaces(offered = { RequestingCI.class, SensorNodeP2PCI.class })
@RequiredInterfaces(required = { SensorNodeP2PCI.class, RegistrationCI.class, ClocksServerCI.class,
		RequestResultCI.class })
public class Sensor extends AbstractComponent implements SensorNodeP2PImplI {

	// ------------------------------------------------------------------------
	// Instance variables
	// ------------------------------------------------------------------------

	protected int executorServiceIndex;
	protected ReentrantReadWriteLock Lock;
	private EnumMap<Direction, ReentrantReadWriteLock> portLocks = new EnumMap<>(Direction.class);

	private final URINodeInboundPortForNode inboundPortSensor;
	private final URINodeInboundPortForClient inboundPortClient;
	public static final String POOL_URI_REQUEST = "pool-uri-request";
	public static final String POOL_URI_NEIGHBOURS = "pool-uri-neighbours";
	private URINodeOutBoundPortToRegister outboundPortRegistre;
	private final String inboundPortRegister;

	private URINodeOutBoundPortToNode outboundPortNE;
	private URINodeOutBoundPortToNode outboundPortNW;
	private URINodeOutBoundPortToNode outboundPortSE;
	private URINodeOutBoundPortToNode outboundPortSW;

	private Set<NodeInfoI> neighbors = new HashSet<>();
	private Set<SensorDataI> sensors = new HashSet<>();
	private ConcurrentMap<String, NodeInfoI> nodeOutboundPorts = new ConcurrentHashMap<>();
	private Set<String> processedRequests = new HashSet<>();
	private NodeInfoI descriptor;

	private String TEST_CLOCK_URI;
	private ClocksServerOutboundPort outboundPortClock;

	private URINodeOutboundPortToClient outBoundPortClient;

	private GraphicalNetworkInterface gui;
	private int nbthread;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	protected Sensor(SensorConfig config) throws Exception {
		super(config.getNbthread(), 1);
		this.nbthread = config.getNbthread();
		this.gui = config.getGui();
		this.TEST_CLOCK_URI = config.getUriClock();
		this.sensors = config.getSensors();

		assert !this.validExecutorServiceURI(POOL_URI_NEIGHBOURS);
	
		int desiredThreads = 4;

		int threadsToAllocate = Math.min(nbthread, desiredThreads);
		this.executorServiceIndex = this.createNewExecutorService(POOL_URI_NEIGHBOURS, threadsToAllocate, false);
		this.inboundPortSensor = new URINodeInboundPortForNode(this, POOL_URI_NEIGHBOURS);
		this.inboundPortSensor.publishPort();
		assert this.validExecutorServiceIndex(this.executorServiceIndex);

		assert !this.validExecutorServiceURI(POOL_URI_REQUEST);
		
		// We subtract desiredThreads to let some for the execution of the neighbours
		int adjustedThreadCount = Math.max(nbthread - desiredThreads, 1);
		this.executorServiceIndex = this.createNewExecutorService(POOL_URI_REQUEST, adjustedThreadCount, false);
		this.inboundPortClient = new URINodeInboundPortForClient(this, POOL_URI_REQUEST);
		this.inboundPortClient.publishPort();
		assert this.validExecutorServiceIndex(this.executorServiceIndex);

		BCM4JavaEndPointDescriptorI urinodeSensor = new Bcm4javaEndPointDescriptor(inboundPortSensor.getPortURI());
		BCM4JavaEndPointDescriptorI urinodeclient = new Bcm4javaEndPointDescriptor(inboundPortClient.getPortURI());
		this.descriptor = new Descriptor("n" + config.getName(), urinodeclient, config.getPosition(), config.getRange(),
				urinodeSensor);

		this.inboundPortRegister = config.getInboundPortRegister();
		initializeOutboundPorts();

		this.processedRequests = new HashSet<>();
		if (gui != null) {
			gui.addGraphicalNode("n" + config.getName(), config.getPosition().getx(), config.getPosition().gety());
		}
		TracerI tracer = this.getTracer();
		int index = config.getName() - 1;
		tracer.setRelativePosition(index % 3, (index / 3) + 1);
		tracer.setTitle("Node n°" + config.getName());

		for (Direction dir : Direction.values()) {
			portLocks.put(dir, new ReentrantReadWriteLock()); // We associate each direction with a mutex
		}

		assert this.validExecutorServiceURI(POOL_URI_NEIGHBOURS);
		assert this.validExecutorServiceURI(POOL_URI_REQUEST);

	}

	// ------------------------------------------------------------------------
	// Component life-cycle
	// ------------------------------------------------------------------------

	@Override
	public synchronized void start() throws ComponentStartException {
		this.Lock = new ReentrantReadWriteLock();

		// ---------------------------------------------------------------------
		// Connection phase
		// ---------------------------------------------------------------------

		// do the connection with the clock
		try {
			this.doPortConnection(outboundPortClock.getPortURI(), ClocksServer.STANDARD_INBOUNDPORT_URI,
					ClocksServerConnector.class.getCanonicalName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// do the connection with the register
		try {
			this.doPortConnection(this.outboundPortRegistre.getPortURI(), inboundPortRegister,
					ConnectorRegistreNode.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringBuilder logMessageBuilder = new StringBuilder();
		logMessageBuilder.append("Sensor values").append(this.descriptor.nodeIdentifier()).append(": [");

		for (SensorDataI sensorData : this.sensors) {
			logMessageBuilder.append(sensorData.getSensorIdentifier()).append(": ").append(sensorData.getValue())
					.append(", ");
		}
		if (!this.sensors.isEmpty()) {
			logMessageBuilder.setLength(logMessageBuilder.length() - 2); // Remove the last comma and space (", ")
		}

		logMessageBuilder.append("]");
		this.logMessage(logMessageBuilder.toString());

		super.start();
	}

	public void execute() throws Exception {

		this.logMessage("executing node component.");

		AcceleratedClock ac = outboundPortClock.getClock(TEST_CLOCK_URI);
		ac.waitUntilStart();
		Instant i0 = ac.getStartInstant();
		Instant i1 = i0.plusSeconds(60);

		long delay = ac.nanoDelayUntilInstant(i1); // Delay (ns)

		this.scheduleTask(o -> {
			try {
				if (!this.outboundPortRegistre.registered(this.descriptor.nodeIdentifier()))
					this.registerAndConnectToNeighbor();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}, delay, TimeUnit.NANOSECONDS);
	}


	@Override
	public synchronized void finalise() throws Exception {
	    // When the node leaves the sensor network, it must first disconnect from its neighbors and then
	    // unregister itself from the registry by calling the unregister method
	    this.logMessage("stopping node component.");

	    // Check if the port is connected before attempting to disconnect it
	    if (this.outboundPortNE.connected()) {
	        this.outboundPortNE.ask4Disconnection(descriptor);
	    }
	    if (this.outboundPortNW.connected()) {
	        this.outboundPortNW.ask4Disconnection(descriptor);
	    }
	    if (this.outboundPortSE.connected()) {
	        this.outboundPortSE.ask4Disconnection(descriptor);
	    }
	    if (this.outboundPortSW.connected()) {
	        this.outboundPortSW.ask4Disconnection(descriptor);
	    }
	    
        this.outboundPortRegistre.unregister(descriptor.nodeIdentifier());


	    if (this.outboundPortClock.connected()) {
	        this.doPortDisconnection(this.outboundPortClock.getPortURI());
	    }

	    if (this.outboundPortNE.connected()) {
	        this.doPortDisconnection(this.outboundPortNE.getPortURI());
	    }

	    if (this.outboundPortNW.connected()) {
	        this.doPortDisconnection(this.outboundPortNW.getPortURI());
	    }

	    if (this.outboundPortSE.connected()) {
	        this.doPortDisconnection(this.outboundPortSE.getPortURI());
	    }

	    if (this.outboundPortSW.connected()) {
	        this.doPortDisconnection(this.outboundPortSW.getPortURI());
	    }

	    if (this.outBoundPortClient.connected()) {
	        this.doPortDisconnection(this.outBoundPortClient.getPortURI());
	    }

	    if (this.outboundPortRegistre.connected()) {
	        this.doPortDisconnection(this.outboundPortRegistre.getPortURI());
	    }
	    super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			outboundPortClock.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.inboundPortClient.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outBoundPortClient.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.inboundPortSensor.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortRegistre.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortNE.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortNW.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortSE.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortSW.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}

	@Override
	public synchronized void shutdownNow() throws ComponentShutdownException {
		try {
			outboundPortClock.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.inboundPortClient.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outBoundPortClient.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.inboundPortSensor.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortRegistre.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortNE.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortNW.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortSE.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.outboundPortSW.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdownNow();
	}

	// --------------------------------------------------------------------------
	// Component internal services
	// --------------------------------------------------------------------------

	/**
	 * Processes a request by wrapping it into a {@link RequestContinuationI}
	 * instance and executing it. This method prepares the node's state and forwards
	 * the request for detailed processing.
	 * 
	 * @param request The {@link RequestI} instance containing the initial request
	 *                details.
	 * @return A {@link QueryResultI} object representing the result of the query
	 *         execution.
	 * @throws Exception If any errors occur during the processing of the request.
	 */
	public QueryResultI execute(RequestI request) throws Exception {
		if (gui != null) {
			gui.resetNodesBlink();
		}
		QueryResult queryR = new QueryResult(new ArrayList<>(), new ArrayList<>());
		ExecutionState executionState = new ExecutionState(null, queryR);
		RequestContinuationI clientRequest = new RequestContinuation(request, executionState, request.requestURI());
		evaluateQuery(clientRequest);
		if (executionState.isContinuationSet()) {
			handleQueryPropagation(clientRequest);
		}
		return queryR;

	}

	public void executeAsync(RequestI request) throws Exception {
		QueryResult queryR = new QueryResult(new ArrayList<>(), new ArrayList<>());
		ExecutionState executionState = new ExecutionState(null, queryR);
		RequestContinuationI clientRequest = new RequestContinuation(request, executionState, request.requestURI());
		evaluateQuery(clientRequest);
		if (executionState.isContinuationSet()) {
			handleQueryPropagation(clientRequest);
		} else {
			this.sendToClient(clientRequest, executionState);
		}

	}

	/**
	 * Executes a sensor network query based on a continuation request. This method
	 * handles both geographic and broadcast queries by updating and evaluating the
	 * state of execution based on sensor data and node information. It also manages
	 * network communication with neighboring nodes to fulfill the query
	 * requirements.
	 * 
	 * <p>
	 * This method checks if a request has been processed before to avoid redundant
	 * processing and potential infinite loops in the network.
	 * </p>
	 *
	 * @return A {@link QueryResultI} object representing the aggregated or
	 *         evaluated result after executing the query.
	 * @throws Exception If there are issues in query processing or during network
	 *                   communication.
	 */
	@Override
	public QueryResultI execute(RequestContinuationI requestContinuation) throws Exception {
		if (this.processedRequests.contains(requestContinuation.requestURI())) {
			return new QueryResult(new ArrayList<>(), new ArrayList<>());
		}
		return processQuery(requestContinuation);
	}

	/**
	 * Asynchronously executes a request based on the provided
	 * {@link RequestContinuationI}. This method is intended for asynchronous
	 * handling of requests where the results are not immediately required to be
	 * returned but can be processed in a non-blocking manner.
	 *
	 * <p>
	 * This method is currently a stub and needs to be implemented.
	 * </p>
	 * 
	 * @param requestContinuation The {@link RequestContinuationI} object
	 *                            representing the request continuation.
	 * @throws Exception If there are issues in processing the request
	 *                   asynchronously.
	 */
	@Override
	public void executeAsync(RequestContinuationI requestContinuation) throws Exception {
		if (!(this.processedRequests.contains(requestContinuation.requestURI()))) {
			processQuery(requestContinuation);
		}
	}

	/**
	 * Attempts to establish a connection with a specified neighbouring node. This
	 * method updates the graphical interface to reflect the connection and adds the
	 * neighbour to the known set. It determines the appropriate direction and
	 * manages the connection based on the determined direction.
	 *
	 * @param neighbour The {@link NodeInfoI} instance representing the neighbouring
	 *                  node to connect with.
	 * @throws Exception If managing the connection based on direction fails.
	 */
	@Override
	public void ask4Connection(NodeInfoI neighbour) throws Exception {
		try {
			connectToNeighbor(neighbour, false);
		} catch (Exception e) {
			this.traceMessage("Failed to manage connection to "+neighbour.nodeIdentifier());
		}
	}

	/**
	 * Disconnects from a specified neighbouring node. This method updates the
	 * graphical interface to reflect the disconnection and uses the directional
	 * information to manage the disconnection process.
	 *
	 * @param neighbour The {@link NodeInfoI} instance representing the neighbouring
	 *                  node to disconnect from.
	 * @throws Exception If there is an issue in managing the disconnection.
	 */
	@Override
	public void ask4Disconnection(NodeInfoI neighbour) throws Exception {
		this.Lock.writeLock().lock();
		Direction direction = this.descriptor.nodePosition().directionFrom(neighbour.nodePosition());
		URINodeOutBoundPortToNode outboundPort = getPortByDirection(direction);
		if (outboundPort != null) {
			try {
				if (outboundPort.connected()) {
					this.doPortDisconnection(outboundPort.getPortURI());
					this.neighbors.remove(neighbour);
					if (gui != null) {
						gui.removeGraphicalConnection(this.descriptor.nodeIdentifier(), neighbour.nodeIdentifier());
						gui.removeGraphicalConnection(neighbour.nodeIdentifier(), this.descriptor.nodeIdentifier());
					}
				}
				NodeInfoI node = outboundPortRegistre.findNewNeighbour(this.descriptor, direction);
				if (node != null) {
					connectToNeighbor(node, true);
				}
			} catch (Exception e) {
				this.traceMessage("Error handling disconnection and reconnection");
			} finally {
				this.Lock.writeLock().unlock();
			}
		} else {
			this.traceMessage("Invalid direction: "+ direction);
		}
	}

	/**
	 * Retrieves the outbound port corresponding to a specific direction.
	 *
	 * @param direction The direction for which the outbound port is needed.
	 * @return The outbound port corresponding to the given direction or
	 *         {@code null} if the direction is invalid.
	 */
	private URINodeOutBoundPortToNode getPortByDirection(Direction direction) {
		switch (direction) {
		case NE:
			return outboundPortNE;
		case NW:
			return outboundPortNW;
		case SE:
			return outboundPortSE;
		case SW:
			return outboundPortSW;
		default:
			this.logMessage("Unknown direction: " + direction);
			return null;
		}
	}

	/**
	 * Registers the current node and establishes connections with all known
	 * neighbours. This method logs the registration process and proceeds to connect
	 * to each neighbour individually.
	 */
	private void registerAndConnectToNeighbor() {
		this.logMessage("Registering node and connecting to neighbors");
		try {
			this.neighbors = this.outboundPortRegistre.register(descriptor);
			for (NodeInfoI node : neighbors) {
				connectToNeighbor(node, true);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Connects to a specified neighboring node based on the calculated directional
	 * position. This method manages the establishment of the connection by first
	 * disconnecting any existing connections on the relevant port before making a
	 * new connection. It also optionally initiates a connection request to the
	 * descriptor depending on the {@code ask} parameter.
	 *
	 * @param node The {@link NodeInfoI} representing the neighbor node to connect
	 *             to.
	 * @param ask  A boolean flag that determines whether to initiate a connection
	 *             request after establishing the connection. If true, the method
	 *             will initiate a connection request; otherwise, it will only
	 *             establish the connection.
	 * @throws Exception if there is an issue retrieving the direction, handling
	 *                   port connections/disconnections, or any other
	 *                   operation-related exceptions.
	 */
	private void connectToNeighbor(NodeInfoI node, boolean ask) throws Exception {
		Direction direction = descriptor.nodePosition().directionFrom(node.nodePosition());
		URINodeOutBoundPortToNode portToUse = getPortByDirection(direction);
		ReentrantReadWriteLock portLock = portLocks.get(direction);

		if (portLock == null) {
			this.logMessage("Invalid direction specified: " + direction);
			return;
		}
		try {
			portLock.writeLock().lock();
			if (portToUse == null) {
				this.logMessage("Invalid direction specified: " + direction);
				return;
			}
			String inboundPortSensor = ((BCM4JavaEndPointDescriptorI) node.p2pEndPointInfo()).getInboundPortURI();
			if (portToUse.connected()) {
				portToUse.ask4Disconnection(descriptor);
				doPortDisconnection(portToUse.getPortURI());
			}
			doPortConnection(portToUse.getPortURI(), inboundPortSensor,
					ConnectorSensorToSensor.class.getCanonicalName());
			if (gui != null) {
				gui.addGraphicalConnection(this.descriptor.nodeIdentifier(), node.nodeIdentifier());
			}
			this.neighbors.add(node);
			nodeOutboundPorts.put(portToUse.getPortURI(), node);
			this.logMessage("Connection established towards neighbor at " + direction + ": " + node.nodeIdentifier());

			if (ask) {
				portToUse.ask4Connection(descriptor);
			}
		} finally {
			portLock.writeLock().unlock();
		}
	}

	/**
	 * Processes a request by orchestrating various steps including state
	 * preparation, query evaluation, UI updates, and query propagation based on the
	 * execution state.
	 *
	 * @param request The {@link RequestContinuationI} instance containing
	 *                continuation details of the query.
	 * @return A {@link QueryResultI} object representing the aggregated or
	 *         evaluated result after executing the query.
	 * @throws Exception if there are issues during query processing or propagation.
	 */
	private QueryResultI processQuery(RequestContinuationI request) throws Exception {
		evaluateQuery(request);
		handleQueryPropagation(request);
		return ((ExecutionState) request.getExecutionState()).getCurrentResult();
	}

	/**
	 * Evaluates the given query based on the type (GQuery or BQuery) and updates
	 * the execution state accordingly.
	 *
	 * @param request The {@link RequestContinuationI} instance containing the query
	 *                and related information.
	 */
	private void evaluateQuery(RequestContinuationI request) {
		this.processedRequests.add(request.requestURI());
		ExecutionState es = (ExecutionState) request.getExecutionState();
		ProcessingNode processingNode = new ProcessingNode(this.descriptor.nodeIdentifier(),
				this.descriptor.nodePosition(), this.neighbors, this.sensors);
		es.updateProcessingNode(processingNode);
		if (request.getQueryCode() instanceof GQuery) {
			((GQuery) request.getQueryCode()).eval(es);
		} else {
			((BQuery) request.getQueryCode()).eval(es);
		}
		if (gui != null) {
			gui.toggleNodeBlinking(this.descriptor.nodeIdentifier());
		}
	}

	/**
	 * Determines the mode of query propagation (flooding or directional) based on
	 * the execution state and executes the propagation accordingly.
	 *
	 * @param request The {@link RequestContinuationI} instance containing the query
	 *                and its continuation data.
	 * @return The result of the query after attempting propagation, if applicable.
	 * @throws Exception if there is an issue during query propagation.
	 */
	private void handleQueryPropagation(RequestContinuationI request) throws Exception {
		ExecutionState executionState = (ExecutionState) request.getExecutionState();
		if (executionState.isFlooding()) {
			propagateFlooding(request);
		} else if (executionState.isDirectional()) {
			if (!(executionState.noMoreHops() || executionState.getDirections().isEmpty())) {
				handleDirectionalPropagation(request);
			} else {
				if (request.isAsynchronous())
					sendToClient(request, executionState);
			}
		}

	}

	/**
	 * Sends the request continuation and execution state to the client.
	 * 
	 * @param request         the request continuation to be sent
	 * @param executionState  the execution state associated with the request
	 * @throws Exception if an error occurs during the process
	 */
	private synchronized void sendToClient(RequestContinuationI request, ExecutionState executionState)
	        throws Exception {
	    // Get the inbound port URI of the client
	    String inboundPortClient = ((BCM4JavaEndPointDescriptorI) request.clientConnectionInfo().endPointInfo())
	            .getInboundPortURI();
	    
	    // Establish a connection from the sensor to the client
	    this.doPortConnection(outBoundPortClient.getPortURI(), inboundPortClient,
	            ConnectorSensorToClient.class.getCanonicalName());
	    
	    // Accept the request result using the outbound port client
	    this.outBoundPortClient.acceptRequestResult(request.requestURI(), executionState.getCurrentResult());
	    
	    // Disconnect the outbound port client after sending the request result
	    this.doPortDisconnection(this.outBoundPortClient.getPortURI());
	}

	/**
	 * Handles query propagation using a flooding approach where the query is sent
	 * to all reachable neighbors within a maximal distance.
	 *
	 * @param request The request continuation instance.
	 * @return The current result of the query after flooding to neighbors.
	 * @throws Exception if there is an issue during the flooding propagation.
	 */
	private void propagateFlooding(RequestContinuationI request) throws Exception {
		ExecutionState executionState = (ExecutionState) request.getExecutionState();

		for (NodeInfoI n : neighbors) {
			Direction d = this.descriptor.nodePosition().directionFrom(n.nodePosition());
			if (executionState.withinMaximalDistance(n.nodePosition())) {
				executeNeighborQuery(d, request);
			}
		}

		// Decide whether to execute tasks asynchronously or synchronously
		if (request.isAsynchronous()) {
			sendToClient(request, executionState);
		}
	}

	/**
	 * Manages directional query propagation based on the specified directions
	 * within the execution state. This method determines the next direction for
	 * query propagation by examining available directions and the state of
	 * connection to those directions. It attempts to propagate the query to the
	 * first available and connected direction. If no directions are available or if
	 * there are no more hops allowed, it simply returns the current result.
	 *
	 * @param request The request continuation detailing the query continuation. It
	 *                is used to pass along the query specifics as the propagation
	 *                proceeds to different directions.
	 * @return The query result after attempting directional propagation. If no
	 *         propagation occurs, the current result of the execution state is
	 *         returned.
	 * @throws Exception if there is an issue during directional propagation, such
	 *                   as a failure in executing the query on the neighbor or
	 *                   issues with connection handling.
	 */
	private void handleDirectionalPropagation(RequestContinuationI request) throws Exception {
		ExecutionState es = (ExecutionState) request.getExecutionState();
		ArrayList<Direction> directions = new ArrayList<>(es.getDirections());
		Iterator<Direction> directionIterator = directions.iterator();

		while (directionIterator.hasNext()) {
			Direction d = directionIterator.next();
			URINodeOutBoundPortToNode port = getPortByDirection(d);
			if (port != null && port.connected()) {
				executeNeighborQuery(d, request);
				break;
			}
		}
	}

	/**
	 * Executes the query on a neighboring node based on the specified direction.
	 *
	 * @param direction The direction in which the neighbor node is located.
	 * @param request   The {@link RequestContinuationI} instance detailing the
	 *                  query continuation.
	 * @return
	 * @throws Exception if there is an issue executing the query on the neighbor
	 *                   node.
	 */
	private QueryResultI executeNeighborQuery(Direction direction, RequestContinuationI request) throws Exception {
		URINodeOutBoundPortToNode port = getPortByDirection(direction);
		ExecutionState executionState = ((ExecutionState) request.getExecutionState());
		ExecutionState executionStateClone = executionState.clone();
		RequestContinuationI clientRequest = new RequestContinuation(request, executionStateClone,
				request.requestURI());
		if (port != null && port.connected()) {
			if (gui != null) {
				gui.startGraphicalLightAnimation(this.descriptor.nodeIdentifier(),
						this.nodeOutboundPorts.get(port.getPortURI()).nodeIdentifier());
			}
			if (request.isAsynchronous()) {
				port.executeAsync(clientRequest);
			} else {
				executionStateClone.resetQuery();
				QueryResultI qr = port.execute(clientRequest);
				executionState.addToCurrentResult(qr);
				return qr;
			}
		}
		return null;
	}

	/**
	 * Initializes and publishes all outbound ports necessary for the sensor node's
	 * operation. This includes ports for registering with the network, connecting
	 * to the clock server, and interacting with other nodes in specific directions.
	 * 
	 * @throws Exception If there is an issue initializing or publishing the ports.
	 */
	private void initializeOutboundPorts() throws Exception {
		this.outboundPortRegistre = new URINodeOutBoundPortToRegister(this);
		this.outboundPortNE = new URINodeOutBoundPortToNode(this);
		this.outboundPortNW = new URINodeOutBoundPortToNode(this);
		this.outboundPortSE = new URINodeOutBoundPortToNode(this);
		this.outboundPortSW = new URINodeOutBoundPortToNode(this);

		this.outBoundPortClient = new URINodeOutboundPortToClient(this);

		this.outboundPortClock = new ClocksServerOutboundPort(this);

		this.outboundPortRegistre.publishPort();
		this.outboundPortNE.publishPort();
		this.outboundPortNW.publishPort();
		this.outboundPortSE.publishPort();
		this.outboundPortSW.publishPort();

		this.outBoundPortClient.publishPort();

		this.outboundPortClock.publishPort();
	}

	// -----------------------------------------------------------------------------

}