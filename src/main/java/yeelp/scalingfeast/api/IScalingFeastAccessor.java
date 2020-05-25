package yeelp.scalingfeast.api;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;

/**
 * Collection of ways to get various properties from Scaling Feast
 * @author Yeelp
 *
 */
public abstract interface IScalingFeastAccessor
{
	/**
	 * Get a player's food cap - an instance of {@link #IFoodCap}
	 * @param player
	 * @return That player's food cap.
	 */
	IFoodCap getFoodCap(EntityPlayer player);
	
	/**
	 * Get a player's food cap modifier - an instance of {@link #IFoodCapModifier}
	 * @param player
	 * @return That player's food cap modifier
	 */
	IFoodCapModifier getFoodCapModifier(EntityPlayer player);

	/**
	 * Get a player's starvation tracker - an instance of {@link #IStarvationTracker}
	 * @param player
	 * @return that player's starvation tracker
	 */
	IStarvationTracker getStarvationTracker(EntityPlayer player);
	
	/**
	 * Get a player's modified food cap. This just calls {@link IFoodCap#getMaxFoodLevel(IFoodCapModifier)} but is included for convenience.
	 * @param player
	 * @return this player's modified food cap.
	 */
	default short getModifiedFoodCap(EntityPlayer player) 
	{
		return getFoodCap(player).getMaxFoodLevel(getFoodCapModifier(player));
	}
	
	
}
