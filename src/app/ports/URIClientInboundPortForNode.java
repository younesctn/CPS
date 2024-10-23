package app.ports;

import app.components.Client;
import app.components.Sensor;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestResultCI;

public class URIClientInboundPortForNode extends AbstractInboundPort implements RequestResultCI {
	private static final long serialVersionUID = 1L;

	public URIClientInboundPortForNode(String uri, ComponentI owner) throws Exception {
		super(uri, RequestResultCI.class, owner);

		assert uri != null && owner instanceof Sensor;
	}

	public URIClientInboundPortForNode(ComponentI owner) throws Exception {
		super(RequestResultCI.class, owner);
//		assert owner instanceof RequestResultCI ;
	}

	@Override
	public void acceptRequestResult(String requestURI, QueryResultI result) throws Exception {
		this.owner.runTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					((Client) this.getTaskOwner()).acceptRequestResult(requestURI, result);
				} catch (Exception e) {
					e.printStackTrace();
					;
				}
			}
		});
	}

}
