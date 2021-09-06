package yeelp.scalingfeast.api;

/**
 * Interface that outlines basic operations with starvation stats
 * 
 * @author Yeelp
 *
 */
public interface IStarvationOperations {
	/**
	 * Count a starvation for the Starvation Counter and Starvation Tracker.
	 * 
	 * @param bonusTrackerTicks The amount of additional ticks the Starvation
	 *                          Tracker should receive
	 */
	void countStarvation(int bonusTrackerTicks);

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
	 * 
	 * @return the current count in the starvation tracker.
	 */
	short getStarvationTrackerCount();

	/**
	 * Get the total number of times this player has starved in a row
	 * 
	 * @return the current count in the starvation counter.
	 */
	short getStarvationCountAllTime();
}
