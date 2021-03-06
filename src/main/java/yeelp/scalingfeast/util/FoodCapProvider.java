package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class FoodCapProvider
{
	@CapabilityInject(IFoodCap.class)
	public static Capability<IFoodCap> capFoodStat = null;
	
	private IFoodCap instance = capFoodStat.getDefaultInstance();
	
	public static IFoodCap getFoodCap(EntityPlayer player)
	{
		return player.getCapability(capFoodStat, null);
	}
}
