package app.models;

import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.PositionI;

/**
 * Represents the two-dimensional position of an entity on a Cartesian plane.
 * This class provides functionality to determine relative directions and
 * distance between this position and another specified position.
 */
public class Position implements PositionI {

	private static final long serialVersionUID = -4235766298940273454L;

	private double x; // The x-coordinate of the position
	private double y; // The y-coordinate of the position

	/**
	 * Constructs a Position object with specified x and y coordinates.
	 *
	 * @param x The x-coordinate of the position.
	 * @param y The y-coordinate of the position.
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x-coordinate of this position.
	 *
	 * @return The x-coordinate as a double.
	 */
	public double getx() {
		return x;
	}

	/**
	 * Returns the y-coordinate of this position.
	 *
	 * @return The y-coordinate as a double.
	 */
	public double gety() {
		return y;
	}

	/**
	 * Calculates the Euclidean distance between this position and another specified
	 * position.
	 *
	 * @param p The other position to which the distance is calculated.
	 * @return The distance as a double, or 0 if the input is not a valid Position
	 *         instance.
	 */
	@Override
	public double distance(PositionI p) {
		if (p instanceof Position) {
			Position other = (Position) p;
			return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
		}
		return 0;
	}

	/**
	 * Determines the cardinal direction from this position to another specified
	 * position.
	 *
	 * @param p The other position from which the direction is determined.
	 * @return The {@link Direction} enum representing the direction, or null if the
	 *         position is invalid or the same.
	 */
	@Override
	public Direction directionFrom(PositionI p) {
		if (p instanceof Position) {
			Position other = (Position) p;
			boolean north = this.northOf(other);
			boolean south = this.southOf(other);
			boolean east = this.eastOf(other);
			boolean west = this.westOf(other);

			if (north && east)
				return Direction.NE;
			if (north && west)
				return Direction.NW;
			if (south && east)
				return Direction.SE;
			if (south && west)
				return Direction.SW;
		}
		return null;
	}

	/**
	 * Checks if the position is north of this position.
	 *
	 * @param p The position to check.
	 * @return true if the position is north of this position; otherwise, false.
	 */
	@Override
	public boolean northOf(PositionI p) {
		if (p instanceof Position) {
			return this.y < ((Position) p).y;
		}
		return false;
	}

	/**
	 * Checks if the position is south of this position.
	 *
	 * @param p The position to check.
	 * @return true if the position is south of this position; otherwise, false.
	 */
	@Override
	public boolean southOf(PositionI p) {
		if (p instanceof Position) {
			return this.y > ((Position) p).y;
		}
		return false;
	}

	/**
	 * Checks if the position is east of this position.
	 *
	 * @param p The position to check.
	 * @return true if the position is east of this position; otherwise, false.
	 */
	@Override
	public boolean eastOf(PositionI p) {
		if (p instanceof Position) {
			return this.x < ((Position) p).x;
		}
		return false;
	}

	/**
	 * Checks if the position is west of this position.
	 *
	 * @param p The position to check.
	 * @return true if the position is west of this position; otherwise, false.
	 */
	@Override
	public boolean westOf(PositionI p) {
		if (p instanceof Position) {
			return this.x > ((Position) p).x;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Position position = (Position) o;
		return Double.compare(position.x, x) == 0 && Double.compare(position.y, y) == 0;
	}
}
