package app;

import app.factory.RequestFactory;
import app.models.ClientConfig;
import app.models.EnumSensorIdentifier;
import app.models.Position;
import app.models.SensorConfig;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.config.Config;
import app.factory.ComponentFactory;
import fr.sorbonne_u.components.helpers.CVMDebugModes;
import fr.sorbonne_u.utils.aclocks.ClocksServer;

import static app.factory.RequestFactory.createRequest;

/**
 * The {@code DistributedCVM} class manages the initialization and deployment in
 * multiple jvm of simulation components for a sensor network, including clocks,
 * user interfaces, and the connections between client and sensor components. It
 * inherits from {@link AbstractDistributedCVM} and configures various aspects
 * of the component lifecycle, visualization, and clock synchronization.
 *
 * Authors: Malek Bouzarkouna, Younes Chetouani, Amine Zemali
 */
public class DistributedCVM extends AbstractDistributedCVM {
	// ------------------------------------------------------------------------
	// Instance variables
	// ------------------------------------------------------------------------
	protected static final String URIRegisterInboundPortURINode = "Regitre-Node-Uri";
	protected static final String URIRegisterInboundPortURIClient = "Regitre-Client-Uri";
	protected String serverClock;
	protected String registerURI;
	protected ArrayList<String> clientURIs = new ArrayList<>();
	protected ArrayList<String> sensorURIs = new ArrayList<>();

	protected static final String Client_AND_NODE_JVM_URI_0 = "jvm0";
	protected static final String Client_AND_NODE_JVM_URI_1 = "jvm1";
	protected static final String Client_AND_NODE_JVM_URI_2 = "jvm2";
	protected static final String Client_AND_NODE_JVM_URI_3 = "jvm3";
	protected static final String Client_AND_NODE_JVM_URI_4 = "jvm4";
	protected static final String Client_AND_NODE_JVM_URI_5 = "jvm5";
	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	/**
	 * Constructs a {@code DistributedCVM} that initializes configurations for
	 * sensors and positions.
	 * 
	 * @param args the command line arguments.
	 * @throws Exception if an error occurs during initialization.
	 */
	public DistributedCVM(String[] args) throws Exception {
		super(args);
		CVMUtils.initializePositionsAndSensors();
	}

	@Override
	public void instantiateAndPublish() throws Exception {
		super.instantiateAndPublish();
		int index = -1;
		if (AbstractCVM.getThisJVMURI().equals(Client_AND_NODE_JVM_URI_0)) {
			configureDebugMode();
			createServerClock();
			createRegisterComponent();

		}
		if (AbstractCVM.getThisJVMURI().equals(Client_AND_NODE_JVM_URI_1)) {
			index = 0;
		} else if (AbstractCVM.getThisJVMURI().equals(Client_AND_NODE_JVM_URI_2)) {
			index = 1;
		} else if (AbstractCVM.getThisJVMURI().equals(Client_AND_NODE_JVM_URI_3)) {
			index = 2;
		} else if (AbstractCVM.getThisJVMURI().equals(Client_AND_NODE_JVM_URI_4)) {
			index = 3;
		} else if (AbstractCVM.getThisJVMURI().equals(Client_AND_NODE_JVM_URI_5)) {
			index = 4;
		}

		if (index != -1) {
			createClientComponents(index);
			createSensorComponents(10, index);
		} else {
			System.out.println("Unknown JVM URI: " + AbstractCVM.getThisJVMURI());
		}
	}

	@Override
	public void interconnect() throws Exception {
		super.interconnect();
	}

	/**
	 * Finalizes components before stopping them.
	 * 
	 * @throws Exception if an error occurs during finalization.
	 */
	@Override
	public void finalise() throws Exception {
		super.finalise();
	}

	/**
	 * Stops components and releases resources.
	 * 
	 * @throws Exception if an error occurs during shutdown.
	 */
	@Override
	public void shutdown() throws Exception {
		assert this.allFinalised();
		super.shutdown();
	}

	// --------------------------------------------------------------------------
	// services
	// --------------------------------------------------------------------------

	/**
	 * Creates the server clock to synchronize all system components. Uses the
	 * defined configuration to initiate the clock at a specific time and with an
	 * acceleration factor.
	 * 
	 * @throws Exception if the clock component cannot be created or configured
	 *                   properly.
	 */

