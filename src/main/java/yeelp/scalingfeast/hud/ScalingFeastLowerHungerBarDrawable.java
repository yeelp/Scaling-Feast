package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastLowerHungerBarDrawable extends AbstractScalingFeastStatBarDrawable {

	public ScalingFeastLowerHungerBarDrawable() {
		super("sf_hunger_bar_lower", 0, 0, true);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		int amount = player.getFoodStats().getFoodLevel();
		return amount % ModConsts.VANILLA_MAX_HUNGER != 0 && amount > (ModConfig.hud.replaceVanilla ? 1 : 2) * ModConsts.VANILLA_MAX_HUNGER;
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return ModConsts.VANILLA_MAX_HUNGER/2;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return DrawUtils.getHungerColour(player.getFoodStats().getFoodLevel() - ModConsts.VANILLA_MAX_HUNGER);
	}

	@Override
	protected int uCoordShift(float excess) {
		return 0;
	}

}
