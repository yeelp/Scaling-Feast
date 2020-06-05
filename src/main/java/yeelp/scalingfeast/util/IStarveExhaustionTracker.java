package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import squeek.applecore.api.AppleCoreAPI;

/**
 * A way to track exhaustion since going to zero hunger.
 * @author Yeelp
 *
 */
public interface IStarveExhaustionTracker extends ICapabilitySerializable<NBTTagFloat>
{
	/**
	 * Add exhaustion to this tracker. Only works when hunger is zero
	 * @param hunger hunger value to check against
	 * @param amount amount of exhaustion to add
	 */
	void addExhaustion(int hunger, float amount);
	
	/**
	 * Get a player's total exhaustion since reaching zero hunger.
	 * @return the total exhaustion.
	 */
	float getTotalExhaustion();
	
	/**
	 * Reset this tracker.
	 */
	void reset();
	
	/**
	 * Get the total amount of bonus damage to deal to this player when they starve.
	 * @param player player to target
	 * @return the amount of bonus damage to deal.
	 */
	default int getTotalDamage(EntityPlayer player)
	{
		return (int) Math.floor(getTotalExhaustion()/AppleCoreAPI.accessor.getMaxExhaustion(player));
	}
}
