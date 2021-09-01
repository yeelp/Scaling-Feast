package yeelp.scalingfeast.features;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigXPBonuses;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.lib.xpcalculators.FoodEfficiencyXPBonusCalculator;
import yeelp.scalingfeast.lib.xpcalculators.MaxHungerXPBonusCalculator;
import yeelp.scalingfeast.lib.xpcalculators.XPBonusCalculator;

public final class SFXPBonuses extends FeatureBase<SFConfigXPBonuses> {

	@Override
	public Handler getFeatureHandler() {
		return new Handler() {

			@SubscribeEvent
			public final void onConfigChanged(final PostConfigChangedEvent evt) {
				if(evt.getModID().equals(ModConsts.MOD_ID)) {
					MaxHungerXPBonusCalculator.getInstance().setMilestoneList(XPBonusCalculator.createNewListOfMilestones(SFXPBonuses.this.getConfig().maxHungerRewards));
					FoodEfficiencyXPBonusCalculator.getInstance().setMilestoneList(XPBonusCalculator.createNewListOfMilestones(SFXPBonuses.this.getConfig().efficiencyRewards));
				}
			}

			@SubscribeEvent
			public final void onPlayerTick(PlayerTickEvent evt) {
				MaxHungerXPBonusCalculator.getInstance().applyXPBonus(evt.player);
				FoodEfficiencyXPBonusCalculator.getInstance().applyXPBonus(evt.player);
			}
		};
	}

	@Override
	protected SFConfigXPBonuses getConfig() {
		return ModConfig.features.xpBonuses;
	}
}
