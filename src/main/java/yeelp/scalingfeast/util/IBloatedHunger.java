package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Absorption for hunger
 * @author Yeelp
 *
 */
public interface IBloatedHunger extends ICapabilitySerializable<NBTTagShort>
{
	/**
	 * Set the bloated amount
	 * @param amount amount to set
	 */
	public void setBloatedAmount(short amount);
	
	/**
	 * Deduct a bloated amount 
	 * @param amount amount to deduct.
	 * @return the leftover amount that could not be deducted. If this returns a positive short, then this bloated amount is zero.
	 */
	public short deductBloatedAmount(short amount);
	
	/**
	 * Get the bloated amount
	 * @return the bloated amount
	 */
	public short getBloatedAmount();
}
