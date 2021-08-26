package yeelp.scalingfeast.capability;

import net.minecraft.nbt.NBTTagShort;
import yeelp.scalingfeast.capability.impl.BloatedHunger;

/**
 * Absorption for hunger
 * 
 * @author Yeelp
 *
 */
public interface IBloatedHunger extends SFSingleValueCapability<Short, NBTTagShort> {

	/**
	 * Deduct a bloated amount
	 * 
	 * @param amount amount to deduct.
	 * @return the leftover amount that could not be deducted. If this returns a
	 *         positive short, then this bloated amount is zero.
	 */
	public short deductBloatedAmount(short amount);
	
	public static void register() {
		SFCapabilityBase.register(IBloatedHunger.class, NBTTagShort.class, BloatedHunger::new);
	}
}
