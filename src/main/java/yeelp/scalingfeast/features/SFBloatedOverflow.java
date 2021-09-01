package yeelp.scalingfeast.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent.FoodStatsAddition;
import squeek.applecore.api.food.FoodValues;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigBloatedOverflow;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFPotion;

public final class SFBloatedOverflow extends FeatureBase<SFConfigBloatedOverflow> {

	@Override
	public Handler getFeatureHandler() {
		return new Handler() {

			@SubscribeEvent
			public final void onFoodStatsAddition(FoodStatsAddition evt) {
				FoodValues fVals = evt.foodValuesToBeAdded;
				EntityPlayer player = evt.player;
				int overflow = player.getFoodStats().getFoodLevel() + fVals.hunger - AppleCoreAPI.accessor.getMaxHunger(player);
				int level = overflow / 4 - 1;

				if(level >= 0) {
					player.addPotionEffect(new PotionEffect(SFPotion.bloated, getConfig().bloatedOverflowDuration, level));
				}
			}
		};
	}

	@Override
	public SFConfigBloatedOverflow getConfig() {
		return ModConfig.features.bloatedOverflow;
	}
}
