package yeelp.scalingfeast.capability.impl;

import net.minecraft.nbt.NBTTagShort;
import yeelp.scalingfeast.capability.SFSingleValueCapability;

public abstract class SFSingleValueCapabilityShort implements SFSingleValueCapability<Short, NBTTagShort> {

	private short val;
	
	public SFSingleValueCapabilityShort(short start) {
		this.val = start;
	}

	@Override
	public NBTTagShort serializeNBT() {
		return new NBTTagShort(this.val);
	}

	@Override
	public void deserializeNBT(NBTTagShort nbt) {
		this.val = nbt.getShort();
	}

	@Override
	public Short getVal() {
		return this.val;
	}

	@Override
	public void setVal(Short val) {
		this.val = val;
	}

	@Override
	public void inc(Short amount) {
		this.val += amount;
	}

	@Override
	public void dec(Short amount) {
		this.val -= amount;
	}

	@Override
	public void reset() {
		this.val = 0;
	}
}
