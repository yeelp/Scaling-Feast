package yeelp.scalingfeast.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.IFoodCapModifier;

/**
 * Scaling Feast's message for FoodCap modifiers
 * @author Yeelp
 *
 */
public class FoodCapModifierMessage implements IMessage
{
	private NBTTagList tag;
	public FoodCapModifierMessage(IFoodCapModifier mod)
	{
		this.tag = new NBTTagList();
		for(Entry<String, Short> entry : mod.getAllModifiers().entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("id", entry.getKey());
			tag.setShort("modifier", entry.getValue());
			this.tag.appendTag(tag);
		}
	}
	
	public FoodCapModifierMessage()
	{
		this.tag = new NBTTagList();
	}
	
	public NBTTagList serializeNBT()
	{
		return this.tag;
	}
	
	public void deserializeNBT(List<NBTTagCompound> lst)
	{
		this.tag = new NBTTagList();
		for(NBTTagCompound tag : lst)
		{
			this.tag.appendTag(tag);
		}
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
			ScalingFeastAPI.accessor.getFoodCapModifier(player).deserializeNBT((NBTTagList) msg.serializeNBT());
		}
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		List<NBTTagCompound> lst = new LinkedList<NBTTagCompound>();
		PacketBuffer pb = new PacketBuffer(buf.copy());
		while(pb.isReadable())
		{
			try
			{
				lst.add(pb.readCompoundTag());
			}
			catch (IOException e)
			{
				//If we're here, we couldn't read the PacketBuffer as a list of Compound Tags. Must be pre 1.5.0 - read it as a short!
				pb = new PacketBuffer(buf);
				lst.clear();
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("id", "modules");
				tag.setShort("modifier", pb.readShort());
				lst.add(tag);
				break;
			}
		}
		deserializeNBT(lst);
	}
	@Override
	public void toBytes(ByteBuf buf) 
	{
		PacketBuffer pb = new PacketBuffer(buf);
		for(NBTBase tag : this.tag)
		{
			pb.writeCompoundTag((NBTTagCompound) tag);
		}
	}

}
