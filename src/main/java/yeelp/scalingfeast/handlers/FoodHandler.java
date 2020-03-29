package yeelp.scalingfeast.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.HungerEvent;
import yeelp.scalingfeast.util.FoodCapProvider;

public class FoodHandler extends Handler 
{	
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt)
	{
		evt.maxHunger = evt.player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel();
	}
}
