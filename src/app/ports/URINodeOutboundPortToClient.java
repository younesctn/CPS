package app.ports;

import app.components.Sensor;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestResultCI;

public class URINodeOutboundPortToClient extends AbstractOutboundPort implements RequestResultCI {
	private static final long serialVersionUID = 1L;

	public URINodeOutboundPortToClient(String uri, ComponentI owner) throws Exception {
		super(uri, RequestResultCI.class, owner);

		assert uri != null && owner instanceof Sensor;
	}

	public URINodeOutboundPortToClient(ComponentI owner) throws Exception {
		super(RequestResultCI.class, owner);
//		assert owner instanceof RequestResultCI;
	}

	@Override
	public void acceptRequestResult(String requestURI, QueryResultI result) throws Exception {
		((RequestResultCI) this.getConnector()).acceptRequestResult(requestURI, result);
	}

}
