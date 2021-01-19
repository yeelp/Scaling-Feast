package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.IHeartyShankUsageTicker;

public class TickerMessage implements IMessage
{
	private NBTTagInt tag;
	public TickerMessage()
	{
		
	}
	
	public TickerMessage(IHeartyShankUsageTicker ticker)
	{
		this.tag = ticker.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		deserializeNBT(new NBTTagInt(new PacketBuffer(buf).readInt()));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		new PacketBuffer(buf).writeInt(serializeNBT().getInt());
	}

	public NBTTagInt serializeNBT()
	{
		return this.tag;
	}
	
	private void deserializeNBT(NBTTagInt tag)
	{
		this.tag = tag.copy();
	}
	
	public static final class Handler implements IMessageHandler<TickerMessage, IMessage>
	{
		@Override
		public IMessage onMessage(TickerMessage message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		public void handle(TickerMessage message, MessageContext ctx)
		{
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			ScalingFeastAPI.accessor.getShankUsageTicker(player).deserializeNBT(message.serializeNBT());
		}	
	}

}
