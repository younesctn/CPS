package app.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a visual node in a graphical network interface. This class
 * encapsulates the properties of a node, such as its name and position, and
 * provides functionality to draw itself on a graphical context. It also
 * supports blinking functionality to highlight the node dynamically.
 */
class Node {
	String name; // The name of the node, displayed in the visual representation
	int x, y; // The x and y coordinates of the node on the panel
	static final int DIAMETER = 50; // The diameter of the node for drawing
	boolean isBlinking = false; // Flag to manage the blinking state of the node

	/**
	 * Constructs a new Node with specified name and position.
	 *
	 * @param name The name of the node, which will be displayed visually.
	 * @param x    The x-coordinate of the node on the display.
	 * @param y    The y-coordinate of the node on the display.
	 */
	public Node(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * Toggles the blinking state of the node.
	 *
	 * @param blink If true, the node will start blinking; if false, it will stop.
	 */
	public void toggleBlinking(boolean blink) {
		isBlinking = blink;
	}

	/**
	 * Draws the node on the provided Graphics context. The node is represented as a
	 * circle with its name centered. The fill color changes based on its blinking
	 * state.
	 *
	 * @param g    The Graphics object on which the node is drawn.
	 * @param isOn Indicates whether the blinking effect should show as 'on' or
	 *             'off'. This should be controlled by an external timer or similar
	 *             mechanism to create a blinking effect.
	 */
	public void draw(Graphics g, AtomicBoolean isOn) {
		g.setColor(Color.BLACK); // Set color for outer circle
		g.fillOval(x, y, DIAMETER, DIAMETER); // Draw the outer circle

		// Change fill color based on blinking state and whether it is the 'on' phase
		if (isBlinking && isOn.get()) {
			g.setColor(Color.RED); // Blinking color
		} else {
			g.setColor(Color.WHITE); // Default color
		}
		g.fillOval(x + 1, y + 1, DIAMETER - 2, DIAMETER - 2); // Draw the inner circle

		// Center the name inside the node
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(name);
		int textHeight = fm.getHeight();
		g.setColor(Color.BLACK); // Set color for text
		g.drawString(name, x + (DIAMETER - textWidth) / 2, y + ((DIAMETER - textHeight) / 2) + fm.getAscent());
	}
}
