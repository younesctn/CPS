package app.ports;

import java.util.Set;

import app.components.Register;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.ConnectionInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.GeographicalZoneI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.LookupCI;

public class URIRegisterInboundPortForClient extends AbstractInboundPort implements LookupCI {
	private static final long serialVersionUID = 1L;

	public URIRegisterInboundPortForClient(String uri, ComponentI owner) throws Exception {
		super(uri, LookupCI.class, owner);

		assert uri != null && owner instanceof Register;
	}

	public URIRegisterInboundPortForClient(ComponentI owner) throws Exception {
		super(LookupCI.class, owner);
		assert owner instanceof LookupCI;
	}

	@Override
	public ConnectionInfoI findByIdentifier(String sensorNodeId) throws Exception {
		return this.getOwner().handleRequest(owner -> ((Register) owner).findByIdentifier(sensorNodeId));
	}

	@Override
	public Set<ConnectionInfoI> findByZone(GeographicalZoneI z) throws Exception {
		return this.getOwner().handleRequest(owner -> ((Register) owner).findByZone(z));
	}
}
