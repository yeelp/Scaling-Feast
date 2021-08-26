package yeelp.scalingfeast.features;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent.FoodStatsAddition;
import squeek.applecore.api.hunger.ExhaustionEvent.ExhaustionAddition;
import squeek.applecore.api.hunger.StarvationEvent.Starve;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.features.SFConfigStarvation;
import yeelp.scalingfeast.handlers.Handler;

public final class SFStarvationFeatures extends FeatureBase<SFConfigStarvation> {

	static final class PiercingStarvation extends DamageSource {
		static final PiercingStarvation INSTANCE = new PiercingStarvation();
		public PiercingStarvation() {
			super("sfPiercingStarvation");
		}

		@Override
		public boolean isUnblockable() {
			return true;
		}

		@Override
		public boolean isDamageAbsolute() {
			return true;
		}

		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
			return new TextComponentTranslation("message.scalingfeast.starvationDeath", entityLivingBaseIn.getName());
		}
	}
	
	@Override
	public Handler getFeatureHandler() {
		return new Handler() {
			
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public final void onStarve(Starve evt) {
				if(!evt.player.isDead) {
					SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.player);
					int bonusDamage = getBonusDamage(sfstats);
					if(getConfig().dynamicStarvationUnblockable) {
						evt.player.attackEntityFrom(PiercingStarvation.INSTANCE, bonusDamage);
					}
					else {
						evt.starveDamage += bonusDamage;
					}
					if(getConfig().starveLoss != 0) {
						sfstats.tickStarvation(bonusDamage + 1);
					}
					sfstats.resetStarvationExhaustionTracker();
				}
			}
			
			@SubscribeEvent
			public final void onFoodStatsAddition(FoodStatsAddition evt) {
				if(getConfig().doesFreqReset && evt.player.getFoodStats().getFoodLevel() == 0) {
					ScalingFeastAPI.accessor.getSFFoodStats(evt.player).resetStarvationTracker();
				}
			}
			
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public final void onExhaution(ExhaustionAddition evt) {
				if(!evt.player.isDead) {
					ScalingFeastAPI.accessor.getSFFoodStats(evt.player).addExhaustionIfAtZeroHunger(evt.deltaExhaustion);
				}
			}

			private int getBonusDamage(SFFoodStats sfstats) {
				SFConfigStarvation config = getConfig();
				return config.doDynamicStarvation ? config.bonusStarveDamageMult * sfstats.getTotalBonusStarvationDamage() : 0;
			}
		};
	}

	@Override
	protected SFConfigStarvation getConfig() {
		return ModConfig.features.starve;
	}
}
