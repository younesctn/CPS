package app.factory;

import fr.sorbonne_u.components.AbstractComponent;
import withplugin.components.Client;
import withplugin.components.Sensor;
import app.components.Register;
import app.models.ClientConfig;
import app.models.SensorConfig;

/**
 * A factory class for creating components within the sensor network system.
 * This class provides static methods to create instances of Register, Client,
 * and Sensor components, encapsulating the component creation complexity and
 * providing a straightforward interface for system setup.
 *
 * <p>
 * Each method in this class utilizes the
 * {@link AbstractComponent#createComponent} method from BCM4Java framework,
 * simplifying the instantiation of components by handling the necessary
 * configuration and initialization details internally.
 * </p>
 */
public class ComponentFactory {

	/**
	 * Creates a new Register component with specified inbound ports. This method
	 * initializes a Register component, which acts as a central registry for nodes
	 * and clients in the network, facilitating their interaction.
	 *
	 * @param uriInboundPortNode   The URI for the inbound port to receive
	 *                             connections from nodes.
	 * @param uriInboundPortClient The URI for the inbound port to receive
	 *                             connections from clients.
	 * @return The unique identifier of the created Register component.
	 * @throws Exception if there is an error during the component creation process.
	 */
	public static String createRegister(String uriInboundPortNode, String uriInboundPortClient) throws Exception {
		return AbstractComponent.createComponent(Register.class.getCanonicalName(),
				new Object[] { uriInboundPortNode, uriInboundPortClient });
	}

	/**
	 * Creates a new Client component based on the provided configuration. This
	 * method initializes a Client component configured to perform operations as
	 * specified in the {@link ClientConfig}. The Client is responsible for sending
	 * requests and handling responses within the sensor network.
	 *
	 * @param config The configuration object containing setup data for the Client.
	 * @return The unique identifier of the created Client component.
	 * @throws Exception if there is an error during the component creation process.
	 */
	public static String createClient(ClientConfig config) throws Exception {
		return AbstractComponent.createComponent(Client.class.getCanonicalName(), new Object[] { config });
	}

	/**
	 * Creates a new Sensor component based on the provided configuration. This
	 * method initializes a Sensor component, which represents a single sensor node
	 * in the network, capable of gathering and sending sensor data. It is
	 * configured according to the {@link SensorConfig}.
	 *
	 * @param config The configuration object containing setup data for the Sensor.
	 * @return The unique identifier of the created Sensor component.
	 * @throws Exception if there is an error during the component creation process.
	 */
	public static String createSensor(SensorConfig config) throws Exception {
		return AbstractComponent.createComponent(Sensor.class.getCanonicalName(), new Object[] { config });
	}
}
