package yeelp.scalingfeast.capability.impl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.scalingfeast.capability.IStarvationStats;
import yeelp.scalingfeast.network.StarvationStatsMessage;

import javax.annotation.Nonnull;

public final class StarvationStats implements IStarvationStats {

	@CapabilityInject(IStarvationStats.class)
	public static Capability<IStarvationStats> cap = null;
	
	private static final class Counter implements ICountable {

		private short count;

		public Counter() {
			this.count = 0;
		}

		@Override
		public void inc(short amount) {
			this.count += amount;
		}

		@Override
		public void reset() {
			this.count = 0;
		}

		@Override
		public void set(short val) {
			this.count = val;
		}

		@Override
		public short get() {
			return this.count;
		}
	}
	
	private final ICountable tracker, counter;

	public StarvationStats() {
		this.counter = new Counter();
		this.tracker = new Counter();
	}
	
	@Override
	public IMessage getIMessage() {
		return new StarvationStatsMessage(this);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setShort("tracker", this.getTracker().get());
		tag.setShort("counter", this.getCounter().get());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.getTracker().set(nbt.getShort("tracker"));
		this.getCounter().set(nbt.getShort("counter"));
	}

	@Override
	public ICountable getTracker() {
		return this.tracker;
	}

	@Override
	public ICountable getCounter() {
		return this.counter;
	}

}
