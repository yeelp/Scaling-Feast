package yeelp.scalingfeast.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.PacketHandler;
import yeelp.scalingfeast.network.SoundMessage;

import java.util.List;
import java.util.Map;

public final class SFSounds {
	private static final Map<String, SoundEvent> SOUND_MAP = Maps.newHashMap();
	private static final List<String> ID_LIST = Lists.newArrayList();
	
	public static final SoundEvent HUNGER_INCREASE = createSound("hunger_increase");
	public static final SoundEvent HUNGER_DECREASE = createSound("hunger_decrease");
	
	public static void init() {
		SOUND_MAP.values().forEach(ForgeRegistries.SOUND_EVENTS::register);
	}
	
	public static void playSound(EntityPlayer player, SoundEvent name, float vol, float pitch) {
		if(player instanceof EntityPlayerMP) {
            //noinspection DataFlowIssue
            PacketHandler.INSTANCE.sendTo(new SoundMessage(encodeSoundID(name.getRegistryName().toString()), vol, pitch), (EntityPlayerMP) player);
		}
		else {
			player.playSound(name, vol, pitch);
		}
	}
	
	public static String decodeSoundID(byte id) {
		if(id < ID_LIST.size()) {
			return ID_LIST.get(id);
		}
		throw new RuntimeException("Can't decode sound ID "+id);
	}
	
	private static byte encodeSoundID(String id) {
		if(ID_LIST.contains(id)) {
			return (byte) ID_LIST.indexOf(id);
		}
		throw new RuntimeException("Can't encode Scaling Feast sound ID "+id);
	}
			
	private static SoundEvent createSound(String id) {
		ResourceLocation loc = new ResourceLocation(ModConsts.MOD_ID, id);
		SoundEvent sound = new SoundEvent(loc).setRegistryName(loc);
		SOUND_MAP.put(id, sound);
		ID_LIST.add(loc.toString());
		return sound;
	}
}
