package yeelp.scalingfeast.features;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.api.ScalingFeastAPI;
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
					if(entity instanceof EntityPlayer && getConfig().hungerDamageMultiplier != 0) {
						EntityPlayer player = (EntityPlayer) entity;
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

}
