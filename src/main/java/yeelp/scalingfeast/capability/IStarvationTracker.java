package yeelp.scalingfeast.capability;

import net.minecraft.nbt.NBTTagShort;
import yeelp.scalingfeast.capability.impl.StarvationTracker;

/**
 * A simple counter to track starvation damage
 * 
 * @author Yeelp
 *
 */
public interface IStarvationTracker extends SFSingleValueCapability<Short, NBTTagShort> {
	
	static void register() {
		SFCapabilityBase.register(IStarvationTracker.class, NBTTagShort.class, StarvationTracker::new);
	}
}
