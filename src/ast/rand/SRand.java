package ast.rand;

import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;

import java.io.Serializable;

/**
 * Represents a sensor-based random value generator.
 * This class implements the IRand interface for evaluating the random value.
 */
public class SRand implements IRand, Serializable {
    private static final long serialVersionUID = 23L;

    private String sensorId;

    /**
     * Constructs a sensor-based random value generator with the specified sensor ID.
     *
     * @param sensorId The ID of the sensor to use for generating random values.
     */
    public SRand(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Evaluates the sensor-based random value generator.
     *
     * @param curentNode The execution state representing the current node.
     * @return The value of the sensor corresponding to the sensor ID.
     */
    @Override
    public double eval(ExecutionStateI curentNode) {
        SensorDataI currentNodeSensor = curentNode.getProcessingNode().getSensorData(sensorId);
        return (Double) currentNodeSensor.getValue();
    }
}

