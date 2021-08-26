package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;

public interface FoodEfficiencyXPBonus {
	/**
	 * Get the attribute modifier value for a player with the set Food Efficiency
	 * Bonus type.
	 * 
	 * @param player player to target
	 * @return the value of the attribute modifier to give this player
	 */
	double getFoodEfficiencyBonus(EntityPlayer player);
}
