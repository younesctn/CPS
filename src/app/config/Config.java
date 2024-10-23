package app.config;

/**
 * Holds configuration constants used throughout the sensor network application.
 * This class provides a central location for managing settings related to time synchronization,
 * simulation parameters, and network architecture.
 */
public class Config {
    /**
     * The URI for the clock server used in the network to synchronize time across components.
     */
    public static final String TEST_CLOCK_URI = "Clock-network-sensor";

    /**
     * The ISO 8601 string representation of the start instant for the simulation or operation.
     */
    public static final String START_INSTANT = "2024-03-18T20:05:00.00Z";

    /**
     * The initial delay in milliseconds before starting the simulation or network operations.
     */
    public static final long START_DELAY = 8000L;

    /**
     * The factor by which time is accelerated in the simulation, aiding in faster execution
     * of time-dependent processes.
     */
    public static final double ACCELERATION_FACTOR = 60.0;

    /**
     * The number of clients in the network simulation or configuration.
     */
    public static final int NBCLIENT = 4;

    /**
     * The number of nodes in the network simulation or configuration.
     */
    public static final int NBNODE = 50;

    /**
     * The number of columns in the network layout or graphical representation,
     * used in arranging the visual components.
     */
    public static final int COLUM = 5;
    
    public static final boolean ASYNC = true;
}
