package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.network.PacketBuffer;
import yeelp.scalingfeast.capability.SFCapabilityBase;

public abstract class AbstractCapabilityMessageFloat<Cap extends SFCapabilityBase<NBTTagFloat>> extends AbstractCapabilityMessage<NBTTagFloat, Cap> {

	public AbstractCapabilityMessageFloat() {

	}

	public AbstractCapabilityMessageFloat(Cap cap) {
		super(cap);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.deserializeNBT(new NBTTagFloat(new PacketBuffer(buf).readFloat()));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeFloat(this.serializeNBT().getFloat());
	}
}
