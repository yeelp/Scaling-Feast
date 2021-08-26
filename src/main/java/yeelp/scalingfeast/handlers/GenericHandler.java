package yeelp.scalingfeast.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HungerEvent;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.helpers.AppleSkinHelper;
import yeelp.scalingfeast.network.SatSyncMessage;
import yeelp.scalingfeast.util.SFAttributes;

public class GenericHandler extends Handler {
	private static final Map<UUID, Float> satLevels = new HashMap<UUID, Float>();

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt) {
		evt.maxHunger += evt.player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD).getAttributeValue();
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
		double reduction = 1.0f / foodEfficiency;
		evt.deltaExhaustion *= reduction < 0 ? 0 : reduction;
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent evt) {
		// AppleSkin will do this for us, no point in duplicating behaviour if it's
		// loaded.
		if(AppleSkinHelper.isLoaded() || !(evt.getEntity() instanceof EntityPlayerMP)) {
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) evt.getEntity();
		float currSatLevel = player.getFoodStats().getSaturationLevel();
		// we use the wrapper class to simplify the case where satLevels.get returns
		// null
		// However, we should use Float.floatValue() when comparing to the primitive
		// float currSatLevel.
		Float lastSatLevel = satLevels.get(player.getUniqueID());
		if(lastSatLevel == null || lastSatLevel.floatValue() != currSatLevel) {
			PacketHandler.INSTANCE.sendTo(new SatSyncMessage(currSatLevel), player);
			satLevels.put(player.getUniqueID(), currSatLevel);
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerUpdate(PlayerTickEvent evt) {
		if(evt.player.getFoodStats().getFoodLevel() > AppleCoreAPI.accessor.getMaxHunger(evt.player)) {
			AppleCoreAPI.mutator.setHunger(evt.player, AppleCoreAPI.accessor.getMaxHunger(evt.player));
		}
		ScalingFeastAPI.mutator.capPlayerHunger(evt.player);
		ScalingFeastAPI.mutator.capPlayerSaturation(evt.player);
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
