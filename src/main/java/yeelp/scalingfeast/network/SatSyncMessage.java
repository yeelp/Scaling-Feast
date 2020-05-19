package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Message to sync saturation to the client for the HUD
 * @author Yeelp
 *
 */
public class SatSyncMessage implements IMessage
{
	private float saturationLevel;
	
	/**
	 * Construct a new SatSyncMessage
	 * @param saturationLevel the saturation level for this message
	 */
	public SatSyncMessage(float saturationLevel)
	{
		this.saturationLevel = saturationLevel;
	}
	
	public SatSyncMessage()
	{
		
	}
	
	public float getSaturationLevel()
	{
		return this.saturationLevel;
	}
	
	public void setSaturationLevel(float sat)
	{
		this.saturationLevel = sat;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.saturationLevel = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(this.saturationLevel);
	}

	public static class Handler implements IMessageHandler<SatSyncMessage, IMessage>
	{

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(SatSyncMessage message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		private void handle(SatSyncMessage msg, MessageContext ctx)
		{
			NetworkHelper.getSidedPlayer(ctx).getFoodStats().setFoodSaturationLevel(msg.saturationLevel);;	
		}
	}
}
