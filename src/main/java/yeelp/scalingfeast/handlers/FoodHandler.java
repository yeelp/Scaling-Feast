package yeelp.scalingfeast.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.hunger.HungerEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

public class FoodHandler extends Handler 
{	
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt)
	{
		evt.maxHunger = evt.player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel();
	}
	
	//Even if this event is canceled, we probably want to tick the starvation tracker. This is fine, as it will only succeed if the food level is zero.
	@SubscribeEvent(receiveCanceled=true)
	public void onStarve(StarvationEvent.Starve evt)
	{
		IStarvationTracker tracker = evt.player.getCapability(StarvationTrackerProvider.starvationTracker, null);
		//only do any of this if there is max hunger to lose, otherwise this is a waste of processing.
		if(ModConfig.foodCap.starveLoss != 0)
		{
			tracker.tickStarvation(evt.player.getFoodStats().getFoodLevel());
			if(tracker.getCount() >= ModConfig.foodCap.lossFreq)
			{
				tracker.reset();
				evt.player.getCapability(FoodCapProvider.capFoodStat, null).decreaseMax((short) ModConfig.foodCap.starveLoss);
				CapabilityHandler.sync(evt.player);
			}
			else
			{
				CapabilityHandler.syncTracker(evt.player);
			}
		}
	}
	
	@SubscribeEvent
	public void onFoodStatsAddition(FoodEvent.FoodStatsAddition evt)
	{
		//Only sync if the food level is zero, as that is the only time the tracker should be resetting. The tracker should always be zero if the frequency resets and the food level > 0.
		if(ModConfig.foodCap.doesFreqReset && evt.player.getFoodStats().getFoodLevel() == 0)
		{
			evt.player.getCapability(StarvationTrackerProvider.starvationTracker, null).reset();
			CapabilityHandler.syncTracker(evt.player);
		}
	}
}
