package yeelp.scalingfeast.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public interface MaxHungerXPBonus
{
	/**
	 * Get the attribute modifier value for a player with the set Max Hunger Bonus type.
	 * @param player player to target
	 * @return the value of the attribute modifier to give this player
	 */
	double getMaxHungerBonus(EntityPlayer player);
}
