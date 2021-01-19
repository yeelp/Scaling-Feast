package yeelp.scalingfeast.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.util.IFoodCapModifier;

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
	 * Increment a player's Hearty Shank usage ticker.
	 * @param player
	 */
	void tickPlayerShankUsageTicker(EntityPlayer player);
	
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
	
	/**
	 * Modify the exhaustion rate attribute for a player. If the exhaustion rate for this player already has a modifier with this id, the value is changed to {@code amount}
	 * <p>
	 * The exhaustion rate attribute will only use operation 2 for modifiers, since other operations are too niche to support. See <a href="http://minecraft.gamepedia.com/Attribute#Modifiers">Minecraft's description on Attribute Modifiers</a> for more info about this operation value.
	 * @param player player to target
	 * @param id the UUID for this modifier.
	 * @param name the name for this modifier. May not be unique.
	 * @param amount value for this modifier. This value represents the net percentage change for this modifier. That is, for a 50% increase to exhaustion, this argument would be {@code 0.5}. For a 50% decrease to exhaustion, this argument would be {@code -0.5}.
	 */
	void setFoodEfficiencyModifier(EntityPlayer player, UUID id, String name, double amount);
	
	/**
	 * Modify the max hunger for this player with an attribute modifier. If the max hunger modifier for this player already has a modifier with this id, the value is changed to {@code amount}
	 * See <a href="http://minecraft.gamepedia.com/Attribute#Modifiers">Minecraft's description on Attribute Modifiers</a> for more info about.
	 * @param player player to target
	 * @param id the id of the modifier
	 * @param name name of the modifier. May not be unique.
	 * @param amount value of this modifier.
	 * @param op operator for this modifier. Must be 0,1,2. Defaults to 0 if specified incorrectly.
	 */
	void setMaxHungerAttributeModifier(EntityPlayer player, UUID id, String name, double amount, byte op);
	
	/**
	 * Remove a modifier for a player's exhaustion rate.
	 * @param player player to target
	 * @param id the id of the modifier to remove.
	 */
	void removeFoodEfficiencyModifier(EntityPlayer player, UUID id);
	
	/**
	 * Remove a modifier for a player's max hunger attribute
	 * @param player player to target
	 * @param id the id of the modifier to remove.
	 */
	void removeMaxHungerAttributeModifier(EntityPlayer player, UUID id);
	
	/**
	 * Add an amount to a player's bloated hunger value.
	 * @param player player to target
	 * @param amount amount to add. If negative, the amount will be deducted, but will never go below zero.
	 */
	void addBloatedHunger(EntityPlayer player, short amount);
	
	/**
	 * Set a player's bloated hunger value to a certain amount.
	 * @param player player to target
	 * @param amount amount to set. If negative, will instead set it to zero.
	 */
	void setBloatedHunger(EntityPlayer player, short amount);
	
	/**
	 * Set a new food cap modifier.
	 * @param player player to target
	 * @param id the name of the modifier
	 * @param amount the amount
	 * @param op the operation to use. MUST be 0, 1 or 2 for add, multiply, and percentage multiply.
	 */
	void setModifier(EntityPlayer player, String id, float amount, byte op);
	
	/**
	 * Remove a modifier
	 * @param player
	 * @param id id of the modifier
	 */
	void removeModifier(EntityPlayer player, String id);
	
	/**
	 * Set a player's unmodified max hunger in their IFoodCap.
	 * @param player
	 * @param amount
	 */
	void setUnmodifiedMaxHunger(EntityPlayer player, short amount);
	
	/**
	 * Add exhaustion to a player's tracker. Only works while starving.
	 * @param player player to target
	 * @param amount amount to add. If negative, will deduct the amount instead - but will never go below zero.
	 */
	void addStarveExhaustion(EntityPlayer player, float amount);
}
