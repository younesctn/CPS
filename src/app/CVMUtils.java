package app;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import app.config.Config;
import app.gui.NetworkPanel;
import app.models.EnumSensorIdentifier;
import app.models.Position;
import app.models.SensorData;
import fr.sorbonne_u.cps.sensor_network.interfaces.SensorDataI;

/**
 * Utility class for managing the sensor network visualization application.
 */
public class CVMUtils {
	private static NetworkPanel panel;
	private static ArrayList<Position> positions = new ArrayList<>();
	private static ArrayList<Set<SensorDataI>> nodeSensors = new ArrayList<>();

	private CVMUtils() {

	}

	/**
	 * Initializes and displays the graphical interface for visualizing the sensor
	 * network. Configures a frame and panel to display real-time information about
	 * the network.
	 */
	static void createAndShowGUI() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Network Graph");
			panel = new NetworkPanel();

			panel.setPreferredSize(new Dimension(2000, 10000));

			JScrollPane scrollPane = new JScrollPane(panel);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;

			frame.setSize(width / 3, height);
			frame.setLocation(width - frame.getWidth(), 0);

			frame.add(scrollPane);

			javax.swing.SwingUtilities.invokeLater(() -> {
				JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
				verticalScrollBar.setValue(verticalScrollBar.getMaximum());
			});
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}

	/**
	 * Generates a random value between 0.0 and 100.0 to simulate sensor values.
	 *
	 * @return The generated sensor value.
	 */
	private static Double generateSensorValue() {
		double value = new Random().nextDouble() * 100;
		return Math.round(value * 100.0) / 100.0;
	}

	/**
	 * Initializes and configures the sensor data sets for each node in the sensor network.
	 */
	private static void initializeSensors() {
		for (int i = 1; i <= Config.NBNODE; i++) {

			Set<SensorDataI> sensorsForNode = new HashSet<>();
			sensorsForNode.add(new SensorData("Node" + i, EnumSensorIdentifier.Weather.name(), generateSensorValue()));
			sensorsForNode
					.add(new SensorData("Node" + i, EnumSensorIdentifier.WindSpeed.name(), generateSensorValue()));
			sensorsForNode
					.add(new SensorData("Node" + i, EnumSensorIdentifier.WindDirection.name(), generateSensorValue()));
			sensorsForNode.add(new SensorData("Node" + i, EnumSensorIdentifier.Smoke.name(), generateSensorValue()));
			sensorsForNode.add(new SensorData("Node" + i, EnumSensorIdentifier.Heat.name(), generateSensorValue()));
			sensorsForNode
					.add(new SensorData("Node" + i, EnumSensorIdentifier.Biological.name(), generateSensorValue()));
			sensorsForNode.add(new SensorData("Node" + i, EnumSensorIdentifier.Humidity.name(), generateSensorValue()));

			nodeSensors.add(sensorsForNode);
		}
	}

	/**
	 * Initializes and populates the list of node positions using a grid defined by the constants configured in Config.ROW and Config.COLUMN. 
	 * This method creates a spatial distribution of nodes in a grid that may have an alternating offset on each row, to simulate,
	 *  for example, a hexagonal arrangement or a similar structure. 
	 *  The method takes into account a maximum number of nodes specified by Config.NBNODE to limit the number of generated positions.
	 * The grid is filled row by row. Each row may have an initial offset that affects the x position of each node on that row. 
	 * The effective number of columns per row is reduced by this offset. The y coordinates are calculated to start from the top of the grid, 
	 * thus reversing the row indices to match a traditional visual orientation.
	 */

	private static void initializePositions() {
		positions.clear();
		int nodeCount = 0;
		int row = 1;

		// Continue creating rows until all nodes are placed
		while (nodeCount < Config.NBNODE) {
			int yOffset = (row % 2 == 0) ? 0 : 1; // Offset for even and odd rows
			for (int col = 0; col < Config.COLUM - yOffset; col++) {
				if (nodeCount >= Config.NBNODE) {
					break; // Stop if all nodes are placed
				}
				int x = col * 2 + 1 + yOffset; // X starts at 1 and alternates offset by row
				int y = row;
				positions.add(new Position(x, y));
				nodeCount++;
			}
			row++; // Move to the next row after finishing one row
		}
	}

	/**
	 * Initializes the positions and sensor data based on the configuration.
	 */

	static void initializePositionsAndSensors() {
		initializePositions();
		initializeSensors();
	}

	/**
	 * get the panel of the application.
	 *
	 * @return the panel.
	 */
	static NetworkPanel getPanel() {
		return panel;
	}

	/**
	 * get the generated positions.
	 *
	 * @return list of positions.
	 */
	static ArrayList<Position> getPositions() {
		return positions;
	}

	/**
	 * get the generated sensor data.
	 *
	 * @return set of sensor data.
	 */
	static ArrayList<Set<SensorDataI>> getNodeSensors() {
		return nodeSensors;
	}
}
