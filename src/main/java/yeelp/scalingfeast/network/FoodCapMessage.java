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
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;

/**
 * Scaling Feast's message for a player's food cap
 * @author Yeelp
 *
 */
public class FoodCapMessage implements IMessage
{
	private NBTTagShort tag;
	/**
	 * Construct a new message.
	 * @param cap The food cap for this message.
	 */
	public FoodCapMessage(IFoodCap cap)
	{
		this.tag = new NBTTagShort(cap.getMaxFoodLevel());
	}
	
	public FoodCapMessage()
	{
		
	}
	
	public NBTTagShort serializeNBT()
	{
		return this.tag;
	}
	
	public void deserializeNBT(NBTTagShort tag)
	{
		this.tag = tag.copy();
	}
	
	/**
	 * The Handler for the FoodCapMessage
	 * @author Yeelp
	 *
	 */
	public static final class Handler implements IMessageHandler<FoodCapMessage, IMessage>
	{	
		
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(FoodCapMessage message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(FoodCapMessage msg, MessageContext ctx)
		{
			EntityPlayer player = Minecraft.getMinecraft().player;
			player.getCapability(FoodCapProvider.capFoodStat, null).deserializeNBT((NBTTagShort) msg.serializeNBT());
		}
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
}
