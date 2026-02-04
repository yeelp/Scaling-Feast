package yeelp.scalingfeast.handlers;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.DisplayStyle;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.TrackerStyle;
import yeelp.scalingfeast.hud.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class HUDOverlayHandler extends Handler {
	private static final IDrawable EXHAUSTION_UNDERLAY = new ExhaustionDrawable();
	private static final IDrawable MAX_DRAWABLE = new ScalingFeastMaxDrawable();
	private static final IDrawable STARVATION_DRAWABLE = new ScalingFeastStarvationTrackerDrawable();
	private static final List<IDrawable> VANILLA_DRAWABLES = Lists.newArrayList(new HungerContainersDrawable(), new VanillaHungerDrawable());
	private static final List<IDrawable> CUSTOM_DRAWABLES = Lists.newArrayList(new CustomContainersDrawable(), new CustomHungerBarBaseDrawable(), new CustomHungerBarFirstBarDrawable());
	private static final List<IDrawable> HUNGER_DRAWABLES = Lists.newArrayList(new ScalingFeastVanillaHungerOverrideDrawable(), new ScalingFeastLowerHungerBarDrawable(), new ScalingFeastUpperHungerBarDrawable());
	private static final List<IDrawable> SATURATION_DRAWABLES = Lists.newArrayList(new ScalingFeastLowerSaturationBarDrawable(), new ScalingFeastUpperSaturationBarDrawable());
	private static final List<IDrawable> BLOATED_DRAWABLES = Lists.newArrayList(new BloatedContainersDrawable(), new ScalingFeastBloatedBarMeatDrawable(), new ScalingFeastLowerBloatedBarDrawable(), new ScalingFeastUpperBloatedBarDrawable());
	
	public HUDOverlayHandler() {
		DrawUtils.updateColours();
		DrawUtils.updateTextColours();
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPreRender(RenderGameOverlayEvent.Pre evt) {
		if(evt.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
			if(ModConfig.hud.style != DisplayStyle.DEFAULT) {
				// In order to make the HUD look nice, we need to completely remove
				// The hunger bar and re draw it so we have control over the 'jittering'.
				evt.setCanceled(true);
				Minecraft mc = Minecraft.getMinecraft();
				EntityPlayer player = mc.player;

				ScaledResolution res = evt.getResolution();

				int offset = GuiIngameForge.right_height;

				final int left = res.getScaledWidth() / 2 + 91;
				final int top = res.getScaledHeight() - offset;
				// Calculate the random jitter amount beforehand and pass it to the draw methods
				DrawUtils.calculateJitterAmount(mc.ingameGUI.getUpdateCounter(), player);
				// If we have AppleSkin/LemonSkin, we need to redraw the whole exhaustion bar.
				EXHAUSTION_UNDERLAY.draw(mc, player, left, top);
				drawDrawables(ModConfig.hud.iconSet.isCustom() ? CUSTOM_DRAWABLES : VANILLA_DRAWABLES, mc, player, left, top);
				drawDrawables(HUNGER_DRAWABLES, mc, player, left, top);
				if(ModConfig.hud.drawSaturation) {
					drawDrawables(SATURATION_DRAWABLES, mc, player, left, top);
				}
				MAX_DRAWABLE.draw(mc, player, left, top);
				if(ModConfig.hud.trackerStyle == TrackerStyle.SATURATION && ModConfig.features.starve.tracker.lossFreq > 1) {
					STARVATION_DRAWABLE.draw(mc, player, left, top);
				}
				InfoStringsDrawables.getInfoDrawable().draw(mc, player, left, top);
				
				int bloatedAmount = ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount();
				if(bloatedAmount > 0) {
					GuiIngameForge.right_height += 10;
					// reset offset value to reflect change to right_height.
					offset = GuiIngameForge.right_height;
					int newTop = res.getScaledHeight() - offset;
					drawDrawables(BLOATED_DRAWABLES, mc, player, left, newTop);
					BloatedInfoStringDrawables.getInfoDrawable().draw(mc, player, left, newTop);
				}
				// air meter expects this to be done before it runs so it doesn't draw on top of
				// hunger.
				GuiIngameForge.right_height += 10;
				if(ModConfig.compat.shouldFirePost) {
					MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(evt, RenderGameOverlayEvent.ElementType.FOOD));
				}
			}
		}
	}
	
	private static void drawDrawables(Iterable<IDrawable> drawables, Minecraft mc, EntityPlayer player, int left, int top) {
		drawables.forEach((d) -> {
			if(d.shouldDraw(player)) {
				d.draw(mc, player, left, top);				
			}
		});
	}
}