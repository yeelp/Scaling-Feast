package yeelp.scalingfeast.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.FakePlayer;
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
				SFConfigBloatedOverflow config = SFBloatedOverflow.this.getConfig();
				if(!config.doBloatedOverflow) {
					return;
				}
				FoodValues fVals = evt.foodValuesToBeAdded;
				EntityPlayer player = evt.player;
				if(player.world.isRemote || player instanceof FakePlayer) {
					return;
				}
				int overflow = player.getFoodStats().getFoodLevel() + fVals.hunger - AppleCoreAPI.accessor.getMaxHunger(player);
				int cap = config.bloatedLevelCap;
				int level = Math.min(cap < 0 ? Integer.MAX_VALUE : cap, overflow / 4 - 1);

				if(level >= 0) {
					player.addPotionEffect(new PotionEffect(SFPotion.bloated, config.bloatedOverflowDuration, level));
				}
			}
		};
	}

	@Override
	public SFConfigBloatedOverflow getConfig() {
		return ModConfig.features.bloatedOverflow;
	}
}
