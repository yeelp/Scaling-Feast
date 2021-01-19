package yeelp.scalingfeast.util;

import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * A tracker for the amount of times a Hearty Shank has been eaten.
 * @author Yeelp
 *
 */
public interface IHeartyShankUsageTicker extends ICapabilitySerializable<NBTTagInt>
{
	/**
	 * Increment the counter.
	 */
	void inc();
	
	/**
	 * Get the count
	 * @return the count
	 */
	int getCount();
}
