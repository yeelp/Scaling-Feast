package yeelp.scalingfeast.features;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent.ExhaustingAction;
import squeek.applecore.api.hunger.ExhaustionEvent.ExhaustingActions;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigExhaustionScaling;
import yeelp.scalingfeast.handlers.Handler;

public final class SFExhaustionScaling extends FeatureBase<SFConfigExhaustionScaling> {

	private static final int TICKS_PER_SECOND = 20;
	
	@Override
	public Handler getFeatureHandler() {
		return new Handler() {
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public final void onExhaustingAction(ExhaustingAction evt) {
				if(SFExhaustionScaling.this.getConfig().doExhaustionScaling && evt.source == ExhaustingActions.HARVEST_BLOCK) {
					evt.deltaExhaustion = 0.0f;
				}
			}
			
			@SubscribeEvent
			public final void onBlockBreak(BreakEvent evt) {
				EntityPlayer player;
				World world;
				SFConfigExhaustionScaling config = SFExhaustionScaling.this.getConfig();
				if(config.doExhaustionScaling && !((player = evt.getPlayer()) instanceof FakePlayer) && !(world = player.world).isRemote) {
					float seconds = 1/(Math.min(ForgeHooks.blockStrength(evt.getState(), player, world, evt.getPos()), 1.0f) * TICKS_PER_SECOND);
					player.addExhaustion(config.baseExhaustionRate * seconds);
				}
			}
		};
	}
	
	@Override
	protected SFConfigExhaustionScaling getConfig() {
		return ModConfig.features.exhaustionScaling;
	}

}
