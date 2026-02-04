package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
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
		float sat = stats.getSaturationLevel();
		if(sat == 0) {
			return 0;
		}
		int wholeSat = (int) sat;
		float iconCount;
		if(wholeSat % 2 == 0) {
			iconCount = sat == wholeSat ? sat : wholeSat + 0.4f;
		}
		else {
			iconCount = sat == wholeSat ? sat : wholeSat + 0.8f;
		}
		if(sat % ModConsts.VANILLA_MAX_SAT == 0) {
			iconCount = ModConsts.VANILLA_MAX_SAT;
		}
		else {
			iconCount = iconCount % ModConsts.VANILLA_MAX_SAT;
		}
		return iconCount / 2.0f;
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
