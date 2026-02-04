package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastStarvationTrackerDrawable extends AbstractScalingFeastStatBarDrawable {

	private static final Colour TRACKER_COLOUR = new Colour("AA0000");
	
	public ScalingFeastStarvationTrackerDrawable() {
		super("sf_starvation_tracker", 0, 9, false);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return player.getFoodStats().getFoodLevel() == 0;
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		int max = Math.min(ModConsts.VANILLA_MAX_HUNGER, AppleCoreAPI.accessor.getMaxHunger(player));
		return ((max / (ModConfig.features.starve.tracker.lossFreq - 1.0f)) * ScalingFeastAPI.accessor.getSFFoodStats(player).getStarvationTrackerCount())/2.0f;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return TRACKER_COLOUR;
	}

	@Override
	protected int uCoordShift(float excess) {
		return DrawUtils.uCoordShiftForSaturation(excess);
	}

}
