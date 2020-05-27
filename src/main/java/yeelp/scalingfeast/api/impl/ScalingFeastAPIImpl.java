package yeelp.scalingfeast.api.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.api.IScalingFeastAccessor;
import yeelp.scalingfeast.api.IScalingFeastMutator;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.SaturationScaling;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

public enum ScalingFeastAPIImpl implements IScalingFeastAccessor, IScalingFeastMutator
{
	INSTANCE;
	
	private short hungerCap;
	private float saturationCap;
	private SaturationScaling satScaling;
	
	private ScalingFeastAPIImpl()
	{
		ScalingFeastAPI.accessor = this;
		ScalingFeastAPI.mutator = this;
		
		this.hungerCap = (short) (ModConfig.foodCap.globalCap == -1 ? Short.MAX_VALUE : ModConfig.foodCap.globalCap); 
		this.saturationCap = (float) (ModConfig.foodCap.satCap == -1 ? Float.MAX_VALUE : ModConfig.foodCap.satCap);
		this.satScaling = ModConfig.foodCap.satScaling;
	}
	
	/******************************/
	/*          ACCESSOR          */
	/******************************/

	@Override
	public IFoodCap getFoodCap(EntityPlayer player)
	{
		return FoodCapProvider.getFoodCap(player);
	}

	@Override
	public IFoodCapModifier getFoodCapModifier(EntityPlayer player)
	{
		return FoodCapModifierProvider.getFoodMod(player);
	}

	@Override
	public IStarvationTracker getStarvationTracker(EntityPlayer player)
	{
		return StarvationTrackerProvider.getTracker(player);
	}

	@Override
	public SaturationScaling getSaturationScaling()
	{
		return this.satScaling;
	}

	@Override
	public short getHungerHardCap()
	{
		return this.hungerCap;
	}

	@Override
	public float getSaturationHardCap()
	{
		return this.saturationCap;
	}

	@Override
	public float getPlayerSaturationCap(EntityPlayer player)
	{
		float scaledSat = this.satScaling.getCap(player);
		return scaledSat < this.saturationCap ? scaledSat : this.saturationCap;
	}

	@Override
	public boolean canPlayerLoseMaxHunger(EntityPlayer player)
	{
		return this.getFoodCap(player).getUnmodifiedMaxFoodLevel() < ModConfig.foodCap.starve.starveLowerCap;
	}
	/*****************************/
	/*          MUTATOR          */
	/*****************************/
	
	@Override
	public void capPlayerHunger(EntityPlayer player)
	{
		FoodStats fs = player.getFoodStats();
		if(fs.getFoodLevel() > this.hungerCap)
		{
			AppleCoreAPI.mutator.setHunger(player, this.hungerCap);
			if(fs.getSaturationLevel() > fs.getFoodLevel())
			{
				AppleCoreAPI.mutator.setSaturation(player, fs.getFoodLevel());
			}
		}
	}

	@Override
	public void capPlayerSaturation(EntityPlayer player)
	{
		float cap = this.getPlayerSaturationCap(player);
		if(player.getFoodStats().getSaturationLevel() > cap)
		{
			//AppleCoreAPI.mutator.setSaturation(player, cap);
			player.getFoodStats().setFoodSaturationLevel(cap);
		}
	}

	@Override
	public void tickPlayerStarvationTracker(EntityPlayer player)
	{
		short threshold = (short) ModConfig.foodCap.starve.lossFreq;
		short lowerBound = (short) ModConfig.foodCap.starve.starveLowerCap;
		IStarvationTracker tracker = this.getStarvationTracker(player);
		tracker.tickStarvation(player.getFoodStats().getFoodLevel());
		checkAndPunishPlayer(player, tracker, threshold, lowerBound);
		CapabilityHandler.syncTracker(player);
	}

	@Override
	public void tickPlayerStarvationTracker(EntityPlayer player, int amount)
	{
		short threshold = (short) ModConfig.foodCap.starve.lossFreq;
		short lowerBound = (short) ModConfig.foodCap.starve.starveLowerCap;
		IStarvationTracker tracker = this.getStarvationTracker(player);
		int foodLevel = player.getFoodStats().getFoodLevel();
		for(int i = 0; i < amount; i++)
		{
			tracker.tickStarvation(foodLevel);
			checkAndPunishPlayer(player, tracker, threshold, lowerBound);
		}
		CapabilityHandler.syncTracker(player);
	}

	private void checkAndPunishPlayer(EntityPlayer player, IStarvationTracker tracker, short threshold, short lowerBound)
	{
		if(tracker.getCount() >= threshold)
		{
			IFoodCap foodCap = this.getFoodCap(player);
			if(foodCap.getUnmodifiedMaxFoodLevel() > lowerBound)
			{
				foodCap.decreaseMax((short) ModConfig.foodCap.starve.starveLoss);
			}
			if(ModConfig.foodCap.starve.doesFreqResetOnStarve)
			{
				tracker.reset();
			}
			else
			{
				tracker.setCount((short)(threshold - 1));
			}
			CapabilityHandler.syncCap(player);
		}
	}
	
	@Override
	public void damageFoodStats(EntityPlayer player, float amount)
	{
		player.addExhaustion(AppleCoreAPI.accessor.getMaxExhaustion(player)*amount);
	}

	@Override
	public void deductFoodStats(EntityPlayer player, float amount)
	{
		float currSat = player.getFoodStats().getSaturationLevel();
		int currHunger = player.getFoodStats().getFoodLevel();
		int rem = (int) Math.floor(currSat < amount ? amount - currSat : 0);
		AppleCoreAPI.mutator.setSaturation(player, Math.min(currSat - amount, 0));
		AppleCoreAPI.mutator.setHunger(player, Math.min(currHunger - rem, 0));
	}
}
