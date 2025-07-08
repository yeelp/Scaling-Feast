package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastVanillaHungerOverrideDrawable extends AbstractScalingFeastStatBarDrawable {

	public ScalingFeastVanillaHungerOverrideDrawable() {
		super("sf_vanilla_hunger_override", 0, 0, true);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return ModConfig.hud.replaceVanilla && player.getFoodStats().getFoodLevel() < ModConsts.VANILLA_MAX_HUNGER;
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return stats.getFoodLevel() / 2.0f;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return DrawUtils.getHungerColour(19);
	}

	@Override
	protected int uCoordShift(float excess) {
		return excess == 0.5f ? 18 : 0;
	}

}
