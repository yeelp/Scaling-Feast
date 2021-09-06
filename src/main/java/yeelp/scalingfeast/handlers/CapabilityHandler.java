
package yeelp.scalingfeast.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.capability.IStarvationStats;
import yeelp.scalingfeast.capability.impl.BloatedHunger;
import yeelp.scalingfeast.capability.impl.StarvationStats;
import yeelp.scalingfeast.capability.impl.StarveExhaustionTracker;
import yeelp.scalingfeast.config.ModConfig;

public class CapabilityHandler extends Handler {

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt) {
		if(evt.getObject() instanceof EntityPlayer) {
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "StarvationStats"), new StarvationStats());
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "BloatedAmount"), new BloatedHunger());
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "ExhaustionSinceStarve"), new StarveExhaustionTracker());
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onClone(PlayerEvent.Clone evt) {
		EntityPlayer oldPlayer = evt.getOriginal();
		EntityPlayer newPlayer = evt.getEntityPlayer();
		oldPlayer.getCapability(StarveExhaustionTracker.cap, null).sync(newPlayer);
		IStarvationStats starveStats = oldPlayer.getCapability(StarvationStats.cap, null);
		if(evt.isWasDeath()) {
			starveStats.getCounter().reset();
			if(ModConfig.features.starve.tracker.doesFreqReset) {
				starveStats.getTracker().reset();
			}
		}
		if(starveStats.getCounter().get() != 0 || starveStats.getTracker().get() != 0) {
			starveStats.sync(newPlayer);			
		}
	}
}
