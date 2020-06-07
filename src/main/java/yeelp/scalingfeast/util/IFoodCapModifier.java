package yeelp.scalingfeast.util;


import java.util.Map;

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
	 * @return the amount to modify food stats by. The first elements is the result of all add operations, the second is the result of all multiply operators and the third is the result of all operation 2 multiply operators.
	 */
	default float[] getModifier()
	{
		return new float[] {0, 1, 1};
	}
	
	/**
	 * Get a modifier by its id
	 * @param id
	 * @return the modifier associated with the id, or 0 if there was no modifier for this id.
	 */
	float getModifier(String id);
	
	Map<String, FoodCapModifier.Modifier> getAllModifiers();
	
	/**
	 * Set the modifier value
	 * @param id identifier for this modifier amount
	 * @param amount
	 * @param op operation for this modifier
	 */
	void setModifier(String id, float amount, FoodCapModifier.Operation op);
}
