package yeelp.scalingfeast.api;

import yeelp.scalingfeast.capability.IStarveExhaustionTracker;

/**
 * Interface to work with the {@link IStarveExhaustionTracker}
 * @author Yeelp
 *
 */
public interface IStarveExhaustTrackerChanger {
	
	/**
	 * Get the IStarveExhaustionTracker
	 * @return The IStarveExhaustionTracker
	 */
	IStarveExhaustionTracker getStarvationExhaustionTracker();
	
	/**
	 * Track exhaustion, sync with server.
	 * @param amount amount to add.
	 */
	void trackExhaustion(float amount);
}
