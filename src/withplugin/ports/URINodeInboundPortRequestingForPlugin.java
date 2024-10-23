package withplugin.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.nodes.interfaces.RequestingCI;
import withplugin.plugins.SensorPlugin;

public class URINodeInboundPortRequestingForPlugin extends AbstractInboundPort implements RequestingCI {
	private static final long serialVersionUID = 1L;

	public URINodeInboundPortRequestingForPlugin(String uri, ComponentI owner, String pluginURI,
			String executorServiceURI) throws Exception {
		super(uri, RequestingCI.class, owner, pluginURI, executorServiceURI);

		assert uri != null && owner instanceof SensorPlugin;
	}

	public URINodeInboundPortRequestingForPlugin(ComponentI owner, String pluginURI, String executorServiceURI)
			throws Exception {
		super(RequestingCI.class, owner, pluginURI, executorServiceURI);
//		assert	owner instanceof RequestingCI ;
	}

	@Override
	public QueryResultI execute(RequestI request) throws Exception {
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<QueryResultI>(this.getPluginURI()) {
			@Override
			public QueryResultI call() throws Exception {
				return ((SensorPlugin) this.getServiceProviderReference()).execute(request);
			}
		});
	}

	@Override
	public void executeAsync(RequestI request) throws Exception {
		this.owner.runTask(this.getExecutorServiceIndex(), new AbstractComponent.AbstractTask(this.getPluginURI()) {
			@Override
			public void run() {
				try {
					((SensorPlugin) this.getTaskProviderReference()).executeAsync(request);
				} catch (Exception e) {
					e.printStackTrace();
					;
				}
			}
		});
	}
}