	private void createServerClock() throws Exception {
		long unixEpochStartTimeInNanos = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis() + Config.START_DELAY);
		this.serverClock = AbstractComponent.createComponent(ClocksServer.class.getCanonicalName(),
				new Object[] { Config.TEST_CLOCK_URI, unixEpochStartTimeInNanos, Instant.parse(Config.START_INSTANT),
						Config.ACCELERATION_FACTOR });
	}

	private void configureDebugMode() {
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.LIFE_CYCLE);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.INTERFACES);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PORTS);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CALLING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.EXECUTOR_SERVICES);
	}

	/**
	 * Creates the registration component necessary for managing connections between
	 * clients and sensors.
	 * 
	 * @throws Exception if the registration component cannot be created or fails to
	 *                   start.
	 */
	private void createRegisterComponent() throws Exception {
		this.registerURI = ComponentFactory.createRegister(URIRegisterInboundPortURINode,
				URIRegisterInboundPortURIClient);
		this.toggleTracing(this.registerURI);
		this.toggleLogging(this.registerURI);
	}

	/**
	 * Creates client components based on the predefined requests and
	 * configurations. Clients are created with a set of requests that can be
	 * executed against the sensor network.
	 * 
	 * @param index the index used to select predefined requests from a list.
	 * @throws Exception if there is a failure in creating the client components.
	 */
	private void createClientComponents(int index) throws Exception {
		// Define a list of predefined request sets
		List<List<RequestI>> predefinedRequestSets = new ArrayList<>();

		// You can add more predefined sets as needed
		predefinedRequestSets.add(Arrays.asList(
				createRequest(RequestFactory.RequestType.SIMPLE, EnumSensorIdentifier.Heat),
				createRequest(RequestFactory.RequestType.FCONT_BASED, new Position(2, 1), 3, EnumSensorIdentifier.Heat),
				createRequest(RequestFactory.RequestType.CUSTOM_COMPLEX_QUERY, EnumSensorIdentifier.Heat,
						EnumSensorIdentifier.WindDirection, new Position(2, 2), 3),
				createRequest(RequestFactory.RequestType.GQUERY_COMPLEX, 3, new Position(2, 1),
						EnumSensorIdentifier.Heat, EnumSensorIdentifier.Heat, EnumSensorIdentifier.Heat),
				createRequest(RequestFactory.RequestType.VERIF_TEMP_SEUIL, 20.0, EnumSensorIdentifier.Smoke,
						new Position(2, 2), 3)));

		predefinedRequestSets.add(Arrays.asList(
				createRequest(RequestFactory.RequestType.GQUERY_COMPLEX, 3, new Position(2, 1),
						EnumSensorIdentifier.Smoke, EnumSensorIdentifier.Heat, EnumSensorIdentifier.Biological),
				createRequest(RequestFactory.RequestType.BQUERY_COMPLEX, Direction.NE, 50.0, 3.0, 2,
						EnumSensorIdentifier.Heat, EnumSensorIdentifier.WindDirection),
				createRequest(RequestFactory.RequestType.BQUERY_OR, 10.0, 20.0, new Position(2, 1), 3,
						EnumSensorIdentifier.Humidity, EnumSensorIdentifier.Biological),
				createRequest(RequestFactory.RequestType.FCONT_WITH_MULTIPLE_SENSORS, new Position(1, 5), 3,
						EnumSensorIdentifier.Weather, EnumSensorIdentifier.WindSpeed),
				createRequest(RequestFactory.RequestType.ECONT_WITH_RGATHER, EnumSensorIdentifier.Weather,
						EnumSensorIdentifier.WindSpeed)

		));
		predefinedRequestSets.add(Arrays.asList(
				createRequest(RequestFactory.RequestType.BQUERY_OR, 30.0, 70.0, new Position(2, 1), 3,
						EnumSensorIdentifier.WindSpeed, EnumSensorIdentifier.Heat),
				createRequest(RequestFactory.RequestType.DCONT, Direction.SE, 3, EnumSensorIdentifier.Heat),
				createRequest(RequestFactory.RequestType.DCONT, Direction.NE, 5, EnumSensorIdentifier.Heat),
				createRequest(RequestFactory.RequestType.VERIF_TEMP_SEUIL, 80.0, EnumSensorIdentifier.Heat,
						new Position(2, 2), 3),
				createRequest(RequestFactory.RequestType.VERIF_TEMP_SEUIL, 60.0, EnumSensorIdentifier.WindSpeed,
						new Position(2, 2), 3)

		));
		predefinedRequestSets.add(Arrays.asList(

				createRequest(RequestFactory.RequestType.BQUERY_AND_VERGLAS, new Position(3, 3), 4),
				createRequest(RequestFactory.RequestType.BQUERY_AND_NOT_VERGLAS, new Position(3, 3), 4),
				createRequest(RequestFactory.RequestType.BQUERY_VERIFY_HUMIDITY, new Position(3, 3), 4, 40.0)

		));

		// Create clients and assign requests in a cyclic manner from the predefined
		// sets
		// Select request set from predefined lists cycling through them
		List<RequestI> requests = predefinedRequestSets.get(index % predefinedRequestSets.size());

		ClientConfig configClient = new ClientConfig(URIRegisterInboundPortURIClient, Config.TEST_CLOCK_URI, index + 1,
				requests, "n5", Config.ASYNC);

		String clientURI = ComponentFactory.createClient(configClient);
		this.toggleTracing(clientURI);
		this.toggleLogging(clientURI);
		clientURIs.add(clientURI);
	}

	/**
	 * Creates sensor components that simulate sensor nodes in the network. Each
	 * sensor component is configured with specific settings including location and
	 * capabilities.
	 * 
	 * @param number the number of sensor components to create.
	 * @param index  the index that helps distribute the creation across different
	 *               JVMs.
	 * @throws Exception if there is an error in creating sensor components.
	 */
	private void createSensorComponents(int number, int index) throws Exception {

		for (int i = index * 10; i < number * (index + 1); i++) {
			SensorConfig configNode = new SensorConfig(null, URIRegisterInboundPortURINode, Config.TEST_CLOCK_URI,
					CVMUtils.getNodeSensors().get(i), i + 1, CVMUtils.getPositions().get(i), 5, 5);

			String sensorURI = ComponentFactory.createSensor(configNode);

			this.toggleTracing(sensorURI);
			this.toggleLogging(sensorURI);
			sensorURIs.add(sensorURI);
		}
	}

	/**
	 * The main entry point to start the component virtual machine, creating an
	 * instance of {@code DistributedCVM}. This method starts the standard lifecycle
	 * of the CVM with a specified duration before shutting down.
	 * 
	 * @param args command-line arguments (not used).
	 */
	public static void main(String[] args) {
		try {
			DistributedCVM dcvm = new DistributedCVM(args);
			dcvm.startStandardLifeCycle(20000L);
			Thread.sleep(5000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}