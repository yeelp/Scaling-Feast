package yeelp.scalingfeast.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigDeath;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

import javax.annotation.Nonnull;

public final class SFDeathFeatures extends FeatureBase<SFConfigDeath> {

	@Override
	@Nonnull
	public Handler getFeatureHandler() {
		return new Handler() {

			@SubscribeEvent
			public void onDeath(PlayerEvent.Clone evt) {
				if(!evt.isWasDeath()) {
					return;
				}
				EntityPlayer player = evt.getEntityPlayer();
				SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(player);
				int maxHunger = AppleCoreAPI.accessor.getMaxHunger(player);
				SFConfigDeath config = getConfig();
				// Death Penalties
				if(maxHunger > config.maxLossLowerBound && config.maxLossAmount > 0) {
					double currDeathPenalty = SFBuiltInModifiers.MaxHungerModifiers.DEATH.getModifierValueForPlayer(player);
					sfstats.applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.DEATH.createModifier(currDeathPenalty + MathHelper.clamp(-config.maxLossAmount, config.maxLossLowerBound - maxHunger, 0)));
				}
				// Stat Persistence
				int respawningHunger = getConfig().respawningStats.getRespawningHunger(evt.getOriginal(), evt.getEntityPlayer());
				float respawningSat = getConfig().respawningStats.getRespawningSaturation(evt.getOriginal(), evt.getEntityPlayer());
				AppleCoreAPI.mutator.setHunger(player, respawningHunger);
				AppleCoreAPI.mutator.setSaturation(player, respawningSat);
				// Hunger Penalties
				if(config.hungerLossOnDeath > 0) {
					ScalingFeastAPI.mutator.deductFoodStats(player, config.hungerLossOnDeath);
				}
			}
		};
	}

	@Override
	protected SFConfigDeath getConfig() {
		return ModConfig.features.death;
	}
	
	@Override
	protected String[] getDimensionListFromConfig() {
		return null;
	}

	@Override
	protected FilterListType getFilterListTypeFromConfig() {
		return null;
	}

	@Override
	protected String getName() {
		return "Death Penalties";
	}

}
