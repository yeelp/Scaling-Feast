package yeelp.scalingfeast.capability;

import net.minecraft.nbt.NBTTagFloat;
import yeelp.scalingfeast.capability.impl.StarveExhaustionTracker;

/**
 * A way to track exhaustion since going to zero hunger.
 * 
 * @author Yeelp
 *
 */
public interface IStarveExhaustionTracker extends SFSingleValueCapability<Float, NBTTagFloat> {
	
	static void register() {
		SFCapabilityBase.register(IStarveExhaustionTracker.class, NBTTagFloat.class, StarveExhaustionTracker::new);
	}
}
