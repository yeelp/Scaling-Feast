package yeelp.scalingfeast.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;

public class BloatedHandler extends Handler {
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onStarve(StarvationEvent.Starve evt) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.player);
		if(sfstats.getBloatedHungerAmount() > 0) {
			evt.setCanceled(true);
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onExhausted(ExhaustionEvent.Exhausted evt) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.player);
		if(sfstats.getBloatedHungerAmount() > 0) {
			EntityPlayer player = evt.player;
			short remainder = sfstats.deductBloatedAmount((short) -(evt.deltaHunger + evt.deltaSaturation));
			FoodStats fs = player.getFoodStats();
			float currSatLevel = fs.getSaturationLevel();
			if(currSatLevel == 0) {
				evt.deltaHunger = remainder;
			}
			else {
				if(currSatLevel >= remainder) {
					evt.deltaSaturation = remainder;
					evt.deltaHunger = 0;
				}
				else {
					evt.deltaSaturation = currSatLevel;
					evt.deltaHunger = (int) (remainder - currSatLevel);
				}
			}
		}
	}
}
