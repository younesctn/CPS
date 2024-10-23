package withplugin.ports;

import app.components.Sensor;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestResultCI;
import withplugin.plugins.ClientPlugin;

public class URIClientInboundPortForNodeForPlugin extends AbstractInboundPort implements RequestResultCI {
	private static final long serialVersionUID = 1L;

	public URIClientInboundPortForNodeForPlugin(String uri, ComponentI owner, String pluginURI) throws Exception {
		super(uri, RequestResultCI.class, owner, pluginURI, null);

		assert uri != null && owner instanceof Sensor;
	}

	public URIClientInboundPortForNodeForPlugin(ComponentI owner, String pluginURI) throws Exception {
		super(RequestResultCI.class, owner, pluginURI, null);
//		assert	owner instanceof RequestResultCI ;
	}

	@Override
	public void acceptRequestResult(String requestURI, QueryResultI result) throws Exception {
		this.owner.runTask(new AbstractComponent.AbstractTask(this.getPluginURI()) {
			@Override
			public void run() {
				try {
					((ClientPlugin) this.getTaskProviderReference()).acceptRequestResult(requestURI, result);
				} catch (Exception e) {
					e.printStackTrace();
					;
				}
			}
		});
	}

}
