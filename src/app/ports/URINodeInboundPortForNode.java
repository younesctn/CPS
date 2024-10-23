package app.ports;

import app.components.Sensor;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestContinuationI;
import fr.sorbonne_u.cps.sensor_network.network.interfaces.SensorNodeP2PCI;

public class URINodeInboundPortForNode extends AbstractInboundPort implements SensorNodeP2PCI {
	private static final long serialVersionUID = 1L;

	public URINodeInboundPortForNode(String uri, ComponentI owner) throws Exception {
		super(uri, SensorNodeP2PCI.class, owner);

		assert uri != null && owner instanceof Sensor;
	}

	public URINodeInboundPortForNode(ComponentI owner) throws Exception {
		super(SensorNodeP2PCI.class, owner);
		assert owner instanceof SensorNodeP2PCI;
	}

	public URINodeInboundPortForNode(ComponentI owner, String executorServiceURI) throws Exception {
		super(SensorNodeP2PCI.class, owner, null, executorServiceURI);

		assert executorServiceURI != null && owner instanceof Sensor;
	}

	@Override
	public void ask4Disconnection(NodeInfoI neighbour) throws Exception {
		this.getOwner().handleRequest(owner -> {
			((Sensor) owner).ask4Disconnection(neighbour);
			return null;
		});
	}

	@Override
	public void ask4Connection(NodeInfoI newNeighbour) throws Exception {
		this.getOwner().handleRequest(owner -> {
			((Sensor) owner).ask4Connection(newNeighbour);
			return null;
		});
	}

	@Override
	public QueryResultI execute(RequestContinuationI request) throws Exception {
		return this.getOwner().handleRequest(owner -> ((Sensor) owner).execute(request));
	}

	@Override
	public void executeAsync(RequestContinuationI requestContinuation) throws Exception {
		this.owner.runTask(this.getExecutorServiceIndex(), new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					((Sensor) this.getTaskOwner()).executeAsync(requestContinuation);
				} catch (Exception e) {
					e.printStackTrace();
					;
				}
			}
		});

	}

}
