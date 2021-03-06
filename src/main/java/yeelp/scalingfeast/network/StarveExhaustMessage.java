package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.IStarveExhaustionTracker;

public class StarveExhaustMessage implements IMessage
{
	private NBTTagFloat tag;
	
	public StarveExhaustMessage()
	{
		
	}
	
	public StarveExhaustMessage(IStarveExhaustionTracker tracker)
	{
		this.tag = new NBTTagFloat(tracker.getTotalExhaustion());
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		deserializeNBT(new NBTTagFloat(new PacketBuffer(buf).readFloat()));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		new PacketBuffer(buf).writeFloat(serializeNBT().getFloat());
	}
	
	public NBTTagFloat serializeNBT()
	{
		return this.tag;
	}
	
	public void deserializeNBT(NBTTagFloat tag)
	{
		this.tag = tag.copy();
	}

	public static final class Handler implements IMessageHandler<StarveExhaustMessage, IMessage>
	{

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(StarveExhaustMessage message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(StarveExhaustMessage msg, MessageContext ctx) 
		{
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			ScalingFeastAPI.accessor.getStarveExhaustionTracker(player).deserializeNBT((NBTTagFloat) msg.serializeNBT());
		}
	}
}
