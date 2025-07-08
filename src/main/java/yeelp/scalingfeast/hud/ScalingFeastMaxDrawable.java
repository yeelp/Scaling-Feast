package yeelp.scalingfeast.hud;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.MaxColourStyle;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.TrackerStyle;
import yeelp.scalingfeast.handlers.GUIIcons;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastMaxDrawable extends AbstractDrawable {

	private static final float[] TICK_COLOUR_THRESHOLDS = {0.9f, 0.75f, 0.5f, 0};
	private static final String[] COLOUR_THRESHOLDS = {"FF5555", "FFAA00", "FFFF55", "FFFFFF"};
	private static final int MAX_U_COORD = 36, MAX_V_COORD = 9;
	
	public ScalingFeastMaxDrawable() {
		super("sf_extended_max", true, true);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return true;
	}

	@Override
	protected void draw(Minecraft mc, EntityPlayer player, FoodStats stats, int left, int top) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GUIIcons.bindSFGUIIcons(mc);
		int max = AppleCoreAPI.accessor.getMaxHunger(player);
		int maxIndex = (int)(((max - 0.1) % ModConsts.VANILLA_MAX_HUNGER)/2.0f);
		int hunger = stats.getFoodLevel();
		int x = left - maxIndex * 8 - 9;
		int y = top + DrawUtils.getJitterAmount()[maxIndex];
		short alpha = (short) (255 * (hunger > ModConsts.VANILLA_MAX_HUNGER * (Math.ceil(max / 20.0f) - 1) || hunger == 0 ? 1.0f : ModConfig.hud.maxOutlineTransparency)); 
		short starveTicks = ScalingFeastAPI.accessor.getSFFoodStats(player).getStarvationTrackerCount();
		Colour maxColour = getMaxColour(starveTicks);
		if(ModConfig.hud.maxColourStyle == MaxColourStyle.CUSTOM) {
			//blend start and end colours if tick > 0, otherwise only use start colour
			if(starveTicks > 0) {
				AbstractDrawable.drawIcon(mc, x, y, MAX_U_COORD, MAX_V_COORD, maxColour, alpha);
			}
			maxColour = new Colour(ModConfig.hud.maxColourStart);
			int lossFreq = ModConfig.features.starve.tracker.lossFreq;
			float blendRatio = starveTicks + 1 < lossFreq ? ((float) lossFreq - starveTicks) / lossFreq : 0;
			alpha *= blendRatio;
		}
		AbstractDrawable.drawIcon(mc, x, y, MAX_U_COORD, MAX_V_COORD, maxColour, alpha);
		GUIIcons.unbindSFGUIIcons(mc);
	}
	
	private static Colour getMaxColour(int starveTicks) {
		String s;
		if(ModConfig.hud.trackerStyle == TrackerStyle.SATURATION) {
			s = "FFFFFF";
		}
		else if(ModConfig.hud.maxColourStyle == MaxColourStyle.CUSTOM) {
			s = ModConfig.hud.maxColourEnd;
		}
		else {
			int maxTicks = ModConfig.features.starve.tracker.lossFreq;
			if(maxTicks == 1 || ModConfig.features.starve.tracker.starveLoss == 0) {
				s = "FFFFFF";
			}
			else if(starveTicks + 1 == maxTicks) {
				s = "AA0000";
			}
			else {
				s = "55FF55";
				for(int i = 0; i < TICK_COLOUR_THRESHOLDS.length; i++) {
					if(starveTicks > TICK_COLOUR_THRESHOLDS[i] * maxTicks) {
						s = COLOUR_THRESHOLDS[i];
						break;
					}
				}
			}
		}
		return new Colour(s);
	}
}
