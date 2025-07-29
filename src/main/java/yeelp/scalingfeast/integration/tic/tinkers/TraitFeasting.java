package yeelp.scalingfeast.integration.tic.tinkers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.integration.tic.TiCConsts;

public final class TraitFeasting extends AbstractTrait {

	public TraitFeasting() {
		super("feasting", TiCConsts.EXHAUSTING_COLOUR);
	}

	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if(wasHit && player instanceof EntityPlayer && !target.isEntityAlive()) {
			ScalingFeastAPI.mutator.addFoodStatsWithOverflow((EntityPlayer) player, 6);
		}
		super.afterHit(tool, player, target, damageDealt, wasCritical, wasHit);
	}
}
