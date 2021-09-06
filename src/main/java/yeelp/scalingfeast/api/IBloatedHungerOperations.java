package yeelp.scalingfeast.api;

/**
 * A simple interface that lists operations that can be performed on bloated hunger.
 * 
 * This merely defers calls to the capability and syncs with the server when needed.
 * @author Yeelp
 *
 */
public interface IBloatedHungerOperations {
	
	/**
	 * Set Bloated hunger amount
	 * @param amount amount to set.
	 */
	void setBloatedHungerAmount(short amount);
	
	/**
	 * Deduct a bloated hunger amount. Return the leftover
	 * @param amount amount to deduct
	 * @return leftover amount not deducted
	 */
	short deductBloatedAmount(short amount);
	
	/**
	 * Give a bloated hunger amount
	 * @param amount amount to increase by
	 */
	void addBloatedAmount(short amount);
	
	/**
	 * Get the bloated hunger amount
	 * @return the bloated hunger amount.
	 */
	short getBloatedHungerAmount();
	
	/**
	 * Remove all bloated hunger.
	 */
	void revokeAllBloatedHunger();
}
