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
				if(!evt.player.shouldHeal()) {
					return;
				}
				if(SFHealthRegen.this.isInValidDimension(evt.player)) {
					evt.setResult(SFHealthRegen.this.getConfig().hungerRegen.determineResult(evt.player, evt.getResult()));
				}
				else {
					evt.setResult(SFHealthRegen.this.getConfig().filteredHungerRegen.determineResult(evt.player, evt.getResult()));
				}
			}
			
			@SubscribeEvent
			public final void onSaturatedRegen(AllowSaturatedRegen evt) {
				if(!evt.player.shouldHeal()) {
					return;
				}
				if(SFHealthRegen.this.isInValidDimension(evt.player)) {
					evt.setResult(SFHealthRegen.this.getConfig().satRegen.determineResult(evt.player, evt.getResult()));
				}
				else {
					evt.setResult(SFHealthRegen.this.getConfig().filteredSatRegen.determineResult(evt.player, evt.getResult()));
				}
			}
		};
	}

	@Override
	protected SFConfigHealthRegen getConfig() {
		return ModConfig.features.regen;
	}
	
	@Override
	protected String[] getDimensionListFromConfig() {
		return this.getConfig().dimList;
	}

	@Override
	protected FilterListType getFilterListTypeFromConfig() {
		return this.getConfig().listType;
	}
	
	@Override
	protected String getName() {
		return "Health Regen Rules";
	}
}
