package app.ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.GeographicalZoneI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.LookupCI;

public class URIClientOutBoundPortToRegister extends AbstractOutboundPort implements LookupCI {
	private static final long serialVersionUID = 1L;

	public URIClientOutBoundPortToRegister(String uri, ComponentI owner) throws Exception {
		super(uri, LookupCI.class, owner);
		assert uri != null && owner != null;
	}

	public URIClientOutBoundPortToRegister(ComponentI owner) throws Exception {
		super(LookupCI.class, owner);

//		assert owner instanceof LookupCI ;
	}

	@Override
	public ConnectionInfoI findByIdentifier(String sensorNodeId) throws Exception {
		return ((LookupCI) this.getConnector()).findByIdentifier(sensorNodeId);
	}

	@Override
	public Set<ConnectionInfoI> findByZone(GeographicalZoneI z) throws Exception {
		return ((LookupCI) this.getConnector()).findByZone(z);
	}

}
