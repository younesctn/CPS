package app.gui;

import javax.swing.*;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// Panneau personnalisé pour le dessin

import javax.swing.JPanel;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Custom JPanel class that provides functionality for drawing and animating a
 * network visualization, including nodes, connections, and light animations. It
 * implements the {@link GraphicalNetworkInterface} to provide a unified
 * interface for managing graphical elements in a network diagram.
 */
public class NetworkPanel extends JPanel implements GraphicalNetworkInterface {
	private static final long serialVersionUID = 1L;
	private static final int PREF_WIDTH = 1000;
	private static final int PREF_HEIGHT = 800;
	private final List<Node> nodes = new CopyOnWriteArrayList<>();
	private final List<Connection> connections = new CopyOnWriteArrayList<>();
	private List<Light> lights = new CopyOnWriteArrayList<>();

	private Timer animationTimer;
	private int gridSize = 100;
	private AtomicBoolean isOn = new AtomicBoolean(true);

	/**
	 * Constructs a NetworkPanel with predefined dimensions and initializes a
	 * blinking timer for node animation effects. The panel is set with a preferred
	 * size and starts a timer that toggles the blinking state of nodes, triggering
	 * repaintSafelys at regular intervals.
	 */
	public NetworkPanel() {
		setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		javax.swing.Timer blinkTimer = new javax.swing.Timer(500, e -> {
			isOn.set(!isOn.get());
			repaintSafely();
		});
		blinkTimer.start();
	}

