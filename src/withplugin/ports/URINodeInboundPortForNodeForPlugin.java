package withplugin.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.cps.sensor_network.interfaces.NodeInfoI;
import fr.sorbonne_u.cps.sensor_network.interfaces.QueryResultI;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestContinuationI;
import fr.sorbonne_u.cps.sensor_network.network.interfaces.SensorNodeP2PCI;
import withplugin.plugins.SensorPlugin;

public class URINodeInboundPortForNodeForPlugin extends AbstractInboundPort implements SensorNodeP2PCI {
	private static final long serialVersionUID = 1L;

	public URINodeInboundPortForNodeForPlugin(String uri, ComponentI owner, String pluginURI, String executorServiceURI)
			throws Exception {
		super(uri, SensorNodeP2PCI.class, owner, pluginURI, executorServiceURI);

		assert uri != null && owner instanceof SensorPlugin;
	}

	public URINodeInboundPortForNodeForPlugin(ComponentI owner, String pluginURI, String executorServiceURI)
			throws Exception {
		super(SensorNodeP2PCI.class, owner, pluginURI, executorServiceURI);
//		assert owner instanceof SensorPlugin ;
	}

	@Override
	public void ask4Disconnection(NodeInfoI neighbour) throws Exception {
		this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(this.getPluginURI()) {
			@Override
			public Void call() throws Exception {
				((SensorPlugin) this.getServiceProviderReference()).ask4Disconnection(neighbour);
				return null;
			}
		});
	}

	@Override
	public void ask4Connection(NodeInfoI newNeighbour) throws Exception {
		this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(this.getPluginURI()) {
			@Override
			public Void call() throws Exception {
				((SensorPlugin) this.getServiceProviderReference()).ask4Connection(newNeighbour);
				return null;
			}
		});
	}

	@Override
	public QueryResultI execute(RequestContinuationI requestContinuation) throws Exception {
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<QueryResultI>(this.getPluginURI()) {
			@Override
			public QueryResultI call() throws Exception {
				return ((SensorPlugin) this.getServiceProviderReference()).execute(requestContinuation);
			}
		});
	}

	@Override
	public void executeAsync(RequestContinuationI requestContinuation) throws Exception {
		this.owner.runTask(this.getExecutorServiceIndex(), new AbstractComponent.AbstractTask(this.getPluginURI()) {
			@Override
			public void run() {
				try {
					((SensorPlugin) this.getTaskProviderReference()).executeAsync(requestContinuation);
				} catch (Exception e) {
					e.printStackTrace();
					;
				}
			}
		});
	}

}
