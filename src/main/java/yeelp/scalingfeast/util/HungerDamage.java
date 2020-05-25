package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;

/**
 * A collection of methods for determining damage to a player's food stats.
 * @author Yeelp
 *
 */
public final class HungerDamage
{
	/**
	 * Damage a player's saturation directly
	 * @param player player to target
	 * @param amount amount of saturation to deduct
	 * @return the remaining amount of saturation that could not be deducted from the player's saturation, or 0 if the entire amount was deducted.
	 */
	public static final float damagePierceSaturation(EntityPlayer player, float amount)
	{
		if(amount <= 0)
		{
			return 0;
		}
		float rem = player.getFoodStats().getSaturationLevel() < amount ? amount - player.getFoodStats().getSaturationLevel() : 0;
		AppleCoreAPI.mutator.setSaturation(player, Math.min(player.getFoodStats().getSaturationLevel() - amount, 0));
		return rem;
	}
	
	/**
	 * Damage a player's hunger directly
	 * @param player player to target
	 * @param amount amount of hunger to deduct
	 * @return the remaining amount of hunger that could not be deducted from the player's hunger, or 0 if all of it was deducted.
	 */
	public static final int damagePierceHunger(EntityPlayer player, int amount)
	{
		if(amount <= 0)
		{
			return 0;
		}
		int rem = player.getFoodStats().getFoodLevel() < amount ? amount - player.getFoodStats().getFoodLevel() : 0;
		AppleCoreAPI.mutator.setHunger(player, Math.min(player.getFoodStats().getFoodLevel() - amount, 0));
		return rem;
	}
	
	/**
	 * Damage a player's food stats directly. Deducts saturation, then hunger.
	 * @param player player to target
	 * @param amount amount to deduct.
	 */
	public static final void damagePierceFoodStats(EntityPlayer player, float amount)
	{
		damagePierceHunger(player, (int)Math.floor(damagePierceSaturation(player, amount)));
	}
	
	/**
	 * Damage a player's food stats indirectly, by adding exhaustion. This lets it be reduced by Iron Stomach and Fasting
	 * @param player player to target
	 * @param amount amount to reduce
	 */
	public static final void damageFoodStats(EntityPlayer player, float amount)
	{
		player.addExhaustion(AppleCoreAPI.accessor.getMaxExhaustion(player)*amount);
	}
}
