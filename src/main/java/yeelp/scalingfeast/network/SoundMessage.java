package yeelp.scalingfeast.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.init.SFSounds;

public final class SoundMessage implements IMessage, IMessageHandler<SoundMessage, IMessage> {
	private byte id;
	private float volume, pitch;

	public SoundMessage() {

	}

	public SoundMessage(byte id, float volume, float pitch) {
		this.id = id;
		this.volume = volume;
		this.pitch = pitch;
	}
	

	/**
	 * @return the id
	 */
	private byte getID() {
		return this.id;
	}

	/**
	 * @return the volume
	 */
	private float getVolume() {
		return this.volume;
	}

	/**
	 * @return the pitch
	 */
	private float getPitch() {
		return this.pitch;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		this.id = pbuf.readByte();
		this.pitch = pbuf.readFloat();
		this.volume = pbuf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		pbuf.writeByte(this.id);
		pbuf.writeFloat(this.pitch);
		pbuf.writeFloat(this.volume);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(SoundMessage message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		return null;
	}

	public static void handle(SoundMessage msg, MessageContext ctx) {
		EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
		if(player != null) {
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(SFSounds.decodeSoundID(msg.getID())));
			if(sound != null) {
				player.playSound(sound, msg.getVolume(), msg.getPitch());
			}
		}
	}

}
