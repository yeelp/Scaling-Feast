package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.IBloatedHunger;

public class BloatedHungerMessage implements IMessage
{
	private NBTTagShort tag;
	
	public BloatedHungerMessage()
	{
		
	}
	
	public BloatedHungerMessage(IBloatedHunger bloatedHunger)
	{
		this.tag = new NBTTagShort(bloatedHunger.getBloatedAmount());
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
	 * Handler for this message
	 * @author Yeelp
	 *
	 */
	public static final class Handler implements IMessageHandler<BloatedHungerMessage, IMessage>
	{

		@Override
		public IMessage onMessage(BloatedHungerMessage message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		public void handle(BloatedHungerMessage msg, MessageContext ctx)
		{
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			ScalingFeastAPI.accessor.getBloatedHunger(player).deserializeNBT(msg.serializeNBT());
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
		new PacketBuffer(buf).writeShort(serializeNBT().getShort());
	}

}
