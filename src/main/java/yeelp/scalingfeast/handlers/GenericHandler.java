package yeelp.scalingfeast.handlers;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HungerEvent;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.init.SFAttributes;
import yeelp.scalingfeast.integration.ModIntegrationKernel;
import yeelp.scalingfeast.network.SatSyncMessage;

import java.util.Map;
import java.util.UUID;

public class GenericHandler extends Handler {
	private static final Map<UUID, Float> satLevels = Maps.newHashMap();
	private static Boolean hasSkinIntegration;

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt) {
		EntityPlayer player = evt.player;
		if(player == null) {
			return;
		}
		evt.maxHunger += (int) player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD).getAttributeValue();
		evt.maxHunger = Math.max(1, evt.maxHunger);
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGetExhaustionCap(ExhaustionEvent.GetExhaustionCap evt) {
		evt.exhaustionLevelCap = Float.MAX_VALUE;
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onExhaustion(ExhaustionEvent.ExhaustionAddition evt) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.player);
		double foodEfficiency = sfstats.getFoodEfficiencyModifierAttribute().getAttributeValue();
		if(foodEfficiency < 1) {
			foodEfficiency = -1.0 / (foodEfficiency - 2);
		}
		double reduction = 1.0 / foodEfficiency;
		evt.deltaExhaustion *= (float) (reduction < 0 ? 0.0f : reduction);
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent evt) {
		// AppleSkin will do this for us, no point in duplicating behaviour if it's
		// loaded.
		if(hasSkinIntegration == null) {
			hasSkinIntegration = ModIntegrationKernel.getCurrentSkinIntegration().isPresent();
		}
		if(hasSkinIntegration || !(evt.getEntity() instanceof EntityPlayerMP)) {
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) evt.getEntity();
		float currSatLevel = player.getFoodStats().getSaturationLevel();
		// we use the wrapper class to simplify the case where satLevels.get returns
		// null
		Float lastSatLevel = satLevels.get(player.getUniqueID());
		if(lastSatLevel == null || lastSatLevel != currSatLevel) {
			PacketHandler.INSTANCE.sendTo(new SatSyncMessage(currSatLevel), player);
			satLevels.put(player.getUniqueID(), currSatLevel);
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerUpdate(PlayerTickEvent evt) {
		if(evt.phase == Phase.END) {
			if(evt.player.getFoodStats().getFoodLevel() > AppleCoreAPI.accessor.getMaxHunger(evt.player)) {
				AppleCoreAPI.mutator.setHunger(evt.player, AppleCoreAPI.accessor.getMaxHunger(evt.player));
			}
			ScalingFeastAPI.mutator.capPlayerHunger(evt.player);
			ScalingFeastAPI.mutator.capPlayerSaturation(evt.player);
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
		if(!(evt.player instanceof EntityPlayerMP)) {
			return;
		}
		satLevels.remove(evt.player.getUniqueID());
	}
}
