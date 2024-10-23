package app.ports;

import java.util.Set;

import app.components.Register;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.registry.interfaces.RegistrationCI;

public class URIRegisterInboundPortForNode extends AbstractInboundPort implements RegistrationCI {
	private static final long serialVersionUID = 1L;

	public URIRegisterInboundPortForNode(String uri, ComponentI owner, String executorServiceURI) throws Exception {
		super(uri, RegistrationCI.class, owner, null, executorServiceURI);

		assert uri != null && owner instanceof Register;
	}

	public URIRegisterInboundPortForNode(ComponentI owner, String executorServiceURI) throws Exception {
		super(RegistrationCI.class, owner, null, executorServiceURI);
		assert owner instanceof RegistrationCI;
	}

	@Override
	public boolean registered(String nodeIdentifier) throws Exception {
		return this.getOwner().handleRequest(owner -> ((Register) owner).registered(nodeIdentifier));
	}

	@Override
	public Set<NodeInfoI> register(NodeInfoI nodeInfo) throws Exception {
		return this.getOwner().handleRequest(this.getExecutorServiceIndex(),
				owner -> ((Register) owner).register(nodeInfo));
	}

	@Override
	public NodeInfoI findNewNeighbour(NodeInfoI nodeInfo, Direction d) throws Exception {
		return this.getOwner().handleRequest(owner -> ((Register) owner).findNewNeighbour(nodeInfo, d));
	}

	@Override
	public void unregister(String nodeIdentifier) throws Exception {
		this.getOwner().handleRequest(owner -> {
			((Register) owner).unregister(nodeIdentifier);
			return null;
		});
	}
}
