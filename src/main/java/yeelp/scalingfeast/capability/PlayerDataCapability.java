package yeelp.scalingfeast.capability;

import java.util.UUID;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.scalingfeast.util.ExtendedFoodStats;
import yeelp.scalingfeast.util.FoodStatsMap;

public class PlayerDataCapability
{
	private static final String EFOOD = "Extended Food Level";
	private static final String ESAT = "Extended Saturation Level";
	private static final String EMAX = "Max Extended Food Level";
	
	/**
	 * Register this capability
	 */
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ExtendedFoodStats.class, new NBTStorage(), ExtendedFoodStats::new);
	}
	private static class NBTStorage implements IStorage<ExtendedFoodStats>
	{

		@Override
		public NBTBase writeNBT(Capability<ExtendedFoodStats> capability, ExtendedFoodStats instance, EnumFacing side)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setShort(EFOOD, instance.getFoodLevel());
			nbt.setFloat(ESAT, instance.getSatLevel());
			nbt.setShort(EMAX, instance.getMaxFoodLevel());
			return nbt;
		}

		@Override
		public void readNBT(Capability<ExtendedFoodStats> capability, ExtendedFoodStats instance, EnumFacing side, NBTBase nbt) 
		{
			NBTTagCompound compound = (NBTTagCompound) nbt;
			instance = new ExtendedFoodStats(compound.getShort(EFOOD), compound.getFloat(ESAT), compound.getShort(EMAX));
			
		}
		
	}
}
