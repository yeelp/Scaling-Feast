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
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.IFoodCapModifier;

/**
 * Scaling Feast's message for FoodCap modifiers
 * @author Yeelp
 *
 */
public class FoodCapModifierMessage implements IMessage
{
	private NBTTagShort tag;
	public FoodCapModifierMessage(IFoodCapModifier mod)
	{
		this.tag = new NBTTagShort(mod.getModifier());
	}
	
	public FoodCapModifierMessage()
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
	 * The FoodCapModifierMessage handler
	 * @author Yeelp
	 *
	 */
	public static final class Handler implements IMessageHandler<FoodCapModifierMessage, IMessage>
	{

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(FoodCapModifierMessage message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(FoodCapModifierMessage msg, MessageContext ctx)
		{
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			player.getCapability(FoodCapModifierProvider.foodCapMod, null).deserializeNBT((NBTTagShort) msg.serializeNBT());
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
