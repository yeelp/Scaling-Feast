package yeelp.scalingfeast.api;

/**
 * Interface that outlines basic operations with starvation stats
 * @author Yeelp
 *
 */
public interface IStarvationOperations {
	/**
	 * Ticks starvation a specified amount of times, using the given player's hunger as reference and punishes that player appropriately
	 * @param amount amount of times to tick
	 */
	void tickStarvation(int amount);
	
	/**
	 * Ticks the starvation tracker just once.
	 */
	default void tickStarvation() {
		this.tickStarvation(1);
	}
	
	/**
	 * Reset this tracker
	 */
	void resetStarvationTracker();
	
	/**
	 * Reset the count of the number of times this player has starved in a row
	 */
	void resetStarvationCountAllTime();
	
	/**
	 * Reset both the tracker and the counter.
	 */
	void resetStarvationStats();
	
	/**
	 * Get the amount of times this tracker has been ticked
	 * @return the current count in the starvation tracker.
	 */
	short getStarvationTrackerCount();
	
	/**
	 * Get the total number of times this player has starved in a row
	 * @return the current count in the starvation counter.
	 */
	short getStarvationCountAllTime();
}
