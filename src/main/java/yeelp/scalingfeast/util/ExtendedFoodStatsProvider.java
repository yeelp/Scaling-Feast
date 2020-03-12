package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExtendedFoodStatsProvider implements ICapabilitySerializable<NBTTagCompound>
{
	@CapabilityInject(ICappedFoodStats.class)
	public static Capability<ICappedFoodStats> capFoodStat = null;
	
	private ICappedFoodStats instance = capFoodStat.getDefaultInstance();
	private static final String EFOOD = "Extended Food Level";
	private static final String ESAT = "Extended Saturation Level";
	private static final String EMAX = "Max Extended Food Level";
	private static class FoodFactory implements Callable<ICappedFoodStats>
	{

		@Override
		public ICappedFoodStats call() throws Exception 
		{
			return new ExtendedFoodStats();
		}
		
	}
	private static class FoodStorage implements IStorage<ICappedFoodStats>
	{

		@Override
		public NBTBase writeNBT(Capability<ICappedFoodStats> capability, ICappedFoodStats instance, EnumFacing side)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			if(instance instanceof ExtendedFoodStats)
			{
				nbt.setShort(EFOOD, instance.getFoodLevel());
				nbt.setFloat(ESAT, instance.getSatLevel());
				nbt.setShort(EMAX, instance.getMaxFoodLevel());
			}
			return nbt;
		}

		@Override
		public void readNBT(Capability<ICappedFoodStats> capability, ICappedFoodStats instance, EnumFacing side, NBTBase nbt)
		{
			if(instance instanceof ExtendedFoodStats)
			{
				NBTTagCompound data = (NBTTagCompound) nbt;
				instance.setMax(data.getShort(EMAX));
				instance.setFoodLevel(data.getShort(EFOOD));
				instance.setSatLevel(data.getFloat(ESAT));
			}
		}
		
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ICappedFoodStats.class, new FoodStorage(), new FoodFactory());
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		return capability == capFoodStat;
	}
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		return capability == capFoodStat ? capFoodStat.<T> cast(this.instance) : null;
	}
	@Override
	public NBTTagCompound serializeNBT() 
	{
		return (NBTTagCompound) capFoodStat.getStorage().writeNBT(capFoodStat, instance, null);
	}
	@Override
	public void deserializeNBT(NBTTagCompound nbt) 
	{
		capFoodStat.getStorage().readNBT(capFoodStat, instance, null, nbt);
	}
	
}
