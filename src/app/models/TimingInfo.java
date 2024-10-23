package app.models;

/**
 * The {@code TimingInfo} class is designed to track the timing of events, 
 * specifically recording the start and end times of a process or operation.
 * It provides a convenient way to measure the duration of operations by capturing
 * the time at the start of the operation and at its conclusion.
 * 
 * This class uses system time in milliseconds for timing, which is suitable for
 * measuring operations in a variety of applications.
 */
public class TimingInfo {
    /** Stores the start time of the event in milliseconds since the epoch. */
    private long startTime;

    /** Stores the end time of the event in milliseconds since the epoch. */
    private long endTime;

    /**
     * Constructs a new {@code TimingInfo} object and initializes the start time 
     * to the current system time in milliseconds.
     */
    public TimingInfo() {
        this.startTime = System.currentTimeMillis();  
    }

    /**
     * Sets the end time to the current system time in milliseconds.
     * This method should be called at the conclusion of the timed event or operation
     * to capture the finish time accurately.
     */
    public void setEndTime() {
        this.endTime = System.currentTimeMillis();  
    }

    /**
     * Retrieves the start time of the event.
     *
     * @return the start time in milliseconds since the epoch.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Retrieves the end time of the event.
     *
     * @return the end time in milliseconds since the epoch.
     */
    public long getEndTime() {
        return endTime;
    }
}
