package yeelp.scalingfeast.api.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.api.IScalingFeastAccessor;
import yeelp.scalingfeast.api.IScalingFeastMutator;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.SaturationScaling;
import yeelp.scalingfeast.util.XPBonusType;

public enum ScalingFeastAPIImpl implements IScalingFeastAccessor, IScalingFeastMutator {
	INSTANCE;

	private short hungerCap;
	private float saturationCap;
	private SaturationScaling satScaling;
	private ScalingFeastAPIImpl() {
		ScalingFeastAPI.accessor = this;
		ScalingFeastAPI.mutator = this;

		this.updateValues();
	}

	public void updateValues() {
		this.hungerCap = (short) (ModConfig.general.globalCap == -1 ? Short.MAX_VALUE : ModConfig.general.globalCap);
		this.saturationCap = (float) (ModConfig.general.satCap == -1 ? Float.MAX_VALUE : ModConfig.general.satCap);
		this.satScaling = ModConfig.general.satScaling;
		XPBonusType.updateRewards();
	}

	/******************************/
	/* ACCESSOR */
	/******************************/

	@Override
	public SaturationScaling getSaturationScaling() {
		return this.satScaling;
	}

	@Override
	public short getHungerHardCap() {
		return this.hungerCap;
	}

	@Override
	public float getSaturationHardCap() {
		return this.saturationCap;
	}

	@Override
	public float getPlayerSaturationCap(EntityPlayer player) {
		float scaledSat = this.satScaling.getCap(player);
		return scaledSat < this.saturationCap ? scaledSat : this.saturationCap;
	}

	@Override
	public boolean canPlayerLoseMaxHunger(EntityPlayer player) {
		return AppleCoreAPI.accessor.getMaxHunger(player) < ModConfig.features.starve.starveLowerCap;
	}

	/*****************************/
	/* MUTATOR */
	/*****************************/

	@Override
	public void capPlayerHunger(EntityPlayer player) {
		FoodStats fs = player.getFoodStats();
		if(fs.getFoodLevel() > this.hungerCap) {
			AppleCoreAPI.mutator.setHunger(player, this.hungerCap);
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
