package yeelp.scalingfeast.capability;

import net.minecraft.nbt.NBTTagCompound;
import yeelp.scalingfeast.capability.impl.StarvationStats;

/**
 * A pairing of the starvation tracker and starvation counter into one capability.
 * @author Yeelp
 *
 */
public interface IStarvationStats extends SFCapabilityBase<NBTTagCompound> {
	
	/**
	 * The base interface for a counter
	 * @author Yeelp
	 *
	 */
	interface ICountable {
		
		/**
		 * Increment once.
		 */
		default void inc() {
			inc((short) 1);
		}
		
		/**
		 * Decrement the counter by an amount. Just calls {@link ICountable#inc(short)} with a sign swapped argument.
		 * @param amount amount to decrement
		 */
		default void dec(short amount) {
			this.inc((short) -amount);
		}
		
		/**
		 * Increment.
		 * @param amount amount of times to increment.
		 */
		void inc(short amount);
		
		/**
		 * Reset the counter.
		 */
		void reset();
		
		/**
		 * Set the counter.
		 * @param val value to set.
		 */
		void set(short val);
		
		/**
		 * Get the current value of the counter.
		 * @return the current value of the counter.
		 */
		short get();
	}
	
	/**
	 * Get the current tracker for tracking current starvation penalties.
	 * @return The tracker ICountable
	 */
	ICountable getTracker();
	
	/**
	 * Get the current counter for how many times this player has starved in a row.
	 * @return The counter ICountable
	 */
	ICountable getCounter();
	
	static void register() {
		SFCapabilityBase.register(IStarvationStats.class, NBTTagCompound.class, StarvationStats::new);
	}
}
