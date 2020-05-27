package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class FoodCapModifierProvider 
{
	@CapabilityInject(IFoodCapModifier.class)
	public static Capability<IFoodCapModifier> foodCapMod = null;
	
	private IFoodCapModifier instance = foodCapMod.getDefaultInstance();

	public static IFoodCapModifier getFoodMod(EntityPlayer player)
	{
		return player.getCapability(foodCapMod, null);
	}
}
