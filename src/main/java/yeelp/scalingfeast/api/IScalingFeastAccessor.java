package yeelp.scalingfeast.api;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.lib.SaturationScaling;

/**
 * Collection of ways to get various properties from Scaling Feast
 * 
 * @author Yeelp
 *
 */
public interface IScalingFeastAccessor {
	
	/**
	 * Get the currently loaded saturation scaling
	 * 
	 * @return the current SaturationScaling
	 */
	SaturationScaling getSaturationScaling();

	/**
	 * Get the current hunger hard cap.
	 * 
	 * @return the current hunger hard cap, or {@link Short#MAX_VALUE} if none was
	 *         set.
	 */
	short getHungerHardCap();

	/**
	 * Get the current saturation hard cap
	 * 
	 * @return the saturation hard cap, or {@link Float#MAX_VALUE} if none was set.
	 */
	float getSaturationHardCap();

	/**
	 * Get the saturation cap for a specific player. That is, The highest amount of
	 * saturation they can ever have.
	 * 
	 * @return
	 */
	float getPlayerSaturationCap(EntityPlayer player);

	/**
	 * Can a player lose max hunger by starving?
	 * 
	 * @param player player to check
	 * @return true if that player can lose max hunger by starving
	 */
	boolean canPlayerLoseMaxHunger(EntityPlayer player);
	
	/**
	 * Get SFFoodStats for a player. SFFoodStats contains information about the player's food related capabilities and attributes.
	 * @param player
	 * @return A lazy SFFoodStats container. The actual capabilities and attributes are only fetched when needed and stored in this instance. All changes to this instance are synced to the server when needed.
	 */
	default SFFoodStats getSFFoodStats(EntityPlayer player) {
		return new SFFoodStats(player);
	}
}
