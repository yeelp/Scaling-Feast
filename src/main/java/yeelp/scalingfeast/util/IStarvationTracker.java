package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * A simple counter to track starvation damage
 * @author Yeelp
 *
 */
public interface IStarvationTracker extends ICapabilitySerializable<NBTTagShort>
{
	/**
	 * Check and increment the starvation counter. Only works if hunger = 0
	 * @param hunger the hunger value to check against.
	 */
	void tickStarvation(int hunger);
	
	/**
	 * Reset the counter
	 */
	void reset();
	
	/**
	 * Get the current value in the counter
	 * @return the current count
	 */
	short getCount();
	
	/**
	 * Set the counter value
	 * @param amount the amount to set it to
	 */
	void setCount(short amount);
}
