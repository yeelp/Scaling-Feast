package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastLowerSaturationBarDrawable extends AbstractScalingFeastStatBarDrawable {

	public ScalingFeastLowerSaturationBarDrawable() {
		super("sf_saturation_bar_lower", 0, 9);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		float sat = player.getFoodStats().getSaturationLevel();
		return sat % ModConsts.VANILLA_MAX_SAT != 0 && sat > ModConsts.VANILLA_MAX_SAT; 
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return ModConsts.VANILLA_MAX_SAT / 2.0f;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return DrawUtils.getSaturationColour(player.getFoodStats().getSaturationLevel() - ModConsts.VANILLA_MAX_SAT);
	}

	@Override
	protected int uCoordShift(float excess) {
		return 0;
	}

}
