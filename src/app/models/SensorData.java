package app.models;

import java.io.Serializable;
import java.time.Instant;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;

/**
 * Represents data collected by a sensor within a sensor network. This class
 * encapsulates the identifier of the node and sensor, the value of the data,
 * and the timestamp when the data was recorded.
 */
public class SensorData implements SensorDataI, Serializable, Cloneable {

	private static final long serialVersionUID = -4202232058504256513L;

	private String nodeIdentifier; // Identifier of the node where the sensor is located
	private String sensorIdentifier; // Identifier of the sensor that produced the data
	private Serializable value; // The numeric value recorded by the sensor
	private Instant timestamp; // The timestamp when the data was recorded

	/**
	 * Constructs a SensorData object with specified identifiers and value.
	 *
	 * @param nodeIdentifier   The identifier of the node where the sensor is
	 *                         located.
	 * @param sensorIdentifier The identifier of the sensor that produced the data.
	 * @param value            The value recorded by the sensor, represented as a
	 *                         Double.
	 */
	public SensorData(String nodeIdentifier, String sensorIdentifier, Serializable value) {
		this.nodeIdentifier = nodeIdentifier;
		this.sensorIdentifier = sensorIdentifier;
		this.value = value;
		this.timestamp = Instant.now(); // Capture the timestamp at the moment of data creation
	}

	/**
	 * Creates and returns a copy of this object.
	 *
	 * @return a clone of this instance.
	 * @throws CloneNotSupportedException if the object's class does not support the
	 *                                    Cloneable interface.
	 */
	@Override
	public SensorData clone() throws CloneNotSupportedException {
		SensorData cloned = (SensorData) super.clone();
		cloned.timestamp = this.timestamp; // Instant is immutable, so we can share references
		// Handle cloning of the value if necessary. Assuming value is immutable or
		// final state.
		// If the value needs deep cloning and is mutable, additional logic will be
		// needed.
		return cloned;
	}

	/**
	 * Retrieves the node identifier for this sensor data.
	 *
	 * @return A string representing the node identifier.
	 */
	@Override
	public String getNodeIdentifier() {
		return nodeIdentifier;
	}

	/**
	 * Retrieves the data type of the sensor value.
	 *
	 * @return A class type representing the type of the sensor data value.
	 */
	@Override
	public Class<? extends Serializable> getType() {
		return value.getClass();
	}

	/**
	 * Retrieves the value recorded by the sensor.
	 *
	 * @return The sensor value as a Serializable object.
	 */
	@Override
	public Serializable getValue() {

		return value;
	}

	/**
	 * Retrieves the sensor identifier.
	 *
	 * @return A string representing the sensor identifier.
	 */
	@Override
	public String getSensorIdentifier() {
		return sensorIdentifier;
	}

	/**
	 * Provides a string representation of the sensor data.
	 *
	 * @return A string that includes the sensor identifier and its corresponding
	 *         value.
	 */
	@Override
	public String toString() {
		return nodeIdentifier + ":" + " " + sensorIdentifier + "(" + value + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true; // Check for reference equality.
		if (obj == null || getClass() != obj.getClass())
			return false; // Check for null and ensure the objects are of the same class.

		SensorDataI that = (SensorDataI) obj; // Type cast the object to SensorDataI.
		return this.toString().equals(that.toString()); // Compare the string representations.
	}

	/**
	 * Retrieves the timestamp when the data was recorded.
	 *
	 * @return The timestamp as an Instant object.
	 */
	@Override
	public Instant getTimestamp() {
		return timestamp;
	}
}
