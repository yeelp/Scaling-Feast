package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class StarvationTrackerProvider 
{
	@CapabilityInject(IStarvationTracker.class)
	public static Capability<IStarvationTracker> starvationTracker = null;

	private IStarvationTracker instance = starvationTracker.getDefaultInstance();
	
	public static IStarvationTracker getTracker(EntityPlayer player)
	{
		return player.getCapability(starvationTracker, null);
	}
	
	public static short getCount(EntityPlayer player)
	{
		return player.getCapability(starvationTracker, null).getCount();
	}
}
