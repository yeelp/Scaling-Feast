package yeelp.scalingfeast.features;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.features.SFConfigXPBonuses;
import yeelp.scalingfeast.handlers.Handler;

public final class SFXPBonuses extends FeatureBase<SFConfigXPBonuses> {

	@Override
	public Handler getFeatureHandler() {
		return new Handler() {
			@SubscribeEvent
			public final void onPlayerTick(PlayerTickEvent evt) {
				ScalingFeastAPI.accessor.getSFFoodStats(evt.player).updateXPBonuses();
			}
		};
	}

	@Override
	protected SFConfigXPBonuses getConfig() {
		return ModConfig.features.xpBonuses;
	}
}
