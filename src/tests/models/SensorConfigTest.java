package tests.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import app.gui.GraphicalNetworkInterface;
import app.models.Position;
import app.models.SensorConfig;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;

class SensorConfigTest {

	// Dummy implementation for GraphicalNetworkInterface for testing purposes
	static class TestGraphicalNetworkInterface implements GraphicalNetworkInterface {

		@Override
		public void addGraphicalNode(String name, double x, double y) {

		}

		@Override
		public void addGraphicalConnection(String startName, String endName) {

		}

		@Override
		public void removeGraphicalConnection(String startName, String endName) {

		}

		@Override
		public void startGraphicalLightAnimation(String startName, String endName) {

		}

		@Override
		public void toggleNodeBlinking(String nodeIdentifier) {

		}

		@Override
		public void resetNodesBlink() {

		}
		// Implementation details are not necessary for the tests
	}

	// Dummy implementation for Position for testing purposes
	@SuppressWarnings("serial")
	static class TestPosition extends Position {
		// Constructor
		public TestPosition(double x, double y) {
			super(x, y);
		}
	}

	@Test
	void testSensorConfig() {

		GraphicalNetworkInterface gui = new TestGraphicalNetworkInterface();
		String inboundPortRegister = "inboundPortRegister";
		String uriClock = "uriClock";
		Set<SensorDataI> sensors = new HashSet<>(); // Empty set for testing
		int name = 123;
		Position position = new TestPosition(1.0, 2.0);
		int nbthread = 4;
		double range = 50.0;

		// Creating a SensorConfig instance with the specified parameters
		SensorConfig sensorConfig = new SensorConfig(gui, inboundPortRegister, uriClock, sensors, name, position,
				nbthread, range);

		assertEquals(gui, sensorConfig.getGui(), "The GUI should match the expected value.");
		assertEquals(inboundPortRegister, sensorConfig.getInboundPortRegister(),
				"The inbound port register should match the expected value.");
		assertEquals(uriClock, sensorConfig.getUriClock(), "The URI clock should match the expected value.");
		assertEquals(sensors, sensorConfig.getSensors(), "The set of sensors should match the expected value.");
		assertEquals(name, sensorConfig.getName(), "The sensor name should match the expected value.");
		assertEquals(position, sensorConfig.getPosition(), "The sensor position should match the expected value.");
		assertEquals(nbthread, sensorConfig.getNbthread(), "The number of threads should match the expected value.");
		assertEquals(range, sensorConfig.getRange(), "The sensor range should match the expected value.");
	}
}
