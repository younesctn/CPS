package app.gui;

/**
 * Interface defining the operations for managing graphical representations of a
 * network. This includes adding and removing nodes and connections, and
 * managing animations.
 */
public interface GraphicalNetworkInterface {
	void addGraphicalNode(String name, double x, double y);

	void addGraphicalConnection(String startName, String endName);

	void removeGraphicalConnection(String startName, String endName);

	void startGraphicalLightAnimation(String startName, String endName);

	void toggleNodeBlinking(String nodeIdentifier);

	void resetNodesBlink();
}
