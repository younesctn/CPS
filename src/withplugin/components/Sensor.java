package withplugin.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.helpers.TracerI;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import withplugin.plugins.SensorPlugin;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import app.models.SensorConfig;

/**
 * The {@code Sensor} class represents a network sensor node within a
 * distributed sensor network. This component utilizes a plugin architecture to extend its functionality,
 * specifically through the {@link SensorPlugin}. It handles tasks such as registering with a
 * registry, managing connections to other nodes, and processing sensor data
 * queries.
 * <p>
 * The sensor node operates within an accelerated time context provided 
 * by a clock server and interacts with other nodes in the network 
 * to perform distributed queries and data aggregation.
 * </p>
 *
 * @author Malek Bouzarkouna, Younes Chetouani, Mohamed Amine Zemali
 * @version 1.0
 * @see fr.sorbonne_u.components.AbstractComponent
 */
public class Sensor extends AbstractComponent {
	// -------------------------------------------------------------------------
	// Component variables and constants
	// -------------------------------------------------------------------------

	public static final String Sensor_PLUGIN_URI = "Sensor-plugin-uri";
	private final String TEST_CLOCK_URI;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected Sensor(SensorConfig config) throws Exception {
		super(config.getNbthread(), 1);
		int index = config.getName() - 1;
		TracerI tracer = this.getTracer();
		tracer.setRelativePosition(index % 3, (index / 3) + 1);
		tracer.setTitle("Node n°"+config.getName());
		this.TEST_CLOCK_URI = config.getUriClock();
	

		SensorPlugin plugin = new SensorPlugin(config);
		plugin.setPluginURI(Sensor_PLUGIN_URI);
		this.installPlugin(plugin);

		assert this.isInstalled(Sensor_PLUGIN_URI);
		assert this.getPlugin(Sensor_PLUGIN_URI) == plugin;
	}

	@Override
	public void execute() throws Exception {
		SensorPlugin plugin = (SensorPlugin) this.getPlugin(Sensor_PLUGIN_URI);
		ClocksServerOutboundPort outBoundPortClock = plugin.getRegisterReference();
		AcceleratedClock ac = outBoundPortClock.getClock(TEST_CLOCK_URI);
		ac.waitUntilStart();
		Instant i0 = ac.getStartInstant();
		Instant i1 = i0.plusSeconds(60);

		long delay = ac.nanoDelayUntilInstant(i1); // delay (ns)

		this.scheduleTask(o -> {
			try {
				plugin.registerAndConnectToNeighbor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, delay, TimeUnit.NANOSECONDS);
	}

}

// -----------------------------------------------------------------------------
