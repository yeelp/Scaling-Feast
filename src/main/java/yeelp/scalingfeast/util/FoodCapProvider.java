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

public class FoodCapProvider implements ICapabilitySerializable<NBTTagCompound>
{
	@CapabilityInject(IFoodCap.class)
	public static Capability<IFoodCap> capFoodStat = null;
	
	private IFoodCap instance = capFoodStat.getDefaultInstance();
	private static final String EMAX = "Max Extended Food Level";
	private static class FoodFactory implements Callable<IFoodCap>
	{

		@Override
		public IFoodCap call() throws Exception 
		{
			return new FoodCap();
		}
		
	}
	private static class FoodStorage implements IStorage<IFoodCap>
	{

		@Override
		public NBTBase writeNBT(Capability<IFoodCap> capability, IFoodCap instance, EnumFacing side)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			if(instance instanceof FoodCap)
			{
				nbt.setShort(EMAX, instance.getMaxFoodLevel());
			}
			return nbt;
		}

		@Override
		public void readNBT(Capability<IFoodCap> capability, IFoodCap instance, EnumFacing side, NBTBase nbt)
		{
			if(instance instanceof FoodCap)
			{
				NBTTagCompound data = (NBTTagCompound) nbt;
				instance.setMax(data.getShort(EMAX));
			}
		}
		
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IFoodCap.class, new FoodStorage(), new FoodFactory());
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
