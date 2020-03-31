package yeelp.scalingfeast.network;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A class for sending NBT messages. Extend this class for messages which send NBT data
 * @author Yeelp
 *
 * @param <T> A class that extends {@link net.minecraft.nbt.NBTBase}
 */
public abstract class NBTMessage<T extends NBTBase> implements IMessage 
{
	private T contents;
	private static final String KEY = "contents";
	/**
	 * Serialize this message's NBT data
	 * @return the message's NBT data.
	 */
	public T serializeNBT()
	{
		return contents;
	}
	
	/**
	 * Store NBT data in this message
	 * @param contents The NBT data to store
	 */
	public void deserializeNBT(T contents)
	{
		this.contents = contents;
	}
	
	/**
	 * The message Handler for the NBTMessage. Any class extending NBTMessage should also have an inner class extending this one
	 * @author Yeelp
	 *
	 * @param <T> A class that extends NBTBase. Should ideally be the same as the type used in {@link NBTMessage}
	 */
	public static abstract class NBTMessageHandler implements IMessageHandler<NBTMessage, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(NBTMessage message, MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		/**
		 * Handle the message.
		 * @param msg the message received
		 * @param ctx the message context
		 */
		@SideOnly(Side.CLIENT)
		public abstract void handle(NBTMessage msg, MessageContext ctx);
	}
}
