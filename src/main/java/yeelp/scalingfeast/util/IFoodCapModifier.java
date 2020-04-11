package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Capability for tracking food cap modifiers
 * @author Yeelp
 *
 */
public interface IFoodCapModifier extends ICapabilitySerializable<NBTTagShort>
{
	/**
	 * Get the FoodCapModifier
	 * @return the amount to modify food values by
	 */
	default short getModifier()
	{
		return 0;
	}
	
	/**
	 * Set the modifier value
	 * @param amount
	 */
	void setModifier(short amount);
}
