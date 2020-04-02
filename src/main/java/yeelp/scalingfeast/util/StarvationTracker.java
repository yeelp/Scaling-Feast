package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/**
 * A class that implements the IStarvationTracker
 * @author Yeelp
 *
 */
public final class StarvationTracker implements IStarvationTracker 
{
	private short counter;
	
	/**
	 * Create a new Starvation Tracker set at 0
	 */
	public StarvationTracker()
	{
		this.counter = 0;
	}
	
	/**
	 * Create a new Starvation Tracker starting at the current value
	 * @param start the starting value
	 */
	public StarvationTracker(short start)
	{
		this.counter = start;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		return capability == StarvationTrackerProvider.starvationTracker;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		return capability == StarvationTrackerProvider.starvationTracker ? StarvationTrackerProvider.starvationTracker.<T> cast(this) : null;
	}

	@Override
	public NBTTagShort serializeNBT() 
	{
		return new NBTTagShort(this.counter);
	}

	@Override
	public void deserializeNBT(NBTTagShort nbt) 
	{
		this.counter = nbt.getShort();
	}

	@Override
	public void tickStarvation(int hunger) 
	{
		if(hunger == 0)
		{
			this.counter++;
		}
	}

	@Override
	public void reset() 
	{
		this.counter = 0;
	}

	@Override
	public short getCount()
	{
		return this.counter;
	}
	
	@Override
	public void setCount(short amount)
	{
		this.counter = amount;
	}
	
	public static void register() 
	{
		CapabilityManager.INSTANCE.register(IStarvationTracker.class, new StarvationStorage(), new StarvationFactory());
	}
	
	private static class StarvationFactory implements Callable<IStarvationTracker>
	{

		@Override
		public IStarvationTracker call() throws Exception 
		{
			return new StarvationTracker();
		}
		
	}
	
	private static class StarvationStorage implements IStorage<IStarvationTracker>
	{

		@Override
		public NBTBase writeNBT(Capability<IStarvationTracker> capability, IStarvationTracker instance, EnumFacing side) 
		{
			return new NBTTagShort(instance.getCount());
		}

		@Override
		public void readNBT(Capability<IStarvationTracker> capability, IStarvationTracker instance, EnumFacing side, NBTBase nbt) 
		{
			instance.setCount(((NBTTagShort) nbt).getShort());
		}
		
	}
}
