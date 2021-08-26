package yeelp.scalingfeast.api;

/**
 * Interface that outlines basic operations with starvation trackers
 * @author Yeelp
 *
 */
public interface IStarvationTrackerOperations {
	/**
	 * Ticks starvation a specified amount of times, using the given player's hunger as reference and punishes that player appropriately
	 * @param amount amount of times to tick
	 */
	void tickStarvation(int amount);
	
	/**
	 * Ticks the starvation tracker just once.
	 * @param player player to check for.
	 */
	default void tickStarvation() {
		this.tickStarvation(1);
	}
	
	/**
	 * Reset this tracker
	 */
	void resetStarvationTracker();
	
	/**
	 * Get the amount of times this tracker has been ticked
	 * @return the current count in the starvation tracker.
	 */
	short getStarvationCount();
}
