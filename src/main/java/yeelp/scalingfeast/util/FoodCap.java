package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import yeelp.scalingfeast.ModConsts;

/**
 * A container to hold extended food values. This is mainly a sanity check to ensure that FoodStatsMap syncs entries, so to speak
 * @author Yeelp
 *
 */
public class FoodCap implements IFoodCap
{
	private short max;
	
	/**
	 * Create a new FoodCap container with default value
	 */
	public FoodCap()
	{
		this.max = ModConsts.VANILLA_MAX_HUNGER;
	}
	/**
	 * Create a new FoodCap container with the specified max.
	 * @param max the maximum food level this container can hold
	 */
	public FoodCap(short max)
	{
		this.max = max;
	}
	/**
	 * Get this container's maximum food level
	 * @return this container's maximum food level
	 */
	public short getMaxFoodLevel()
	{
		return this.max;
	}

	public void setMax(short max)
	{
		if(max < 0)
		{
			this.max = 0;
		}
		else
		{
			this.max = max;
		}
	}

	public void increaseMax(short amount)
	{
		if(amount < 0)
		{
			return;
		}
		else
		{
			this.max += amount;
		}
	}
}
