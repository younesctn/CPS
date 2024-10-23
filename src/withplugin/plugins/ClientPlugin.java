package withplugin.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.cps.sensor_network.interfaces.BCM4JavaEndPointDescriptorI;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestResultCI;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.nodes.interfaces.RequestingCI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.LookupCI;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import withplugin.ports.URIClientInboundPortForNodeForPlugin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import app.connectors.ConnectorClientToSensor;
import app.connectors.ConnectorRegistreClient;
import app.models.Bcm4javaEndPointDescriptor;
import app.models.ClientConfig;
import app.models.ConnectionInfo;
import app.models.Request;
import app.models.TimingInfo;
import app.ports.URIClientOutBoundPortToNode;
import app.ports.URIClientOutBoundPortToRegister;

/**
 * A plugin component that extends the functionality of a client component within a sensor network.
 * This plugin facilitates the interaction between the client and various network services including
 * sensor nodes, registration services, and clock synchronization servers.
 *
 * The {@code ClientPlugin} handles tasks such as setting up communication ports, connecting to necessary
 * services, and managing request executions both synchronously and asynchronously. It also deals with
 * the aggregation and processing of query results from sensor nodes.
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Initial setup of outbound and inbound ports for communication with other network components.</li>
 *   <li>Connection management to ensure the client can interact with the clock server and registration server.</li>
 *   <li>Execution of sensor node requests, handling both synchronous and asynchronous operations.</li>
 *   <li>Collection and merging of results from sensor nodes based on requests issued by the client.</li>
 *   <li>Logging and error management to provide debugging support and robust error handling.</li>
 * </ul>
 * @author Malek Bouzarkouna, Younes Chetouani, Mohamed Amine Zemali
 * @see AbstractPlugin
 * @see ComponentI
 */
public class ClientPlugin extends AbstractPlugin {
	private static final long serialVersionUID = 1L;
	
	protected URIClientOutBoundPortToRegister uriOutPortRegister;
	protected URIClientOutBoundPortToNode uriOutPortNode;
	private String inBoundPortRegister;
	private URIClientInboundPortForNodeForPlugin inboundPortClient;
	
//	private PrintWriter logWriter;
	private ClocksServerOutboundPort outBoundPortClock;
	private String requestNodeName;
	private ConcurrentHashMap<String, List<QueryResultI>> resultsMap = new ConcurrentHashMap<>();
	private ConnectionInfoI ClientInfo;
	private ClientConfig configClient;
	private ConcurrentHashMap<String, TimingInfo> timingMap = new ConcurrentHashMap<>();

	// -------------------------------------------------------------------------
	// Life cycle
	// -------------------------------------------------------------------------
	
	/**
     * Constructs a {@code ClientPlugin} with a specified client configuration. This constructor
     * initializes the plugin with necessary configurations and prepares it for installation
     * on a client component.
     *
     * @param configClient The configuration settings for this plugin, including node names and connection settings.
     */
	public ClientPlugin(ClientConfig configClient) {
		super();
		this.configClient = configClient;

	}

