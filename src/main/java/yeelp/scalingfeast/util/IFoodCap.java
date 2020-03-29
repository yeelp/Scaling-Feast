package yeelp.scalingfeast.util;

/**
 * An interface for implementing extended food stats
 * @author Yeelp
 *
 */
public interface IFoodCap 
{
	/**
	 * Get the max food level
	 * @return the max food level
	 */
	default short getMaxFoodLevel()
	{
		return 0;
	}
	/**
	 * Set the max food cap
	 * @param maxLevel the level to set
	 */
	void setMax(short maxLevel);
	/**
	 * Increase the max food count
	 * @param amount the amount to increase by
	 */
	void increaseMax(short amount);
}
