package yeelp.scalingfeast.api;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.SaturationScaling;

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
	
	/**
	 * Get the currently loaded saturation scaling
	 * @return the current SaturationScaling
	 */
	SaturationScaling getSaturationScaling();
	
	/**
	 * Get the current hunger hard cap.
	 * @return the current hunger hard cap, or {@link Short#MAX_VALUE} if none was set.
	 */
	short getHungerHardCap();
	
	/**
	 * Get the current saturation hard cap
	 * @return the saturation hard cap, or {@link Float#MAX_VALUE} if none was set.
	 */
	float getSaturationHardCap();
	
	/**
	 * Get the saturation cap for a specific player. That is, The highest amount of saturation they can ever have.
	 * @return
	 */
	float getPlayerSaturationCap(EntityPlayer player);
	
	/**
	 * Can a player lose max hunger by starving?
	 * @param player player to check
	 * @return true if that player can lose max hunger by starving
	 */
	boolean canPlayerLoseMaxHunger(EntityPlayer player);
}
