package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastUpperHungerBarDrawable extends AbstractScalingFeastStatBarDrawable {

	public ScalingFeastUpperHungerBarDrawable() {
		super("sf_hunger_bar_upper", 0, 0, true);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return player.getFoodStats().getFoodLevel() > ModConsts.VANILLA_MAX_HUNGER;
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return AbstractScalingFeastStatBarDrawable.getIconCountForTopmostBar(stats.getFoodLevel());
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return DrawUtils.getHungerColour(player.getFoodStats().getFoodLevel());
	}

	@Override
	protected int uCoordShift(float excess) {
		return excess == 0.5f ? 9 : 0;
	}

}
