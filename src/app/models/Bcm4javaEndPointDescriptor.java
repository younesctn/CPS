package app.models;

import java.util.HashSet;
import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.cps.sensor_network.interfaces.BCM4JavaEndPointDescriptorI;

/**
 * Provides an implementation of the BCM4JavaEndPointDescriptorI interface,
 * representing an endpoint descriptor for BCM4Java components. This class
 * encapsulates details about the inbound port URI and the interfaces offered by
 * a component.
 */
public class Bcm4javaEndPointDescriptor implements BCM4JavaEndPointDescriptorI {

	private static final long serialVersionUID = 1L;
	private String inboundPortURI;
	private Set<Class<? extends OfferedCI>> offeredInterfaces;

	/**
	 * Constructs a new endpoint descriptor with a specified inbound port URI.
	 *
	 * @param inboundPortURI the URI of the inbound port associated with the
	 *                       endpoint.
	 */
	public Bcm4javaEndPointDescriptor(String inboundPortURI) {
		this.inboundPortURI = inboundPortURI;
		this.offeredInterfaces = new HashSet<>();
	}

	/**
	 * Returns the inbound port URI associated with this endpoint.
	 *
	 * @return the inbound port URI as a string.
	 */
	@Override
	public String getInboundPortURI() {
		return this.inboundPortURI;
	}

	/**
	 * Checks if a specific interface is offered by this endpoint.
	 *
	 * @param inter the class of the interface to check.
	 * @return true if the interface is offered, false otherwise.
	 */
	@Override
	public boolean isOfferedInterface(Class<? extends OfferedCI> inter) {
		return this.offeredInterfaces.contains(inter);
	}
}
