package yeelp.scalingfeast.api;

import net.minecraft.entity.player.EntityPlayer;

/**
 * A collection of methods for modifying properties about Scaling Feast. Typically for convenience.
 * @author Yeelp
 *
 */
public abstract interface IScalingFeastMutator
{
	/**
	 * Cap a player's food to the hard cap, if there is one
	 * @param player player to target
	 */
	void capPlayerHunger(EntityPlayer player);
	
	/**
	 * Cap a player's saturation, first by scaling, then by the hard cap.
	 * @param player player to target
	 */
	void capPlayerSaturation(EntityPlayer player);
	
	/**
	 * Tick a player's starvation tracker. If the count passes the threshold defined in the config, this method will also punish the player.
	 * @param player player to target
	 */
	void tickPlayerStarvationTracker(EntityPlayer player);
	
	/**
	 * Tick a player's starvation tracker multiple times If the count passes the threshold defined in the config, this method will punish the player appropriately.
	 * @param player player to target
	 * @param amount amount of times the tracker should be ticked.
	 */
	void tickPlayerStarvationTracker(EntityPlayer player, int amount);

	/**
	 * Deduct hunger/saturation from a player's foodstats by adding exhaustion. This lets this be reduced by Fasting and Iron Stomach
	 * @param player player to target
	 * @param amount amount of hunger/saturation to reduce. This is NOT the amount of exhaustion to add. The amount of foodstats actually reduced may vary if the targeted player has Iron Stomach or Fasting active.
	 */
	void damageFoodStats(EntityPlayer player, float amount);
	
	/**
	 * Directly deduct hunger/saturation from a player's foodstats. This effect will be observed instantly and cannot be blocked by Fasting or Iron Stomach, unlike {@link #damageFoodStats(EntityPlayer, float)} 
	 * @param player player to target
	 * @param amount amount of hunger/saturation to reduce.
	 */
	void deductFoodStats(EntityPlayer player, float amount);
}
