package app.ports;

import app.components.Sensor;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.nodes.interfaces.RequestingCI;

public class URINodeInboundPortForClient extends AbstractInboundPort implements RequestingCI {
	private static final long serialVersionUID = 1L;

	public URINodeInboundPortForClient(String uri, ComponentI owner) throws Exception {
		super(uri, RequestingCI.class, owner);

		assert uri != null && owner instanceof Sensor;
	}

	public URINodeInboundPortForClient(ComponentI owner, String executorServiceURI) throws Exception {
		super(RequestingCI.class, owner, null, executorServiceURI);

		assert executorServiceURI != null && owner instanceof Sensor;
	}

	public URINodeInboundPortForClient(ComponentI owner) throws Exception {
		super(RequestingCI.class, owner);
		assert owner instanceof RequestingCI;
	}

	@Override
	public QueryResultI execute(RequestI request) throws Exception {
		return this.getOwner().handleRequest(owner -> ((Sensor) owner).execute(request));
	}

	@Override
	public void executeAsync(RequestI request) throws Exception {

		this.owner.runTask(this.getExecutorServiceIndex(), new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					((Sensor) this.getTaskOwner()).executeAsync(request);
				} catch (Exception e) {
					e.printStackTrace();
					;
				}
			}
		});

	}
}
