package yeelp.scalingfeast.integration.tic.tinkers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTraitLeveled;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.integration.tic.TiCConsts;

public final class TraitExhausting extends AbstractTraitLeveled {

	private static final List<Potion> effects = Arrays.asList(new Potion[] {
			MobEffects.WEAKNESS,
			MobEffects.SLOWNESS});

	public TraitExhausting(int level) {
		super("exhausting", TiCConsts.EXHAUSTING_COLOUR, 3, level);
	}

	@Override
	public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
		if(player instanceof EntityPlayer && !player.getEntityWorld().isRemote) {
			EntityPlayer entityPlayer = (EntityPlayer) player;
			entityPlayer.getFoodStats().addExhaustion(3.0f * getLevel(tool));
		}
		super.afterBlockBreak(tool, world, state, pos, player, wasEffective);
	}

	@Override
	public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
		if(!target.getEntityWorld().isRemote) {
			if(target instanceof EntityPlayer) {
				EntityPlayer defender = (EntityPlayer) target;
				ScalingFeastAPI.mutator.damageFoodStats(defender, 2 * getLevel(tool));
			}
			if(Math.random() < 0.15) {
				Collections.shuffle(effects);
				target.addPotionEffect(new PotionEffect(effects.get(0), 15 * 20, getLevel(tool), false, true));
			}
		}
		super.onHit(tool, player, target, damage, isCritical);
	}

	private static int getLevel(ItemStack tool) {
		return new ModifierNBT(TinkerUtil.getModifierTag(tool, "exhausting")).level;
	}
}
