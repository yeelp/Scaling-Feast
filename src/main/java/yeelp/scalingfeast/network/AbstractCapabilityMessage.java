package yeelp.scalingfeast.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import yeelp.scalingfeast.capability.SFCapabilityBase;

public abstract class AbstractCapabilityMessage<NBT extends NBTBase, Cap extends SFCapabilityBase<NBT>> implements IMessage {
	private NBT nbt;
	
	public AbstractCapabilityMessage() {
		
	}
	
	public AbstractCapabilityMessage(Cap cap) {
		this.nbt = cap.serializeNBT();
	}
	
	public NBT serializeNBT() {
		return this.nbt;
	}
	
	@SuppressWarnings("unchecked")
	public void deserializeNBT(NBT nbt) {
		this.nbt = (NBT) nbt.copy();
	}
	
	public static abstract class MessageHandler<NBT extends NBTBase, Cap extends SFCapabilityBase<NBT>, Msg extends AbstractCapabilityMessage<NBT, Cap>> implements IMessageHandler<Msg, IMessage> {

		@Override
		public IMessage onMessage(Msg message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		public void handle(Msg msg, MessageContext ctx) {
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			player.getCapability(this.getCapabilityInstance(), null).deserializeNBT(msg.serializeNBT());
		}
		
		protected abstract Capability<Cap> getCapabilityInstance();
	}
}
