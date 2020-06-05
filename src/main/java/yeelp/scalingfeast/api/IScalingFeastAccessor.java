package yeelp.scalingfeast.api;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.util.IBloatedHunger;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.IStarveExhaustionTracker;
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
	 * Get a player's bloated hunger - an instance of {@link #IBloatedHunger}
	 * @param player
	 * @return that player's bloated hunger
	 */
	IBloatedHunger getBloatedHunger(EntityPlayer player);
	
	/**
	 * Get a player's exhaustion tracker for tracker exhaustion at zero hunger - an instance of {@link #IStarveExhaustionTracker}
	 * @param player
	 * @return that player's exhaustion tracker for zero hunger.
	 */
	IStarveExhaustionTracker getStarveExhaustionTracker(EntityPlayer player);
	
	/**
	 * Get a player's bloated hunger amount. This just calls {@link IBloatedHunger#getBloatedAmount()} but is included for convenience.
	 * @param player
	 * @return that player's bloated hunger amount
	 */
	default short getBloatedHungerAmount(EntityPlayer player)
	{
		return getBloatedHunger(player).getBloatedAmount();
	}
	
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
	 * Get the bonus damage to deal to the player on starvation.
	 * @param player
	 * @return the amount of extra damage to do from exhaustion.
	 */
	default int getBonusExhaustionDamage(EntityPlayer player)
	{
		return getStarveExhaustionTracker(player).getTotalDamage(player);
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
	
	/**
	 * Get the exhaustion rate attribute for this player.
	 * @param player player to get this attribute for
	 * @return The IAttribute with the exhaustion rate for this player.
	 */
	IAttributeInstance getFoodEfficiency(EntityPlayer player);
	
	/**
	 * Get the max hunger modifier from attributes for a player.
	 * @param player player to target
	 * @return the IAttribute with the modifier for max hunger for this player. This is NOT the actual max hunger of this player.
	 * Use {@link #getFoodCap(EntityPlayer)} to get the actual capability. This is just the modifiers for max hunger from attributes.
	 */
	IAttributeInstance getMaxHungerAttributeModifier(EntityPlayer player);
}
