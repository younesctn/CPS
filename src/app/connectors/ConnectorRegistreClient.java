package app.connectors;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.GeographicalZoneI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.LookupCI;

/**
 * A connector class that implements the {@link LookupCI} interface to
 * facilitate the lookup operations between client components and sensor network
 * registry components. This connector provides methods to find connection
 * information for sensor nodes either by their identifier or by their
 * geographical location.
 *
 * This class extends {@link AbstractConnector}, providing concrete
 * implementations for the lookup methods defined in the {@link LookupCI}
 * interface.
 */
public class ConnectorRegistreClient extends AbstractConnector implements LookupCI {

	/**
	 * Retrieves the connection information for a sensor node based on its
	 * identifier. This method forwards the query to the connected registry
	 * component which performs the actual lookup based on the node identifier
	 * provided.
	 *
	 * @param sensorNodeId The unique identifier of the sensor node for which
	 *                     connection information is requested.
	 * @return The {@link ConnectionInfoI} of the sensor node if found, otherwise
	 *         {@code null}.
	 * @throws Exception if there is an issue in retrieving the information.
	 */
	@Override
	public ConnectionInfoI findByIdentifier(String sensorNodeId) throws Exception {
		return ((LookupCI) this.offering).findByIdentifier(sensorNodeId);
	}

	/**
	 * Retrieves a set of connection information for all sensor nodes located within
	 * a specified geographical zone. This method forwards the geographical query to
	 * the connected registry component which performs the search and returns a set
	 * of connection information for nodes within the specified zone.
	 *
	 * @param z The {@link GeographicalZoneI} defining the geographical area for the
	 *          lookup.
	 * @return A set of {@link ConnectionInfoI} representing the sensor nodes within
	 *         the specified geographical zone.
	 * @throws Exception if there is an issue in performing the lookup.
	 */
	@Override
	public Set<ConnectionInfoI> findByZone(GeographicalZoneI z) throws Exception {
		return ((LookupCI) this.offering).findByZone(z);
	}
}
