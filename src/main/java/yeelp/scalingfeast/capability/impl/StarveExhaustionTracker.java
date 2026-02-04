package yeelp.scalingfeast.capability.impl;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.scalingfeast.capability.IStarveExhaustionTracker;
import yeelp.scalingfeast.network.StarveExhaustMessage;

import javax.annotation.Nonnull;

public class StarveExhaustionTracker extends SFSingleValueCapabilityFloat implements IStarveExhaustionTracker {
	
	@CapabilityInject(IStarveExhaustionTracker.class)
	public static Capability<IStarveExhaustionTracker> cap = null;

	public StarveExhaustionTracker() {
		super();
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
	public void inc(Float amount) {
		super.inc(amount);
		if(this.getVal() < 0) {
			this.setVal(0.0f);
		}
	}

	@Override
	public IMessage getIMessage() {
		return new StarveExhaustMessage(this);
	}

}
