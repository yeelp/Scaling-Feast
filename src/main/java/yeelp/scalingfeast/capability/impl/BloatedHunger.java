package yeelp.scalingfeast.capability.impl;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.scalingfeast.capability.IBloatedHunger;
import yeelp.scalingfeast.network.BloatedHungerMessage;

public class BloatedHunger extends SFSingleValueCapabilityShort implements IBloatedHunger {

	@CapabilityInject(IBloatedHunger.class)
	public static Capability<IBloatedHunger> cap = null;

	/**
	 * Create a new BloatedHunger set at 0;
	 */
	public BloatedHunger() {
		this((short) 0);
	}

	/**
	 * Create a new BloatedHunger.
	 * 
	 * @param startingAmount the amount this BloatedHunger should have.
	 */
	public BloatedHunger(short startingAmount) {
		super(startingAmount);
	}

	@Override
	public void setVal(Short val) {
		if(this.getVal() < 0) {
			super.setVal((short) 0);
		}
		else {
			super.setVal(val);
		}
	}

	@Override
	public void dec(Short amount) {
		this.deductBloatedAmount(amount);
	}

	@Override
	public short deductBloatedAmount(short amount) {
		if(amount < 0) {
			return 0;
		}
		short deduction = amount >= this.getVal() ? this.getVal() : amount;
		this.setVal((short) (this.getVal() - deduction));
		return (short) (amount - deduction);
	}

	@Override
	public IMessage getIMessage() {
		return new BloatedHungerMessage(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}
}
