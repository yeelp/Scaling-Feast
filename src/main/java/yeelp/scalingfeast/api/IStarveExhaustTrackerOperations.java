package yeelp.scalingfeast.api;

public interface IStarveExhaustTrackerOperations {

	/**
	 * Add exhaustion to this tracker. Only works when hunger is zero
	 * 
	 * @param amount amount of exhaustion to add
	 */
	void addExhaustionIfAtZeroHunger(float amount);

	/**
	 * Get a player's total exhaustion since reaching zero hunger. If the player isn't at zero hunger, this returns 0
	 * 
	 * @return the total exhaustion.
	 */
	float getTotalExhaustionAtZeroHunger();

	/**
	 * Get the total amount of bonus damage to deal to this player when they starve.
	 * 
	 * @return the amount of bonus damage to deal.
	 */
	int getTotalBonusStarvationDamage();
	
	/**
	 * Reset the starvation exhaustion tracker
	 */
	void resetStarvationExhaustionTracker();
}
