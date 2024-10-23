package withplugin.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.helpers.TracerI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import withplugin.plugins.ClientPlugin;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.models.ClientConfig;

/**
 * Represents a client component in a sensor network that handles request execution
 * and connection management with sensor nodes using configurable settings.
 * This component utilizes a plugin architecture to extend its functionality,
 * specifically through the {@link ClientPlugin}. The client manages tasks such as
 * scheduling requests and connecting to nodes based on a configuration provided upon initialization.
 * 
 * The client component also interacts with a clock server to manage time-sensitive tasks,
 * ensuring that operations are synchronized with the clock's configuration.
 * 
 * @author Malek Bouzarkouna, Younes Chetouani, Mohamed Amine Zemali
 * @version 1.0
 * @see fr.sorbonne_u.components.AbstractComponent
 */
public class Client extends AbstractComponent {
	// -------------------------------------------------------------------------
	// Component variables and constants
	// -------------------------------------------------------------------------

	public static final String CLIENT_PLUGIN_URI = "client-plugin-uri";
	private final String TEST_CLOCK_URI; 
	private boolean isRequestAsync;
	private List<RequestI> requests;

	// -------------------------------------------------------------------------
	// Component variables
	// -------------------------------------------------------------------------
	private String requestNodeName;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	 /**
     * Constructs a new Client component with the specified configuration.
     * Initializes the component with specific tracer positions and installs a client plugin
     * to manage sensor network interactions.
     *
     * @param configClient The configuration settings for the client, including names, URIs, and request details.
     * @throws Exception If an error occurs during component or plugin initialization.
     */
	protected Client(ClientConfig configClient) throws Exception {

		super(5, 10);
		this.requestNodeName = configClient.getRequestNodeName();
		this.TEST_CLOCK_URI = configClient.getUriClock();
		this.requests = configClient.getRequests();
		this.isRequestAsync = configClient.getIsRequestAsync();

		TracerI tracer = this.getTracer();
		tracer.setRelativePosition(configClient.getName() % 3, configClient.getName() / 3);
		tracer.setTitle("Client "+configClient.getName());

		ClientPlugin plugin = new ClientPlugin(configClient);
		plugin.setPluginURI(CLIENT_PLUGIN_URI);
		this.installPlugin(plugin);
		assert this.isInstalled(CLIENT_PLUGIN_URI);
		assert this.getPlugin(CLIENT_PLUGIN_URI) == plugin;

	}
	
	 /**
     * Executes the main task of the client component, which involves time synchronization,
     * node connection, and request scheduling. This method retrieves the clock settings from the
     * clock server, waits for the start signal from the clock, and then schedules a task to
     * execute requests after a specified delay.
     *
     * @throws Exception If an error occurs during execution, such as in connecting to nodes or scheduling tasks.
     */
	@Override
	public void execute() throws Exception {
		ClientPlugin plugin = (ClientPlugin) this.getPlugin(CLIENT_PLUGIN_URI);
		ClocksServerOutboundPort outBoundPortClock = plugin.getRegisterReference();
		AcceleratedClock ac = outBoundPortClock.getClock(TEST_CLOCK_URI);
		ac.waitUntilStart();
		Instant start = ac.getStartInstant();
		Instant i2 = start.plusSeconds(120);
		long delay = ac.nanoDelayUntilInstant(i2);

		this.scheduleTask(o -> {
			try {
				
				plugin.requestNodeAndConnectByName(this.requestNodeName);
				Instant i3 = i2.plusSeconds(60);
				plugin.scheduleTasks(requests, i3, ac, 120, isRequestAsync);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, delay, TimeUnit.NANOSECONDS);
	}
}
