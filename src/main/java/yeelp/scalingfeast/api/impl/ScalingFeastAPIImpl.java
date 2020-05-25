package yeelp.scalingfeast.api.impl;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.IScalingFeastAccessor;
import yeelp.scalingfeast.api.IScalingFeastMutator;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

public enum ScalingFeastAPIImpl implements IScalingFeastAccessor, IScalingFeastMutator
{
	INSTANCE;
	
	private ScalingFeastAPIImpl()
	{
		ScalingFeastAPI.accessor = this;
		ScalingFeastAPI.mutator = this;
	}

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
}
