package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class HeartyShankUsageTicker implements IHeartyShankUsageTicker
{
	private int count = 0;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == HeartyShankUsageTickerProvider.ticker;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == HeartyShankUsageTickerProvider.ticker ? HeartyShankUsageTickerProvider.ticker.<T> cast(this) : null;
	}

	@Override
	public NBTTagInt serializeNBT()
	{
		return new NBTTagInt(count);
	}

	@Override
	public void deserializeNBT(NBTTagInt nbt)
	{
		count = nbt.getInt();
	}

	@Override
	public void inc()
	{
		count++;
	}

	@Override
	public int getCount()
	{
		return count;
	}

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IHeartyShankUsageTicker.class, new TickerStorage(), new TickerFactory());
	}
	
	private static class TickerFactory implements Callable<IHeartyShankUsageTicker>
	{
		@Override
		public IHeartyShankUsageTicker call() throws Exception
		{
			return new HeartyShankUsageTicker();
		}
	}
	
	private static class TickerStorage implements IStorage<IHeartyShankUsageTicker>
	{
		@Override
		public NBTBase writeNBT(Capability<IHeartyShankUsageTicker> capability, IHeartyShankUsageTicker instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IHeartyShankUsageTicker> capability, IHeartyShankUsageTicker instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagInt) nbt);
		}
	}
}
