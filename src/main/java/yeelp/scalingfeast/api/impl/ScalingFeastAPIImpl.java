package yeelp.scalingfeast.api.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.MathHelper;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.IScalingFeastAccessor;
import yeelp.scalingfeast.api.IScalingFeastMutator;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.lib.SaturationScaling;

public enum ScalingFeastAPIImpl implements IScalingFeastAccessor, IScalingFeastMutator {
	INSTANCE;

	private ScalingFeastAPIImpl() {
		ScalingFeastAPI.accessor = this;
		ScalingFeastAPI.mutator = this;
	}

	/******************************/
	/* ACCESSOR */
	/******************************/

	@Override
	public SaturationScaling getSaturationScaling() {
		return ModConfig.general.satScaling;
	}

	@Override
	public short getHungerHardCap() {
		return (short) (ModConfig.general.globalCap == -1 ? Short.MAX_VALUE : ModConfig.general.globalCap);
	}

	@Override
	public float getSaturationHardCap() {
		return (float) (ModConfig.general.satCap == -1 ? Float.MAX_VALUE : ModConfig.general.satCap);
	}

	@Override
	public float getPlayerSaturationCap(EntityPlayer player) {
		return MathHelper.clamp(this.getSaturationScaling().getCap(player), 0, this.getSaturationHardCap());
	}

	@Override
	public boolean canPlayerLoseMaxHunger(EntityPlayer player) {
		return AppleCoreAPI.accessor.getMaxHunger(player) < ModConfig.features.starve.tracker.starveLowerCap;
	}

	/*****************************/
	/* MUTATOR */
	/*****************************/

	@Override
	public void capPlayerHunger(EntityPlayer player) {
		FoodStats fs = player.getFoodStats();
		if(fs.getFoodLevel() > this.getHungerHardCap()) {
			AppleCoreAPI.mutator.setHunger(player, this.getHungerHardCap());
			if(fs.getSaturationLevel() > fs.getFoodLevel()) {
				AppleCoreAPI.mutator.setSaturation(player, fs.getFoodLevel());
			}
		}
	}

	@Override
	public void capPlayerSaturation(EntityPlayer player) {
		float cap = this.getPlayerSaturationCap(player);
		if(player.getFoodStats().getSaturationLevel() > cap) {
			AppleCoreAPI.mutator.setSaturation(player, cap);
		}
	}

	@Override
	public void damageFoodStats(EntityPlayer player, float amount) {
		player.addExhaustion(AppleCoreAPI.accessor.getMaxExhaustion(player) * amount);
	}

	@Override
	public void deductFoodStats(EntityPlayer player, float amount) {
		float currSat = player.getFoodStats().getSaturationLevel();
		int currHunger = player.getFoodStats().getFoodLevel();
		int rem = (int) Math.floor(currSat < amount ? amount - currSat : 0);
		AppleCoreAPI.mutator.setSaturation(player, Math.max(currSat - amount, 0));
		AppleCoreAPI.mutator.setHunger(player, Math.max(currHunger - rem, 0));
	}
}
