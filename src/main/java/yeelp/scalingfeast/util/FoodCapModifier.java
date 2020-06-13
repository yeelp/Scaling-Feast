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
	private Map<String, Modifier> mods = new HashMap<String, Modifier>();
	public class Modifier
	{
		private float value;
		private Operation op;
		/**
		 * Build a new modifier for the food cap
		 * @param value value for the modifier
		 * @param op operation to perform.
		 */
		public Modifier(float value, Operation op)
		{
			this.value = value;
			this.op = op;
		}
		/**
		 * Get the value of this modifier
		 * @return the value
		 */
		public float getValue()
		{
			return this.value;
		}
		/**
		 * Get the operation of this modifier
		 * @return the operation
		 */
		public Operation getOp()
		{
			return this.op;
		}
	}
	public enum Operation
	{
		ADD,
		PERCENT_STACK_ADDITVELY,
		PERCENT_STACK_MULTIPLICATIVELY;
	}

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
		for(Entry<String, Modifier> entry : mods.entrySet())
		{
			Modifier mod = entry.getValue();
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("id", entry.getKey());
			tag.setFloat("modifier", mod.value);
			tag.setByte("op", (byte) mod.op.ordinal());
			lst.appendTag(tag);
		}
		return lst;
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		this.mods = new HashMap<String, Modifier>();
		if(nbt instanceof NBTTagShort)
		{
			this.mods.put("modules", new Modifier(((NBTTagShort) nbt).getShort(), Operation.ADD));
		}
		else if(nbt instanceof NBTTagList)
		{
			NBTTagList lst = (NBTTagList) nbt;
			if(lst.getTagType() == 10) //10 is the ID for NBTTagCompounds
			{
				for(NBTBase tag : lst)
				{
					NBTTagCompound modifier = (NBTTagCompound) tag;
					float amount = modifier.getFloat("modifier");
					String name = modifier.getString("id");
					byte opByte = modifier.getByte("op"); //will be zero if it doesn't exist. This is fine, since that will default to add.
					Operation op = Operation.values()[(int) (0 <= opByte && opByte <= 2 ? opByte : 0)]; 
					this.mods.put(modifier.getString("id"), new Modifier(amount, op));
				}
			}
		}
	}
	
	@Override
	public float[] getModifier()
	{
		float addMod = 0;
		float multMod = 1;
		float percentMod = 1;
		for(Modifier m : mods.values())
		{
			switch(m.op)
			{
				case ADD:
					addMod += m.value;
					break;
				case PERCENT_STACK_ADDITVELY:
					multMod += m.value;
					break;
				case PERCENT_STACK_MULTIPLICATIVELY:
					percentMod *= 1 + m.value;
					break;
				default:
					break;
					
			}
		}
		return new float[] {addMod, multMod, percentMod};
	}
	
	@Override
	public float getModifier(String id)
	{
		Modifier mod = this.mods.get(id);
		return mod == null ? 0 : mod.value;
	}
	
	@Override
	public Map<String, Modifier> getAllModifiers()
	{
		return this.mods;
	}
	
	@Override
	public void setModifier(String id, float amount, Operation op)
	{
		this.mods.put(id, new Modifier(amount, op));//this.mod = amount;
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
