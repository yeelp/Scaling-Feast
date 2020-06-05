package yeelp.scalingfeast.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class StarveExhaustionTracker implements IStarveExhaustionTracker
{
	private float exhaust;
	public StarveExhaustionTracker()
	{
		exhaust = 0.0f;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == StarveExhaustionTrackerProvider.starveExhaust;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == StarveExhaustionTrackerProvider.starveExhaust ? StarveExhaustionTrackerProvider.starveExhaust.<T> cast(this) : null;
	}

	@Override
	public NBTTagFloat serializeNBT()
	{
		return new NBTTagFloat(exhaust);
	}

	@Override
	public void deserializeNBT(NBTTagFloat nbt)
	{
		exhaust = nbt.getFloat();
	}

	@Override
	public void addExhaustion(int hunger, float amount)
	{
		if(hunger == 0)
		{
			exhaust += amount;
			if(exhaust < 0)
			{
				exhaust = 0;
			}
		}
	}

	@Override
	public float getTotalExhaustion()
	{
		return exhaust;
	}

	@Override
	public void reset()
	{
		exhaust = 0;
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IStarveExhaustionTracker.class, new StarveExhaustStorage(), new StarveExhaustFactory());
	}
	
	private static class StarveExhaustFactory implements Callable<IStarveExhaustionTracker>
	{

		@Override
		public IStarveExhaustionTracker call() throws Exception
		{
			return new StarveExhaustionTracker();
		}
		
	}
	
	private static class StarveExhaustStorage implements IStorage<IStarveExhaustionTracker>
	{

		@Override
		public NBTBase writeNBT(Capability<IStarveExhaustionTracker> capability, IStarveExhaustionTracker instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IStarveExhaustionTracker> capability, IStarveExhaustionTracker instance, EnumFacing side, NBTBase nbt)
		{
			if(nbt instanceof NBTTagFloat)
			{
				instance.deserializeNBT((NBTTagFloat) nbt);
			}
		}
		
	}

}
