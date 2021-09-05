package yeelp.scalingfeast.features;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.HealthRegenEvent.AllowRegen;
import squeek.applecore.api.hunger.HealthRegenEvent.AllowSaturatedRegen;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigHealthRegen;
import yeelp.scalingfeast.handlers.Handler;

public final class SFHealthRegen extends FeatureBase<SFConfigHealthRegen> {

	@Override
	public Handler getFeatureHandler() {
		return new Handler() {
			
			@SubscribeEvent
			public final void onHungerRegen(AllowRegen evt) {
				if(evt.player.shouldHeal()) {
					evt.setResult(SFHealthRegen.this.getConfig().hungerRegen.determineResult(evt.player, evt.getResult()));
				}
			}
			
			@SubscribeEvent
			public final void onSaturatedRegen(AllowSaturatedRegen evt) {
				if(evt.player.shouldHeal()) {
					evt.setResult(SFHealthRegen.this.getConfig().satRegen.determineResult(evt.player, evt.getResult()));
				}
			}
		};
	}

	@Override
	protected SFConfigHealthRegen getConfig() {
		return ModConfig.features.regen;
	}
}
