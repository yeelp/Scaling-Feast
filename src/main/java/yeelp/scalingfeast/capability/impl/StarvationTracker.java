package yeelp.scalingfeast.capability.impl;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.scalingfeast.capability.IStarvationTracker;
import yeelp.scalingfeast.network.StarvationTrackerMessage;

/**
 * A class that implements the IStarvationTracker
 * 
 * @author Yeelp
 *
 */
public final class StarvationTracker extends SFSingleValueCapabilityShort implements IStarvationTracker {

	@CapabilityInject(IStarvationTracker.class)
	public static Capability<IStarvationTracker> cap = null;
	
	/**
	 * Create a new Starvation Tracker set at 0
	 */
	public StarvationTracker() {
		this((short) 0);
	}

	/**
	 * Create a new Starvation Tracker starting at the current value
	 * 
	 * @param start the starting value
	 */
	public StarvationTracker(short start) {
		super(start);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	public IMessage getIMessage() {
		return new StarvationTrackerMessage(this);
	}
}
