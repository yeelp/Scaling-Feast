package yeelp.scalingfeast.capability;

import net.minecraft.nbt.NBTBase;

/**
 * A capability that tracks a single value.
 * @author Yeelp
 *
 * @param <T> The numerical value being tracked.
 * @param <NBT> The NBT tags being stored.
 */
public interface SFSingleValueCapability<T extends Number, NBT extends NBTBase> extends SFCapabilityBase<NBT> {
	
	/**
	 * Get the value being tracked
	 * @return the values being tracked
	 */
	T getVal();
	
	/**
	 * Set the value that's being tracked
	 * @param val the value to set
	 */
	void setVal(T val);
	
	/**
	 * Increment the tracked value by a certain amount
	 * @param amount amount to increment
	 */
	void inc(T amount);
	
	/**
	 * Decrement the tracked value by a certain amount
	 * @param amount amount to decrement
	 */
	@SuppressWarnings("unused")
	void dec(T amount);
	
	/**
	 * Reset the tracked value.
	 */
	void reset();
}
