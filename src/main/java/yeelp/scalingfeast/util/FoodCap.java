package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;

/**
 * A class that implements the IFoodCap
 * @author Yeelp
 *
 */
public final class FoodCap implements IFoodCap
{
	private short max;
	
	/**
	 * Create a new FoodCap container with the default value
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

	
	public short getMaxFoodLevel(IFoodCapModifier mod)
	{
		float[] mods = mod.getModifier();
		return (short) Math.min(ScalingFeastAPI.accessor.getHungerHardCap(), (Math.max(1, (this.max + mods[0])*mods[1]*mods[2])));
	}

	public short getUnmodifiedMaxFoodLevel()
	{
		return this.max;
	}
	
	public void setMax(short max)
	{
		if(max <= 0)
		{
			this.max = 1;
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
	
	public void decreaseMax(short amount)
	{
		if(amount < 0 || this.max == 1)
		{
			return;
		}
		else if(this.max - amount < 1)
		{
			this.max = 1;
		}
		else if(this.max - amount < ModConfig.foodCap.starve.starveLowerCap)
		{
			this.max = (short)ModConfig.foodCap.starve.starveLowerCap;
		}
		else
		{
			this.max -= amount;
		}
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IFoodCap.class, new FoodStorage(), new FoodFactory());
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		return capability == FoodCapProvider.capFoodStat;
	}
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		return capability == FoodCapProvider.capFoodStat ? FoodCapProvider.capFoodStat.<T> cast(this) : null;
	}
	@Override
	public NBTTagShort serializeNBT() 
	{
		return new NBTTagShort(this.max);
	}
	@Override
	public void deserializeNBT(NBTTagShort nbt) 
	{
		this.max = nbt.getShort();
	}
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
			return new NBTTagShort(instance.getUnmodifiedMaxFoodLevel());
		}

		@Override
		public void readNBT(Capability<IFoodCap> capability, IFoodCap instance, EnumFacing side, NBTBase nbt)
		{
			instance.setMax(((NBTTagShort) nbt).getShort());
		}
		
	}
}
