package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * A container to hold extended food values. This is mainly a sanity check to ensure that FoodStatsMap syncs entries, so to speak
 * @author Yeelp
 *
 */
public class ExtendedFoodStats implements ICappedFoodStats
{
	
	
	private short foodLevel;
	private float satLevel;
	private short max;
	
	/**
	 * Create a new ExtendedFoodStats container with default values
	 */
	public ExtendedFoodStats()
	{
		this.foodLevel = 0;
		this.satLevel = 0.0f;
		this.max = 0;
	}
	/**
	 * Create a new ExtendedFoodStats container. Upon creation, this container ensures that its caps are being enforced.
	 * @param foodLevel the extra food level
	 * @param satLevel the extra saturation level
	 * @param max the maximum food level this container can hold
	 */
	public ExtendedFoodStats(short foodLevel, float satLevel, short max)
	{
		this.foodLevel = foodLevel;
		this.satLevel = satLevel;
		this.max = max;
		this.enforceCaps();
	}
	/**
	 * Get this container's food level
	 * @return This container's food level
	 */
	public short getFoodLevel()
	{
		return this.foodLevel;
	}
	/**
	 * Get this container's saturation level
	 * @return This container's saturation level
	 */
	public float getSatLevel()
	{
		return this.satLevel;
	}
	/**
	 * Get this container's maximum food level
	 * @return this container's maximum food level
	 */
	public short getMaxFoodLevel()
	{
		return this.max;
	}
	/**
	 * Set this container's food level. This method respects this container's maximum food level.
	 * Any attempt to set this container's food level greater than its maximum allowed amount will have its food level instead set to the maximum.
	 * Any attempt to set this container's food level to a negative number will instead set it to 0.
	 * @param foodLevel The food level to set
	 */
	public void setFoodLevel(short foodLevel)
	{
		if(foodLevel < 0)
		{
			this.foodLevel = 0;
		}
		else
		{
			this.foodLevel = foodLevel;
		}
		this.enforceCaps();
	}
	/**
	 * Set this container's saturation level. This method respects Minecraft's basic hunger and saturation mechanics. That is, the saturation level can never exceed the food level.
	 * Any attempt to set this container's saturation level greater than its food level will have its saturation level instead set to its food level.
	 * Any attempt to set this container's saturation level to a negative value will instead set it to 0.
	 * @param satLevel The saturation level to set
	 * @see <a href = "https://minecraft.gamepedia.com/Hunger#Mechanics">Minecraft's basic hunger mechanics</a>
	 */
	public void setSatLevel(float satLevel)
	{
		if(satLevel < 0)
		{
			this.satLevel = 0;
		}
		else
		{
			this.satLevel = satLevel;
		}
		this.enforceCaps();
	}
	/**
	 * Set this container's maximum food level. Any attempt to set this value to a negative number will instead set it to 0.
	 * This method will update the container's food stats such that:
	 * 		- This container's food level <= this container's max cap
	 * 		- This container's saturation level <= this container's food level
	 * The second point comes from Minecraft's basic hunger mechanics.
	 * @param max
	 * @see <a href="https://minecraft.gamepedia.com/Hunger#Mechanics">Minecraft's basic hunger mechanics</a>
	 */
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
		this.enforceCaps();
	}
	/**
	 * Add food values to this container. This method respects this container's maximum food level. 
	 * Any attempt to add food values such that this container's food level exceeds this container's max food cap will instead set this container's food level to this container's max level
	 * Further, this method also respects Minecraft's basic hunger mechanics. Any attempt to add saturation beyond this container's food level does nothing.
	 * @param foodLevel The food level to add
	 * @param satLevel The saturation level to add
	 * @see <a ref="https://minecraft.gamepedia.com/Hunger#Mechanics">Minecraft's basic hunger mechanic</a>
	 */
	public void addFoodStats(short foodLevel, float satLevel)
	{
		if(foodLevel > 0)
		{
			this.foodLevel += foodLevel;
		}
		if(satLevel > 0)
		{
			this.satLevel += satLevel;
		}
		this.enforceCaps();
	}
	
	private void enforceCaps()
	{
		if(this.foodLevel > this.max)
		{
			this.foodLevel = max;
		}
		if(this.satLevel > this.foodLevel)
		{
			this.satLevel = this.foodLevel;
		}
	}
}
