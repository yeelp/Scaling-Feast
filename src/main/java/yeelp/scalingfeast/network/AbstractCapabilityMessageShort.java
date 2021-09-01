package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.PacketBuffer;
import yeelp.scalingfeast.capability.SFCapabilityBase;

public abstract class AbstractCapabilityMessageShort<Cap extends SFCapabilityBase<NBTTagShort>> extends AbstractCapabilityMessage<NBTTagShort, Cap> {

	public AbstractCapabilityMessageShort() {
		
	}

	public AbstractCapabilityMessageShort(Cap cap) {
		super(cap);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.deserializeNBT(new NBTTagShort(new PacketBuffer(buf).readShort()));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeShort(this.serializeNBT().getShort());
	}
}
