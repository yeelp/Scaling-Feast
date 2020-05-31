package yeelp.scalingfeast.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	private Map<String, Short> mods = new HashMap<String, Short>();

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
	public NBTTagList serializeNBT() 
	{
		NBTTagList lst = new NBTTagList();
		for(Entry<String, Short> entry : mods.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("id", entry.getKey());
			tag.setShort("modifier", entry.getValue());
			lst.appendTag(tag);
		}
		return lst;
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		this.mods = new HashMap<String, Short>();
		if(nbt instanceof NBTTagShort)
		{
			this.mods.put("modules", ((NBTTagShort) nbt).getShort());
		}
		else if(nbt instanceof NBTTagList)
		{
			NBTTagList lst = (NBTTagList) nbt;
			if(lst.getTagType() == 10) //10 is the ID for NBTTagCompounds
			{
				for(NBTBase tag : lst)
				{
					NBTTagCompound modifier = (NBTTagCompound) tag;
					this.mods.put(modifier.getString("id"), modifier.getShort("modifier"));
				}
			}
		}
	}
	
	@Override
	public short getModifier()
	{
		short mod = 0;
		for(short s : this.mods.values())
		{
			mod += s;
		}
		return mod;
	}
	
	@Override
	public short getModifier(String id)
	{
		Short val = this.mods.get(id);
		return val == null ? 0 : val.shortValue();
	}
	
	@Override
	public Map<String, Short> getAllModifiers()
	{
		return this.mods;
	}
	
	@Override
	public void setModifier(String id, short amount)
	{
		this.mods.put(id, amount);//this.mod = amount;
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IFoodCapModifier.class, new FoodModStorage(), new FoodModFactory());
	}

	private static class FoodModFactory implements Callable<IFoodCapModifier>
	{
		@Override
		public IFoodCapModifier call() throws Exception 
		{
			return new FoodCapModifier();
		}	
	}
	
	private static class FoodModStorage implements IStorage<IFoodCapModifier>
	{

		@Override
		public NBTBase writeNBT(Capability<IFoodCapModifier> capability, IFoodCapModifier instance, EnumFacing side) 
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IFoodCapModifier> capability, IFoodCapModifier instance, EnumFacing side, NBTBase nbt) 
		{
			instance.deserializeNBT(nbt);
		}
		
	}
}
