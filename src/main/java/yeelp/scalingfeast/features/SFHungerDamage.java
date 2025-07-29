package yeelp.scalingfeast.features;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigHungerDamage;
import yeelp.scalingfeast.handlers.Handler;

public final class SFHungerDamage extends FeatureBase<SFConfigHungerDamage> {

	@Override
	public Handler getFeatureHandler() {
		return new Handler() {
			@SubscribeEvent
			public void onPlayerAttacked(LivingDamageEvent evt) {
				DamageSource src = evt.getSource();
				if(src.damageType.equals("mob") || (src.getTrueSource() instanceof EntityLivingBase && !(src.getTrueSource() instanceof EntityPlayer))) {
					EntityLivingBase entity = evt.getEntityLiving();
					EntityPlayer player;
					if(entity instanceof EntityPlayer && SFHungerDamage.this.isInValidDimension(player = (EntityPlayer) entity) && getConfig().hungerDamageMultiplier != 0) {
						ScalingFeastAPI.mutator.damageFoodStats(player, (float) (getConfig().hungerDamageMultiplier * evt.getAmount()));
					}
				}
			}
		};
	}

	@Override
	protected SFConfigHungerDamage getConfig() {
		return ModConfig.features.hungerDamage;
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
		return "Hunger Damage";
	}
}