	/**
	 * Finds a node by its name among the nodes currently managed by the panel.
	 * 
	 * @param name The name of the node to find.
	 * @return The {@link Node} object if found, otherwise {@code null}.
	 */
	public Node findNodeByName(String name) {
		for (Node node : nodes) {
			if (node.name.equals(name)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Starts an animation of a light moving between two nodes.
	 * 
	 * @param start The starting node for the light animation.
	 * @param end   The ending node for the light animation.
	 */
	public void startLightAnimation(Node start, Node end) {
		Point startPoint = new Point(start.x + Node.DIAMETER / 2, start.y + Node.DIAMETER / 2);
		Point endPoint = new Point(end.x + Node.DIAMETER / 2, end.y + Node.DIAMETER / 2);
		synchronized (this) { // Synchronize access if needed
			lights.add(new Light(startPoint, endPoint));
		}

		if (animationTimer == null || !animationTimer.isRunning()) {
			animationTimer = new javax.swing.Timer(10, e -> {
				synchronized (this) { // Ensure atomic updates
					lights.removeIf(light -> !light.move());
				}
				repaintSafely();
				if (lights.isEmpty()) {
					animationTimer.stop();
				}
			});
			animationTimer.start();
		}
	}

	/**
	 * Draws a grid background with dashed lines, enhancing the visibility of node
	 * positioning within the panel.
	 * 
	 * @param g2d The Graphics2D context used for drawing.
	 */
	private void drawGrid(Graphics2D g2d) {
		int width = getWidth();
		int height = getHeight();

		g2d.setColor(Color.BLACK);
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2d.setStroke(dashed);

		for (int i = gridSize; i < height; i += gridSize) {
			g2d.drawLine(0, i, width, i);
			g2d.drawString(String.valueOf((height - i) / gridSize), 5,
					i + (g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent()) / 2);
		}

		for (int i = gridSize; i < width; i += gridSize) {
			g2d.drawLine(i, 0, i, height);
			g2d.drawString(String.valueOf(i / gridSize),
					i - g2d.getFontMetrics().stringWidth(String.valueOf(i / gridSize)) / 2, height - 5);
		}
	}

	/**
	 * Paints all components of the network including nodes, connections, and
	 * lights. This method is overridden to ensure custom drawing of all graphical
	 * elements.
	 * 
	 * @param g The Graphics object provided by the system.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();

		drawGrid(g2d);

		for (Connection conn : connections) {
			conn.draw(g2d);
		}
		for (Node node : nodes) {
			node.draw(g2d, isOn);
		}

		for (Light light : lights) {
			light.draw(g2d);
		}

		g2d.dispose();
	}

	/**
	 * Adds a node to the network visualization at specified grid coordinates,
	 * converting these coordinates into pixel positions for graphical display. The
	 * node is centered based on its diameter to ensure precise placement on the
	 * grid.
	 *
	 * @param name  The unique name of the node.
	 * @param gridX The x-coordinate on the grid where the node should be placed.
	 * @param gridY The y-coordinate on the grid where the node should be placed.
	 */
	public void addNode(String name, double gridX, double gridY) {
		int pixelX = (int) (gridX * gridSize); // Convert grid coordinates to pixel for x-axis
		int pixelY = (int) (getHeight() - gridY * gridSize); // Convert grid coordinates to pixel for y-axis, inversing
																// to match graphical layout
		nodes.add(new Node(name, pixelX - (Node.DIAMETER / 2), pixelY - (Node.DIAMETER / 2))); // Center the node by
																								// adjusting for
																								// diameter
		repaintSafely(); // Redraw the panel to display the new node
	}

	/**
	 * Creates a connection between two nodes identified by their names. If both
	 * nodes exist, a new connection is established and the graphical representation
	 * is updated.
	 *
	 * @param startName The name of the starting node of the connection.
	 * @param endName   The name of the ending node of the connection.
	 */
	public void addConnection(String startName, String endName) {
		Node startNode = findNodeByName(startName);
		Node endNode = findNodeByName(endName);
		if (startNode != null && endNode != null) {
			connections.add(new Connection(startNode, endNode));
			repaintSafely(); // Redraw to update the graphical display with the new connection
		}
	}

	/**
	 * Implements the {@link GraphicalNetworkInterface} to add a graphical node at
	 * specified coordinates. This method is a direct interface for the internal
	 * `addNode` method, facilitating the addition of nodes through graphical
	 * interactions.
	 *
	 * @param name The name of the node to add.
	 * @param x    The x-coordinate on the grid for the node.
	 * @param y    The y-coordinate on the grid for the node.
	 */
	@Override
	public void addGraphicalNode(String name, double x, double y) {
		this.addNode(name, x, y);
	}

	/**
	 * Implements the {@link GraphicalNetworkInterface} to create a connection
	 * between two nodes based on their names. This method provides an accessible
	 * interface for adding connections in a graphical context.
	 *
	 * @param startName The name of the node where the connection starts.
	 * @param endName   The name of the node where the connection ends.
	 */
	@Override
	public void addGraphicalConnection(String startName, String endName) {
		this.addConnection(startName, endName);
	}

	/**
	 * Removes a graphical connection between two nodes identified by their names.
	 * The connection, if found, is removed from the graphical display, and the
	 * panel is repaintSafelyed to reflect this change.
	 *
	 * @param startName The name of the starting node of the connection to be
	 *                  removed.
	 * @param endName   The name of the ending node of the connection to be removed.
	 */
	public void removeGraphicalConnection(String startName, String endName) {
		for (Iterator<Connection> iterator = connections.iterator(); iterator.hasNext();) {
			Connection conn = iterator.next();
			if (conn.start.name.equals(startName) && conn.end.name.equals(endName)) {
				iterator.remove();
				repaintSafely();
				break;
			}
		}
	}

	/**
	 * Initiates a graphical animation of a light moving between two nodes specified
	 * by their names. This method checks for the existence of both start and end
	 * nodes and starts the animation if both are found.
	 *
	 * @param startName The name of the starting node for the animation.
	 * @param endName   The name of the ending node for the animation.
	 */
	@Override
	public void startGraphicalLightAnimation(String startName, String endName) {
		Node startNode = this.findNodeByName(startName);
		Node endNode = this.findNodeByName(endName);
		if (startNode != null && endNode != null) {
			this.startLightAnimation(startNode, endNode);
		}
	}

	/**
	 * Toggles the blinking state of a node identified by its name to 'true',
	 * indicating it should start blinking. This method finds the node and if it
	 * exists, toggles its blinking state.
	 *
	 * @param nodeName The name of the node whose blinking state should be toggled.
	 */
	public void toggleNodeBlinking(String nodeName) {
		Node node = findNodeByName(nodeName);
		if (node != null) {
			node.toggleBlinking(true); // Enable blinking for the node
		}
	}

	/**
	 * Resets the blinking state of all nodes in the panel to 'false', stopping any
	 * blinking. This method iterates through all nodes and disables their blinking
	 * state.
	 */
	public void resetNodesBlink() {
		for (Node node : nodes) {
			node.toggleBlinking(false); // Disable blinking for all nodes
		}
	}

	public void repaintSafely() {
		SwingUtilities.invokeLater(this::repaint);
	}

}