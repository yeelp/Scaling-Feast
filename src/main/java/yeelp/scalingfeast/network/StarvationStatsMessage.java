package yeelp.scalingfeast.network;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.base.Functions;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.scalingfeast.ScalingFeast;
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
		try {
            //noinspection DataFlowIssue
            this.deserializeNBT(new PacketBuffer(buf).readCompoundTag());
		}
		catch(IOException e) {
			Arrays.stream(e.getStackTrace()).map(Functions.toStringFunction()).forEach(ScalingFeast::err);
			throw new RuntimeException(e);
		}
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
