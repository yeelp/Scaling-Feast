package yeelp.scalingfeast.util;

/**
 * An interface for implementing extended food stats
 * @author Yeelp
 *
 */
public interface ICappedFoodStats 
{
	/**
	 * Get the food level
	 * @return the food level
	 */
	default short getFoodLevel()
	{
		return 0;
	}
	/**
	 * Get the saturation level
	 * @return the saturation level
	 */
	default float getSatLevel()
	{
		return 0.0f;
	}
	/**
	 * Get the max food level
	 * @return the max food level
	 */
	default short getMaxFoodLevel()
	{
		return 0;
	}
	void setFoodLevel(short foodLevel);
	void setSatLevel(float satLevel);
	void setMax(short maxLevel);
}
