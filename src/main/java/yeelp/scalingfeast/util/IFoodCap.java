package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * An interface for implementing extended food stats
 * @author Yeelp
 *
 */
public interface IFoodCap extends ICapabilitySerializable<NBTTagShort>
{
	/**
	 * Get the max food level
	 * @return the max food level
	 */
	default short getMaxFoodLevel()
	{
		return 20;
	}
	
	/**
	 * Get the max food level before modifications
	 * @return the max food level, before any modifications
	 */
	default short getUnmodifiedMaxFoodLevel(FoodCapModifier mod)
	{
		return 20;
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
	/**
	 * Decrease the max food count
	 * @param amount the amount to decrease by
	 */
	void decreaseMax(short amount);	
}
