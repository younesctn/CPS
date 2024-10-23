package app.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.nodes.interfaces.RequestingCI;

public class URIClientOutBoundPortToNode extends AbstractOutboundPort implements RequestingCI {
	private static final long serialVersionUID = 1L;

	public URIClientOutBoundPortToNode(String uri, ComponentI owner) throws Exception {
		super(uri, RequestingCI.class, owner);
		assert uri != null && owner != null;
	}

	public URIClientOutBoundPortToNode(ComponentI owner) throws Exception {
		super(RequestingCI.class, owner);

//		assert owner instanceof RequestingCI ;
	}

	@Override
	public QueryResultI execute(RequestI request) throws Exception {
		return ((RequestingCI) this.getConnector()).execute(request);
	}

	@Override
	public void executeAsync(RequestI request) throws Exception {
		((RequestingCI) this.getConnector()).executeAsync(request);

	}

}
