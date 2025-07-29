package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastUpperSaturationBarDrawable extends AbstractScalingFeastStatBarDrawable {

	public ScalingFeastUpperSaturationBarDrawable() {
		super("sf_saturation_bar_upper", 0, 9);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return true;
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return AbstractScalingFeastStatBarDrawable.getIconCountForTopmostBar(stats.getSaturationLevel());
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return DrawUtils.getSaturationColour(player.getFoodStats().getSaturationLevel());
	}

	@Override
	protected int uCoordShift(float excess) {
		return DrawUtils.uCoordShiftForSaturation(excess);
	}

}
