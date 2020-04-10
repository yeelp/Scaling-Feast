package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * A class that implements the IFoodCapModifier
 * @author Yeelp
 *
 */
public class FoodCapModifier implements IFoodCapModifier 
{
	private short mod;

	/**
	 * Create a new FoodCapModifier
	 */
	public FoodCapModifier()
	{
		this.mod = 0;
	}
	
	/**
	 * Create a new FoodCapModifer with the specified amount
	 * @param mod the starting modifier
	 */
	public FoodCapModifier(short mod)
	{
		this.mod = mod;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == FoodCapModifierProvider.foodCapMod;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		return capability == FoodCapModifierProvider.foodCapMod ? FoodCapModifierProvider.foodCapMod.<T> cast(this) : null;
	}

	@Override
	public NBTTagShort serializeNBT() 
	{
		return new NBTTagShort(this.mod);
	}

	@Override
	public void deserializeNBT(NBTTagShort nbt)
	{
		this.mod = nbt.getShort();
	}
	
	@Override
	public short getModifier()
	{
		return mod;
	}
	
	@Override
	public void setModifier(short amount)
	{
		this.mod = amount;
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IFoodCapModifier.class, new FoodModStorage(), new FoodModFactory());
	}

	public static class FoodModFactory implements Callable<IFoodCapModifier>
	{
		@Override
		public IFoodCapModifier call() throws Exception 
		{
			return new FoodCapModifier();
		}	
	}
	
	public static class FoodModStorage implements IStorage<IFoodCapModifier>
	{

		@Override
		public NBTBase writeNBT(Capability<IFoodCapModifier> capability, IFoodCapModifier instance, EnumFacing side) 
		{
			return new NBTTagShort(instance.getModifier());
		}

		@Override
		public void readNBT(Capability<IFoodCapModifier> capability, IFoodCapModifier instance, EnumFacing side, NBTBase nbt) 
		{
			instance.setModifier(((NBTTagShort) nbt).getShort());
		}
		
	}
}
