package yeelp.scalingfeast.api.impl;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.MathHelper;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.api.IScalingFeastAccessor;
import yeelp.scalingfeast.api.IScalingFeastMutator;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.util.BloatedHungerProvider;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.FoodCapModifier.Operation;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IBloatedHunger;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.IStarveExhaustionTracker;
import yeelp.scalingfeast.util.SFAttributes;
import yeelp.scalingfeast.util.SaturationScaling;
import yeelp.scalingfeast.util.StarvationTrackerProvider;
import yeelp.scalingfeast.util.StarveExhaustionTrackerProvider;

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
	public IBloatedHunger getBloatedHunger(EntityPlayer player)
	{
		return BloatedHungerProvider.getBloatedHunger(player);
	}
	
	@Override
	public IStarveExhaustionTracker getStarveExhaustionTracker(EntityPlayer player)
	{
		return StarveExhaustionTrackerProvider.getStarveExhaustionTracker(player);
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
	
	@Override
	public IAttributeInstance getFoodEfficiency(EntityPlayer player)
	{
		return player.getAttributeMap().getAttributeInstance(SFAttributes.FOOD_EFFICIENCY);
	}
	
	@Override
	public IAttributeInstance getMaxHungerAttributeModifier(EntityPlayer player)
	{
		return player.getAttributeMap().getAttributeInstance(SFAttributes.MAX_HUNGER_MOD);
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
			AppleCoreAPI.mutator.setSaturation(player, cap);
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
			IFoodCapModifier foodCapMod = this.getFoodCapModifier(player);
			if(foodCap.getMaxFoodLevel(foodCapMod) > lowerBound)
			{
				float penalty = foodCapMod.getModifier("starvation");
				foodCapMod.setModifier("starvation", MathHelper.clamp(penalty-ModConfig.foodCap.starve.starveLoss, lowerBound, Integer.MAX_VALUE), FoodCapModifier.Operation.ADD);
			}
			if(ModConfig.foodCap.starve.doesFreqResetOnStarve)
			{
				tracker.reset();
			}
			else
			{
				tracker.setCount((short)(threshold - 1));
			}
			CapabilityHandler.syncMod(player);
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
		AppleCoreAPI.mutator.setSaturation(player, Math.max(currSat - amount, 0));
		AppleCoreAPI.mutator.setHunger(player, Math.max(currHunger - rem, 0));
	}
	
	@Override
	public void setFoodEfficiencyModifier(EntityPlayer player, UUID id, String name, double amount)
	{
		IAttributeInstance instance = getFoodEfficiency(player);
		AttributeModifier modifier = instance.getModifier(id);
		if(modifier != null)
		{
			instance.removeModifier(modifier);
		}
		instance.applyModifier(new AttributeModifier(id, name, amount, 2));
	}
	
	@Override
	public void setMaxHungerAttributeModifier(EntityPlayer player, UUID id, String name, double amount, byte op)
	{
		IAttributeInstance instance = getMaxHungerAttributeModifier(player);
		AttributeModifier modifier = instance.getModifier(id);
		if(modifier != null)
		{
			instance.removeModifier(modifier);
		}
		instance.applyModifier(new AttributeModifier(id, name, amount, op < 3 && op > -1 ? op : 0));
	}
	
	@Override
	public void removeFoodEfficiencyModifier(EntityPlayer player, UUID id)
	{
		getFoodEfficiency(player).removeModifier(id);
	}
	
	@Override
	public void removeMaxHungerAttributeModifier(EntityPlayer player, UUID id)
	{
		getMaxHungerAttributeModifier(player).removeModifier(id);
	}
	
	@Override
	public void addBloatedHunger(EntityPlayer player, short amount)
	{
		IBloatedHunger bloatedHunger = getBloatedHunger(player);
		bloatedHunger.setBloatedAmount((short) (bloatedHunger.getBloatedAmount() + amount));
		CapabilityHandler.syncBloatedHunger(player);
	}
	
	@Override
	public void setBloatedHunger(EntityPlayer player, short amount)
	{
		IBloatedHunger bloatedHunger = getBloatedHunger(player);
		bloatedHunger.setBloatedAmount(amount);
		CapabilityHandler.syncBloatedHunger(player);
	}
	
	@Override
	public void setModifier(EntityPlayer player, String id, float amount, byte op)
	{
		if(0 <= op && op < 3)
		{
			IFoodCapModifier mod = getFoodCapModifier(player);
			mod.setModifier(id, amount, Operation.values()[op]);
			short currMax = getFoodCap(player).getMaxFoodLevel(mod);
			FoodStats fs = player.getFoodStats();
			if(fs.getFoodLevel() > currMax)
			{
				AppleCoreAPI.mutator.setHunger(player, currMax);
				capPlayerSaturation(player);
			}
			if(fs.getSaturationLevel() > currMax)
			{
				AppleCoreAPI.mutator.setSaturation(player, currMax);
				capPlayerSaturation(player);
			}
			CapabilityHandler.syncMod(player);
		}
		else
		{
			throw new RuntimeException("op argument for setModifier must be either 0, 1 or 2!");
		}
	}
	
	@Override
	public void removeModifier(EntityPlayer player, String id)
	{
		IFoodCapModifier mod = getFoodCapModifier(player);
		mod.getAllModifiers().remove(id);
		CapabilityHandler.syncMod(player);
	}
	
	@Override 
	public void setUnmodifiedMaxHunger(EntityPlayer player, short amount)
	{
		IFoodCap cap = getFoodCap(player);
		cap.setMax(amount);
		CapabilityHandler.syncCap(player);
	}
	
	@Override
	public void addStarveExhaustion(EntityPlayer player, float amount)
	{
		getStarveExhaustionTracker(player).addExhaustion(player.getFoodStats().getFoodLevel(), amount);
		CapabilityHandler.syncStarveExhaust(player);
	}
}