	/**
	 * Initializes the plugin by setting up outbound and inbound ports, and
	 * configuring initial connection settings based on the provided configuration.
	 *
	 * @param owner The component that owns this plugin.
	 * @throws Exception If any error occurs during the initialization of ports or
	 *                   connections.
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addOfferedInterface(RequestResultCI.class);
		this.addRequiredInterface(LookupCI.class);
		this.addRequiredInterface(ClocksServerCI.class);
		this.addRequiredInterface(RequestingCI.class);
	}

	@Override
	public void initialise() throws Exception {
		super.initialise();
		
		//For Test performance
//		  try {
//	            File logFile = new File("request_log.txt");
//	            if (!logFile.exists()) {
//	                logFile.createNewFile();
//	            }
//	            FileWriter fw = new FileWriter(logFile, true);
//	            this.logWriter = new PrintWriter(fw, true);
//	        } catch (IOException e) {
//	            System.err.println("Failed to initialize log writer: " + e.getMessage());
//	        }
		
		this.outBoundPortClock = new ClocksServerOutboundPort(this.getOwner());
		outBoundPortClock.publishPort();

		this.uriOutPortRegister = new URIClientOutBoundPortToRegister(this.getOwner());
		this.uriOutPortRegister.publishPort();

		this.requestNodeName = configClient.getRequestNodeName();
		this.uriOutPortNode = new URIClientOutBoundPortToNode(this.getOwner());
		this.uriOutPortNode.publishPort();

		this.inBoundPortRegister = configClient.getInboundPortRegister();

		this.inboundPortClient = new URIClientInboundPortForNodeForPlugin(this.getOwner(), this.getPluginURI());
		this.inboundPortClient.publishPort();

		BCM4JavaEndPointDescriptorI uriClient = new Bcm4javaEndPointDescriptor(inboundPortClient.getPortURI());

		this.ClientInfo = new ConnectionInfo(requestNodeName, uriClient);

		// ---------------------------------------------------------------------
		// Connection phase
		// ---------------------------------------------------------------------

		// do the connection with the clock
		try {
			this.getOwner().doPortConnection(outBoundPortClock.getPortURI(),ClocksServer.STANDARD_INBOUNDPORT_URI, 
					ClocksServerConnector.class.getCanonicalName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// do the connection with the register
		try {
			this.getOwner().doPortConnection(this.uriOutPortRegister.getPortURI(),inBoundPortRegister,
					ConnectorRegistreClient.class.getCanonicalName());
			this.logMessage("Connected to Register");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Finalizes the plugin before uninstallation. This method disconnects all ports
	 * and removes interfaces, ensuring a clean shutdown and resource deallocation.
	 * 
	 * @throws Exception If there is an issue during the disconnection of ports or
	 *                   interface removal.
	 */
	@Override
	public void uninstall() throws Exception {
		try {
			this.uriOutPortRegister.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.uriOutPortNode.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.outBoundPortClock.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.inboundPortClient.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.removeOfferedInterface(RequestResultCI.class);
		this.removeRequiredInterface(RequestingCI.class);
		this.removeRequiredInterface(LookupCI.class);
		this.removeRequiredInterface(ClocksServerCI.class);
		
	}

	@Override
	public synchronized void finalise() throws Exception {
		this.logMessage("stopping client component.");
		this.getOwner().printExecutionLogOnFile("client");
		if (this.uriOutPortRegister.connected()) {
			this.getOwner().doPortDisconnection(this.uriOutPortRegister.getPortURI());
		}
		if (this.uriOutPortNode.connected()) {
			this.getOwner().doPortDisconnection(this.uriOutPortNode.getPortURI());
		}
		if (this.outBoundPortClock.connected()) {
			this.getOwner().doPortDisconnection(this.outBoundPortClock.getPortURI());
		}

		super.finalise();
	}

	// -------------------------------------------------------------------------
	// Plug-in services implementation
	// -------------------------------------------------------------------------
	
	/**
	 * Retrieves a reference to the Clock Server's outbound port. This port is used to communicate
	 * with the Clock Server for synchronizing time across various components.
	 *
	 * @return The outbound port connected to the Clock Server.
	 */
	public ClocksServerOutboundPort getRegisterReference() {
		return this.outBoundPortClock;
	}

	/**
	 * Executes a single request to a sensor node, processing the response and
	 * logging the result.
	 *
	 * @param clientRequest the request to execute.
	 * @throws Exception if there is an error in executing the request or processing
	 *                   the response.
	 */
	private void executeRequest(RequestI clientRequest) throws Exception {

		if (clientRequest instanceof Request) {
			Request request = (Request) clientRequest;
			request.setAsynchronous(false);
			QueryResultI queryResult = uriOutPortNode.execute(request);
			printQueryResult(queryResult);
		} else {
			throw new IllegalArgumentException("Expected clientRequest to be an instance of Request");
		}
	}

	/**
	 * Executes a request asynchronously. This method ensures the request is flagged
	 * as asynchronous and then delegates its execution to the outbound port of a
	 * node. If the provided request is not of the expected type, an exception is
	 * thrown.
	 *
	 * @param clientRequest The request to be executed asynchronously.
	 * @throws Exception                If there is an error during the asynchronous
	 *                                  execution.
	 * @throws IllegalArgumentException If the provided request is not an instance
	 *                                  of {@link Request}.
	 */
	private void executeRequestAsync(RequestI clientRequest) throws Exception {
		if (clientRequest instanceof Request) {
			Request request = (Request) clientRequest;
			request.setAsynchronous(true);
			// Record start time
			 timingMap.put(request.requestURI(), new TimingInfo());
			uriOutPortNode.executeAsync(request);
		} else {
			throw new IllegalArgumentException("Expected clientRequest to be an instance of Request");
		}
	}

	/**
	 * Logs the result of a query to the console. This method checks the type of
	 * query result and formats the output accordingly, displaying either the
	 * positive sensor nodes or the gathered sensor values.
	 *
	 * @param queryResult The result of a query to be logged.
	 */
	private void printQueryResult(QueryResultI queryResult) {
		if (queryResult.isBooleanRequest()) {
			logMessage("request result: " + queryResult.positiveSensorNodes().toString());
		} else if (queryResult.isGatherRequest()) {
			logMessage("request result: " + queryResult.gatheredSensorsValues().toString());
		}

	}

	/**
	 * Attempts to find and connect to a node by its name. This method retrieves the
	 * connection information for the specified node and, if found, initiates a
	 * connection. If the node is not found, it logs an appropriate message.
	 *
	 * @param nodeName The name of the node to connect to.
	 * @throws Exception If there is an error during the connection process.
	 */
	public void requestNodeAndConnectByName(String nodeName) throws Exception {
		Optional<ConnectionInfoI> node = getNode(nodeName);
		node.ifPresentOrElse(this::connectToNode, () -> logMessage("Node " + nodeName + " not found."));
	}

	/**
	 * Attempts to retrieve connection information for a specified node by its
	 * identifier. This method queries the registered nodes through an outbound port
	 * to the register component, returning an {@link Optional} that contains the
	 * node's connection information if found. If the node is not found or if an
	 * error occurs during the query, the method returns an empty {@link Optional}.
	 *
	 * @param nodeName The identifier of the node for which connection information
	 *                 is sought.
	 * @return An {@link Optional} of {@link ConnectionInfoI} which will be
	 *         non-empty if the node is found, and empty if the node is not found or
	 *         if an error occurs during the request.
	 */
	private Optional<ConnectionInfoI> getNode(String nodeName) {
		try {
			logMessage("Requesting node: " + nodeName);
			ConnectionInfoI nodeResult = this.uriOutPortRegister.findByIdentifier(nodeName);
			return Optional.ofNullable(nodeResult);
		} catch (Exception e) {
			logError("Error requesting node: " + nodeName, e);
			return Optional.empty();
		}
	}

	/**
	 * Connects to a specified node within the network using detailed connection
	 * information.
	 *
	 * @param node the connection information for the node to connect to.
	 */
	private void connectToNode(ConnectionInfoI node) {
		try {
			BCM4JavaEndPointDescriptorI endPointDescriptor = (BCM4JavaEndPointDescriptorI) node.endPointInfo();
			String inboundPortSensor = endPointDescriptor.getInboundPortURI();
			logMessage("Found node: " + node.nodeIdentifier());

			this.getOwner().doPortConnection(this.uriOutPortNode.getPortURI(),inboundPortSensor,
					ConnectorClientToSensor.class.getCanonicalName());
			this.logMessage("Connected to " + node.nodeIdentifier());
		} catch (Exception e) {
			logError("Error connecting to node: " + node.nodeIdentifier(), e);
		}
	}
	
	/**
	 * Logs an error message along with the stack trace of the exception. This method provides
	 * a centralized way to handle error logging, facilitating easier maintenance and standardization
	 * of error management.
	 *
	 * @param message The error message to log.
	 * @param e The exception associated with the error.
	 */
	private void logError(String message, Exception e) {
		logMessage(message);
		e.printStackTrace();
	}

	/**
	 * Schedules a list of requests to be executed sequentially with a defined delay
	 * between each execution. The tasks are scheduled based on a starting instant
	 * and are spaced out by a specified delay. This method leverages an
	 * {@link AcceleratedClock} to determine the precise nanoseconds delay until
	 * each task's intended start time, ensuring accurate scheduling according to
	 * the clock's acceleration.
	 *
	 * @param requests          The list of {@link RequestI} objects to be executed.
	 * @param start             The starting {@link Instant} from which the first
	 *                          task is scheduled.
	 * @param ac                The {@link AcceleratedClock} used to calculate
	 *                          precise delay times for task scheduling.
	 * @param delayBetweenTasks The delay in seconds between consecutive task
	 *                          executions.
	 */
	public void scheduleTasks(List<RequestI> requests, Instant start, AcceleratedClock ac, int delayBetweenTasks,
			boolean isAsynchronous) {
		for (int i = 0; i < requests.size(); i++) {
			final int taskIndex = i;
			long delay = isAsynchronous ? taskIndex * delayBetweenTasks
					: ac.nanoDelayUntilInstant(start.plusSeconds(taskIndex * delayBetweenTasks));
			this.getOwner().scheduleTask(o -> {
				try {
					Request request = (Request) requests.get(taskIndex);
					if (isAsynchronous) {
						request.setClient(ClientInfo);
						
						executeRequestAsync(request);
						Instant i2 = start.plusSeconds(120);
						long delay2 = ac.nanoDelayUntilInstant(i2);
						this.getOwner().scheduleTask(ob -> {
							try {
								this.mergeAndPrint(request.requestURI());
								
//								For the performance test
								
//								TimingInfo timingInfo = timingMap.get(request.requestURI());
//					            long duration = timingInfo.getEndTime() - timingInfo.getStartTime();
//					            logWriter.println(duration);
//						        logMessage("Request " + request.requestURI() + " latest time updated to " + (timingInfo.getEndTime() - timingInfo.getStartTime()) + "ms" );
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}, delay2, TimeUnit.NANOSECONDS);
					} else {
						executeRequest(request);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, delay, TimeUnit.NANOSECONDS);
		}
	}

	/**
	 * Accepts and stores a query result associated with a specific request URI. The
	 * method is thread-safe and updates the result map atomically, ensuring that
	 * multiple threads can safely operate on it. If no list exists for the given
	 * URI, a new list is created.
	 *
	 * @param requestURI The URI of the request to which the result belongs.
	 * @param result     The query result to store.
	 */
	public void acceptRequestResult(String requestURI, QueryResultI result) {
		// The compute methods are thread-safe and atomic
		resultsMap.compute(requestURI, (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(result);
			return v;
		});
		
		// Update end time each time this method is called
	    TimingInfo timingInfo = timingMap.get(requestURI);
	    if (timingInfo != null) {
	        timingInfo.setEndTime();  // Always update to the latest call time
	    }
	}

	/**
	 * Merges all query results associated with a given request URI and prints the
	 * merged result. This method first retrieves the list of results for the URI
	 * and then combines them based on the type of query (gather or boolean). If the
	 * list is empty, it logs that no results were found. Otherwise, it merges the
	 * results and prints the final aggregated result.
	 *
	 * @param requestURI The URI of the request whose results are to be merged and
	 *                   printed.
	 * @throws Exception if an error occurs during result processing or printing.
	 */
	public void mergeAndPrint(String requestURI) throws Exception {
		// The compute methods are thread-safe and atomic
		List<QueryResultI> resultsList = resultsMap.computeIfAbsent(requestURI, k -> {
			this.logMessage("No results to process found");
			return new ArrayList<>();
		});

		if (!resultsList.isEmpty()) {
			QueryResultI mergedResults = resultsList.get(0);
			;
			for (int i = 1; i < resultsList.size(); i++) {
				QueryResultI result = resultsList.get(i);
				if (result.isGatherRequest()) {
					updateGatheredSensors(mergedResults, result);
				} else if (result.isBooleanRequest()) {
					updatePositiveSensorNodes(mergedResults, result);
				}
			}

			this.printQueryResult(mergedResults);
		}
	}

	/**
	 * Updates the gathered sensor data for a merged result by combining new sensor
	 * data from another result. This operation is performed sequentially,
	 * processing the sensor data merging task within a single thread.
	 *
	 * @param existingResult The existing merged result to update.
	 * @param newResult      The new result whose sensor data is to be added to the
	 *                       existing merged result.
	 * @throws InterruptedException if the thread execution is interrupted.
	 */
	private void updateGatheredSensors(QueryResultI existingResult, QueryResultI newResult) {
	    List<SensorDataI> sensors = new ArrayList<>(newResult.gatheredSensorsValues());
	    synchronized (existingResult) {
	        for (SensorDataI sensorData : sensors) {
	            if (!existingResult.gatheredSensorsValues().contains(sensorData)) {
	                existingResult.gatheredSensorsValues().add(sensorData);
	            }
	        }
	    }
	}


	/**
	 * Updates the set of positive sensor nodes in the existing result by adding new
	 * positive sensor nodes from another result. This method ensures that the set
	 * contains unique sensor nodes only.
	 *
	 * @param existingResult The existing result to update.
	 * @param newResult      The new result from which positive sensor nodes are to
	 *                       be added.
	 */
	private void updatePositiveSensorNodes(QueryResultI existingResult, QueryResultI newResult) {
		Set<String> uniqueSensorNodes = new LinkedHashSet<>(existingResult.positiveSensorNodes());
		uniqueSensorNodes.addAll(newResult.positiveSensorNodes());
		existingResult.positiveSensorNodes().clear();
		existingResult.positiveSensorNodes().addAll(uniqueSensorNodes);
	}

}