
package yeelp.scalingfeast.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.capability.impl.BloatedHunger;
import yeelp.scalingfeast.capability.impl.StarvationTracker;
import yeelp.scalingfeast.capability.impl.StarveExhaustionTracker;

public class CapabilityHandler extends Handler {

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt) {
		if(evt.getObject() instanceof EntityPlayer) {
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "StarvationTracker"), new StarvationTracker());
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
		if(!evt.isWasDeath() || !ModConfig.features.starve.doesFreqReset) {
			oldPlayer.getCapability(StarvationTracker.cap, null).sync(newPlayer);
		}
	}
}
