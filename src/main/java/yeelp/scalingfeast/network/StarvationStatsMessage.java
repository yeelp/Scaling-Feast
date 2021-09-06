package yeelp.scalingfeast.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.scalingfeast.capability.IStarvationStats;
import yeelp.scalingfeast.capability.impl.StarvationStats;

public class StarvationStatsMessage extends AbstractCapabilityMessage<NBTTagCompound, IStarvationStats> {
	
	public StarvationStatsMessage() {
		
	}

	public StarvationStatsMessage(IStarvationStats cap) {
		super(cap);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound readTag = new NBTTagCompound();
		try {
			readTag = new PacketBuffer(buf).readCompoundTag();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		this.deserializeNBT(readTag);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.serializeNBT());
	}
	
	public static MessageHandler<NBTTagCompound, IStarvationStats, StarvationStatsMessage> getMessageHandler() {
		return new MessageHandler<NBTTagCompound, IStarvationStats, StarvationStatsMessage>() {
			
			@Override
			protected Capability<IStarvationStats> getCapabilityInstance() {
				return StarvationStats.cap;
			}
		};
	}

}
