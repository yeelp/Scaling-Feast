package yeelp.scalingfeast.handlers;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;

import java.util.Map;
import java.util.UUID;

public class BloatedHandler extends Handler {

	private static final Map<UUID, Integer> DELAY = Maps.newHashMap();
	private static final int TIME_THRESHOLD = 30;

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onStarve(StarvationEvent.Starve evt) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.player);
		if(sfstats.getBloatedHungerAmount() > 0) {
			evt.setCanceled(true);
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onExhausted(ExhaustionEvent.Exhausted evt) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.player);
		if(sfstats.getBloatedHungerAmount() > 0) {
			EntityPlayer player = evt.player;
			short remainder = sfstats.deductBloatedAmount((short) -(evt.deltaHunger + evt.deltaSaturation));
			FoodStats fs = player.getFoodStats();
			float currSatLevel = fs.getSaturationLevel();
			if(currSatLevel == 0) {
				evt.deltaHunger = remainder;
			}
			else {
				if(currSatLevel >= remainder) {
					evt.deltaSaturation = remainder;
					evt.deltaHunger = 0;
				}
				else {
					evt.deltaSaturation = currSatLevel;
					evt.deltaHunger = (int) (remainder - currSatLevel);
				}
			}
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent evt) {
		// If server side player has bloated amount, sync to client right away so it
		// shows in HUD.
		if(evt.player instanceof EntityPlayerMP && ScalingFeastAPI.accessor.getSFFoodStats(evt.player).getBloatedHungerAmount() > 0) {
			ScalingFeastAPI.accessor.getSFFoodStats(evt.player).syncCapabilities(SFFoodStats.BLOATED_SYNC_FLAG);
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt) {
		// If player has bloated hunger, sync it with the server every 30 ticks to
		// prevent desync.
		if(!evt.player.world.isRemote && DELAY.compute(evt.player.getPersistentID(), (uuid, i) -> i == null ? 1 : (i + 1) % TIME_THRESHOLD) == 0 && ScalingFeastAPI.accessor.getSFFoodStats(evt.player).getBloatedHungerAmount() > 0) {
			ScalingFeastAPI.accessor.getSFFoodStats(evt.player).syncCapabilities(SFFoodStats.BLOATED_SYNC_FLAG);
		}
	}
}
