package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;

public final class VanillaHungerDrawable extends AbstractVanillaStatBarDrawable {

	private static final int U_COORD_BASE = 52;
	private static final int HUNGER_U_COORD_OFFSET = 36;
	
	public VanillaHungerDrawable() {
		super("sf_vanilla_hunger", U_COORD_BASE, HUNGER_U_COORD_OFFSET);
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return Math.min(ModConsts.VANILLA_MAX_HUNGER, stats.getFoodLevel()) / 2.0f;
	}

	@Override
	protected int uCoordShift(float excess) {
		return excess == 0.5f ? 9 : 0;
	}

}
