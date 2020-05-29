package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Capability for tracking food cap modifiers
 * @author Yeelp
 *
 */
public interface IFoodCapModifier extends ICapabilitySerializable<NBTBase>
{
	/**
	 * Get the final FoodCapModifier
	 * @return the amount to modify food stats by
	 */
	default short getModifier()
	{
		return 0;
	}
	
	/**
	 * Get a modifier by its id
	 * @param id
	 * @return the modifier associated with the id, or 0 if there was no modifier for this id.
	 */
	short getModifier(String id);
	
	/**
	 * Set the modifier value
	 * @param id identifier for this modifier amount
	 * @param amount
	 */
	void setModifier(String id, short amount);
}
