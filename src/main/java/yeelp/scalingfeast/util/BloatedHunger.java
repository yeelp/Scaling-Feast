package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class BloatedHunger implements IBloatedHunger
{
	private short bloatedAmount;
	
	/**
	 * Create a new BloatedHunger set at 0;
	 */
	public BloatedHunger()
	{
		this.bloatedAmount = 0;
	}
	
	/**
	 * Create a new BloatedHunger.
	 * @param startingAmount the amount this BloatedHunger should have.
	 */
	public BloatedHunger(short startingAmount)
	{
		this.bloatedAmount = startingAmount;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == BloatedHungerProvider.bloatedHunger;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == BloatedHungerProvider.bloatedHunger ? BloatedHungerProvider.bloatedHunger.<T> cast(this) : null;
	}

	@Override
	public NBTTagShort serializeNBT()
	{
		return new NBTTagShort(this.bloatedAmount);
	}

	@Override
	public void deserializeNBT(NBTTagShort nbt)
	{
		this.bloatedAmount = nbt.getShort();
	}

	@Override
	public void setBloatedAmount(short amount)
	{
		if(amount < 0)
		{
			amount = 0;
		}
		this.bloatedAmount = amount;
	}

	@Override
	public short deductBloatedAmount(short amount)
	{
		if(amount < 0)
		{
			return 0;
		}
		short deduction = amount >= this.bloatedAmount ? this.bloatedAmount : amount;
		this.bloatedAmount -= deduction;
		return (short) (amount - deduction);
	}
	
	@Override
	public short getBloatedAmount()
	{
		return this.bloatedAmount;
	}
	
	/**
	 * Register this capability
	 */
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IBloatedHunger.class, new BloatedHungerStorage(), new BloatedHungerFactory());
	}
	
	private static class BloatedHungerFactory implements Callable<IBloatedHunger>
	{
		@Override
		public IBloatedHunger call() throws Exception
		{
			return new BloatedHunger();
		}	
	}
	
	private static class BloatedHungerStorage implements IStorage<IBloatedHunger>
	{

		@Override
		public NBTBase writeNBT(Capability<IBloatedHunger> capability, IBloatedHunger instance, EnumFacing side)
		{
			return new NBTTagShort(instance.getBloatedAmount());
		}

		@Override
		public void readNBT(Capability<IBloatedHunger> capability, IBloatedHunger instance, EnumFacing side, NBTBase nbt)
		{
			instance.setBloatedAmount(((NBTTagShort) nbt).getShort());
		}
	}
}
