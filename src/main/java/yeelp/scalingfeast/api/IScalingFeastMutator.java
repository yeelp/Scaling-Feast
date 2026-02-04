package yeelp.scalingfeast.api;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A collection of methods for modifying properties about Scaling Feast.
 * 
 * @author Yeelp
 *
 */
@ParametersAreNonnullByDefault
public interface IScalingFeastMutator {
	/**
	 * Cap a player's food to the hard cap, if there is one
	 * 
	 * @param player player to target
	 */
	void capPlayerHunger(EntityPlayer player);

	/**
	 * Cap a player's saturation, first by scaling, then by the hard cap.
	 * 
	 * @param player player to target
	 */
	void capPlayerSaturation(EntityPlayer player);

	/**
	 * Deduct hunger/saturation from a player's food stats by adding exhaustion. This
	 * lets this be reduced by Fasting and Iron Stomach
	 * 
	 * @param player player to target
	 * @param amount amount of hunger/saturation to reduce. This is NOT the amount
	 *               of exhaustion to add. The amount of food stats actually reduced
	 *               may vary if the targeted player has Iron Stomach or Fasting
	 *               active.
	 */
	void damageFoodStats(EntityPlayer player, float amount);

	/**
	 * Directly deduct hunger/saturation from a player's food stats. This effect will
	 * be observed instantly and cannot be blocked by Fasting or Iron Stomach,
	 * unlike {@link #damageFoodStats(EntityPlayer, float)}
	 * 
	 * @param player player to target
	 * @param amount amount of hunger/saturation to reduce.
	 */
	void deductFoodStats(EntityPlayer player, float amount);
	
	/**
	 * Directly add hunger/saturation to a player's food stats. First add to hunger, then any remaining
	 * amount will overflow and add to saturation.
	 * @param player player to target
	 * @param amount amount of hunger/saturation to add
	 */
	void addFoodStatsWithOverflow(EntityPlayer player, float amount);
}
