package yeelp.scalingfeast.features;

import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.hunger.HealthRegenEvent;
import squeek.applecore.api.hunger.HealthRegenEvent.AllowRegen;
import squeek.applecore.api.hunger.HealthRegenEvent.AllowSaturatedRegen;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigHealthRegen;
import yeelp.scalingfeast.config.features.SFConfigHealthRegen.RegenCriterion;
import yeelp.scalingfeast.handlers.Handler;

public final class SFHealthRegen extends FeatureBase<SFConfigHealthRegen> {

	@Override
	public Handler getFeatureHandler() {
		return new Handler() {
			private Predicate<EntityPlayer> sat = (p) -> p.getFoodStats().getFoodLevel() >= AppleCoreAPI.accessor.getMaxHunger(p) && p.getFoodStats().getSaturationLevel() > 0.0f;
			private Predicate<EntityPlayer> hunger = (p) -> p.getFoodStats().getFoodLevel() >= AppleCoreAPI.accessor.getMaxHunger(p) - 2;
			
			@SubscribeEvent
			public final void onHungerRegen(AllowRegen evt) {
				handle(getConfig().hungerRegen, this.hunger, evt);
			}
			
			@SubscribeEvent
			public final void onSaturatedRegen(AllowSaturatedRegen evt) {
				handle(getConfig().satRegen, this.sat, evt);
			}
			
			private <E extends HealthRegenEvent> void handle(RegenCriterion criterion, Predicate<EntityPlayer> shouldAllow, E evt) {
				switch(criterion) {
					case DISABLED:
						evt.setResult(Result.DENY);
					case VANILLA:
						break;
					case VANILLA_LIKE:
						evt.setResult(shouldAllow.test(evt.player) ? Result.ALLOW : Result.DENY);
				}
			}
		};
	}

	@Override
	protected SFConfigHealthRegen getConfig() {
		return ModConfig.features.regen;
	}
}
