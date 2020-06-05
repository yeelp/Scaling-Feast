package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BloatedHungerProvider
{
	@CapabilityInject(IBloatedHunger.class)
	public static Capability<IBloatedHunger> bloatedHunger = null;
	
	private IBloatedHunger instance = bloatedHunger.getDefaultInstance();
	
	public static IBloatedHunger getBloatedHunger(EntityPlayer player)
	{
		return player.getCapability(bloatedHunger, null);
	}
}
