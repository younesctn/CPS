package app.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Represents a graphical light animation between two points in a network
 * visualization. This class manages the movement and drawing of a light-like
 * graphic.
 */
class Light {
	Point position;
	Point start;
	Point end;
	final int radius = 10;
	Color color = Color.YELLOW;

	/**
	 * Constructs a new Light instance with specified start and end points.
	 * 
	 * @param start The starting point of the light animation.
	 * @param end   The ending point of the light animation.
	 */
	public Light(Point start, Point end) {
		this.position = new Point(start);
		this.start = start;
		this.end = end;
	}

	/**
	 * Draws the light at its current position on the specified Graphics object.
	 * 
	 * @param g The Graphics object on which the light will be drawn.
	 */
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(position.x - radius, position.y - radius, radius * 2, radius * 2);
	}

	/**
	 * Moves the light towards its endpoint and returns false when it reaches the
	 * endpoint.
	 * 
	 * @return true if the light has not yet reached the endpoint; false otherwise.
	 */
	public boolean move() {
		if (position.equals(end)) {
			return false;
		}
		int xDirection = Integer.compare(end.x, position.x);
		int yDirection = Integer.compare(end.y, position.y);
		position.translate(xDirection, yDirection);
		return !position.equals(end);
	}
}
