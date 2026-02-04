package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public final class HungerContainersDrawable extends AbstractVanillaStatBarDrawable {

	static final int U_COORD_BASE = 16;
	static final int HUNGER_EFFECT_U_COORD_OFFSET = 117;
	
	public HungerContainersDrawable() {
		super("sf_empty_hunger_bar", U_COORD_BASE, HUNGER_EFFECT_U_COORD_OFFSET);
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return DrawUtils.getContainerCount(player);
	}

	@Override
	protected int uCoordShift(float excess) {
		return 0;
	}

}
