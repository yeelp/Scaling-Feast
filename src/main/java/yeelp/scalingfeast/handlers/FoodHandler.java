package yeelp.scalingfeast.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.hunger.HungerEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

public class FoodHandler extends Handler 
{	
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt)
	{
		evt.maxHunger = evt.player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel(evt.player.getCapability(FoodCapModifierProvider.foodCapMod, null));
	}
	
	//Even if this event is canceled, we probably want to tick the starvation tracker. This is fine, as it will only succeed if the food level is zero.
	@SubscribeEvent(receiveCanceled=true)
	public void onStarve(StarvationEvent.Starve evt)
	{
		//only do any of this if there is max hunger to lose, otherwise this is a waste of processing.
		if(ModConfig.foodCap.starve.starveLoss != 0 && !evt.player.isDead)
		{
			IStarvationTracker tracker = evt.player.getCapability(StarvationTrackerProvider.starvationTracker, null);
			tracker.tickStarvation(evt.player.getFoodStats().getFoodLevel());
			if(tracker.getCount() >= ModConfig.foodCap.starve.lossFreq)
			{
				if(ModConfig.foodCap.starve.doesFreqResetOnStarve)
				{
					tracker.reset();
				}
				else
				{
					tracker.setCount((short)(ModConfig.foodCap.starve.lossFreq - 1));
				}
				IFoodCap foodCap = evt.player.getCapability(FoodCapProvider.capFoodStat, null);
				//if foodcap <= our lower bound, do nothing.
				if(foodCap.getUnmodifiedMaxFoodLevel() <= ModConfig.foodCap.starve.starveLowerCap)
				{
					return;
				}
				else
				{
					foodCap.decreaseMax((short) ModConfig.foodCap.starve.starveLoss);
				}
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
		if(ModConfig.foodCap.starve.doesFreqReset && evt.player.getFoodStats().getFoodLevel() == 0)
		{
			evt.player.getCapability(StarvationTrackerProvider.starvationTracker, null).reset();
			if(!evt.player.world.isRemote)
			{
				CapabilityHandler.syncTracker(evt.player);
			}
		}
	}
}
