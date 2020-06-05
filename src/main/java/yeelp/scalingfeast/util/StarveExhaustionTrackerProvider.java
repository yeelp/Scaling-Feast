package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class StarveExhaustionTrackerProvider
{
	@CapabilityInject(IStarveExhaustionTracker.class)
	public static Capability<IStarveExhaustionTracker> starveExhaust = null;
	
	private IStarveExhaustionTracker instance = starveExhaust.getDefaultInstance();
	
	public static IStarveExhaustionTracker getStarveExhaustionTracker(EntityPlayer player)
	{
		return player.getCapability(starveExhaust, null);
	}
}
