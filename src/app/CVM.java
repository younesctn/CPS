package app;

import app.factory.RequestFactory;
import app.models.ClientConfig;
import app.models.EnumSensorIdentifier;
import app.models.Position;
import app.models.SensorConfig;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
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
 * The CVM class manages the initialization and deployment of simulation
 * components for a sensor network, including clocks, user interfaces, and the
 * connections between client and sensor components. It inherits from
 * {@link AbstractCVM} and configures various aspects of the component
 * lifecycle, visualization, and clock synchronization.
 *
 * Authors: Malek Bouzarkouna, Younes Chetouani, Amine Zemali
 */

public class CVM extends AbstractCVM {
	// ------------------------------------------------------------------------
	// Instance variables
	// ------------------------------------------------------------------------
	protected static final String URIRegisterInboundPortURINode = "Regitre-Node-Uri";
	protected static final String URIRegisterInboundPortURIClient = "Regitre-Client-Uri";
	protected String serverClock;
	protected String registerURI;
	protected ArrayList<String> clientURIs = new ArrayList<>();
	protected ArrayList<String> sensorURIs = new ArrayList<>();

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	/**
	 * Constructor for the CVM class that initializes sensor and position
	 * configurations.
	 *
	 * @throws Exception if an error occurs during initialization.
	 */
	public CVM() throws Exception {
		super();
		CVMUtils.initializePositionsAndSensors();
	}

	/**
	 * Deploys the necessary components and configures the environment for
	 * simulation.
	 *
	 * @throws Exception if an error occurs during deployment.
	 */
	@Override
	public void deploy() throws Exception {
		assert !this.deploymentDone();

		configureDebugMode();
		CVMUtils.createAndShowGUI();
		createServerClock();
		createRegisterComponent();
		createClientComponents();
		createSensorComponents();

		super.deploy();
		assert this.deploymentDone();
	}

	/**
	 * Finalizes the components before stopping them.
	 *
	 * @throws Exception if an error occurs during finalization.
	 */
	@Override
	public void finalise() throws Exception {
		super.finalise();
	}

	/**
	 * Stops the components and releases resources.
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
	 * specified configuration to initiate the clock at a specific time and with an
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

	/**
	 * Configures the debugging modes for the CVM, enabling the tracking of various
	 * activities.
	 */
	private void configureDebugMode() {
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.LIFE_CYCLE);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.INTERFACES);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PORTS);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CALLING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.EXECUTOR_SERVICES);
	}

	/**
	 * Creates the Register component.
	 */
	private void createRegisterComponent() throws Exception {
		this.registerURI = ComponentFactory.createRegister(URIRegisterInboundPortURINode,
				URIRegisterInboundPortURIClient);
		this.toggleTracing(this.registerURI);
		this.toggleLogging(this.registerURI);
	}

	/**
	 * Creates the Client components.
	 */

	private void createClientComponents() throws Exception {
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
//		 Random random = new Random();
		for (int i = 0; i < Config.NBCLIENT; i++) {
			// Select request set from predefined lists cycling through them
			List<RequestI> requests = predefinedRequestSets.get(i % predefinedRequestSets.size());

			ClientConfig configClient = new ClientConfig(URIRegisterInboundPortURIClient, Config.TEST_CLOCK_URI, i + 1,
					requests, "n5", Config.ASYNC);
//					+(1+random.nextInt(Config.NBNODE))

			String clientURI = ComponentFactory.createClient(configClient);
			this.toggleTracing(clientURI);
			this.toggleLogging(clientURI);
			clientURIs.add(clientURI);
		}
	}

	/**
	 * Creates the Sensor components.
	 */
	private void createSensorComponents() throws Exception {
		for (int i = 0; i < Config.NBNODE; i++) {
			SensorConfig configNode = new SensorConfig(CVMUtils.getPanel(), URIRegisterInboundPortURINode,
					Config.TEST_CLOCK_URI, CVMUtils.getNodeSensors().get(i), i + 1, CVMUtils.getPositions().get(i), 5,
					1.5);

			String sensorURI = ComponentFactory.createSensor(configNode);

			this.toggleTracing(sensorURI);
			this.toggleLogging(sensorURI);
			sensorURIs.add(sensorURI);
		}
	}

	/**
	 * The main entry point to start the component virtual machine, creating an
	 * instance of CVM.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		try {
			CVM a = new CVM();
			a.startStandardLifeCycle(20000L);
			Thread.sleep(5000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}