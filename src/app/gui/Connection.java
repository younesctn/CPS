package app.gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Represents a graphical connection between two nodes in a network
 * visualization. This class manages the graphical representation and drawing of
 * a line between two nodes.
 */
class Connection {
	Node start;
	Node end;

	/**
	 * Constructs a new Connection instance between two nodes.
	 * 
	 * @param start The starting node of the connection.
	 * @param end   The ending node of the connection.
	 */
	public Connection(Node start, Node end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Draws the connection as a line on the specified Graphics object.
	 * 
	 * @param g The Graphics object on which the connection will be drawn.
	 */
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke(new BasicStroke(3)); // Set stroke width to 3
		g2.drawLine(start.x + 25, start.y + 25, end.x + 25, end.y + 25);
		g2.dispose(); // Dispose of the graphics copy after modifications
	}
}
