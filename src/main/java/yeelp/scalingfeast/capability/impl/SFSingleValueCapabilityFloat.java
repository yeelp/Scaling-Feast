package yeelp.scalingfeast.capability.impl;

import net.minecraft.nbt.NBTTagFloat;
import yeelp.scalingfeast.capability.SFSingleValueCapability;

public abstract class SFSingleValueCapabilityFloat implements SFSingleValueCapability<Float, NBTTagFloat> {
	private float val;

	public SFSingleValueCapabilityFloat() {
		this.val = 0.0f;
	}

	public SFSingleValueCapabilityFloat(float val) {
		this.val = val;
	}

	@Override
	public NBTTagFloat serializeNBT() {
		return new NBTTagFloat(this.val);
	}

	@Override
	public void deserializeNBT(NBTTagFloat nbt) {
		this.val = nbt.getFloat();
	}

	@Override
	public Float getVal() {
		return this.val;
	}

	@Override
	public void setVal(Float val) {
		this.val = val;
	}

	@Override
	public void inc(Float amount) {
		this.val += amount;
	}

	@Override
	public void dec(Float amount) {
		this.val -= amount;
	}

	@Override
	public void reset() {
		this.val = 0.0f;
	}
}
