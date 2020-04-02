package yeelp.scalingfeast.util;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class StarvationTrackerProvider 
{
	@CapabilityInject(IStarvationTracker.class)
	public static Capability<IStarvationTracker> starvationTracker = null;

	private IStarvationTracker instance = starvationTracker.getDefaultInstance();
}
