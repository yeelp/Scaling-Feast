package yeelp.scalingfeast.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.IFoodCapModifier;

public class ModuleHandler extends Handler 
{	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onFoodEaten(FoodEvent.FoodEaten evt)
	{
		updatePlayer(evt.player);
	}
	
	public static void updatePlayer(EntityPlayer player)
	{
		short mod = 0;
		IFoodCapModifier curr = player.getCapability(FoodCapModifierProvider.foodCapMod, null);
		if(SpiceOfLifeHelper.isEnabled() && ModConfig.modules.spiceoflife.enabled)
		{
			mod += SpiceOfLifeHelper.getPenalty(player);
		}
		if(SOLCarrotHelper.isEnabled() && ModConfig.modules.sol.enabled)
		{
			mod += SOLCarrotHelper.getReward(player);
		}
		if(curr.getModifier() == mod)
		{
			return;
		}
		else
		{
			curr.setModifier(mod);
			if(!player.world.isRemote)
			{
				CapabilityHandler.syncMod(player);
			}
		}
	}
}
