package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

/**
 * Scaling Feast's message for tracking starvation
 * @author Yeelp
 *
 */
public class StarvationTrackerMessage implements IMessage 
{
	private NBTTagShort tag;
	
	public StarvationTrackerMessage(IStarvationTracker tracker)
	{
		this.tag = new NBTTagShort(tracker.getCount());
	}
	
	public StarvationTrackerMessage()
	{
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		deserializeNBT(new NBTTagShort(new PacketBuffer(buf).readShort()));
	}
	@Override
	public void toBytes(ByteBuf buf) 
	{
		new PacketBuffer(buf).writeShort(((NBTTagShort) serializeNBT()).getShort());
	}
	
	public NBTTagShort serializeNBT()
	{
		return this.tag;
	}
	
	public void deserializeNBT(NBTTagShort tag)
	{
		this.tag = tag.copy();
	}
	
	public static final class Handler implements IMessageHandler<StarvationTrackerMessage, IMessage>
	{

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(StarvationTrackerMessage message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(StarvationTrackerMessage msg, MessageContext ctx) 
		{
			EntityPlayer player = Minecraft.getMinecraft().player;
			player.getCapability(StarvationTrackerProvider.starvationTracker, null).deserializeNBT((NBTTagShort) msg.serializeNBT());
		}
		
	}
}
