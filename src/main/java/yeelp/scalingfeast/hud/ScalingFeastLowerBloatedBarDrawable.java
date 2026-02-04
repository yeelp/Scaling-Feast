package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastLowerBloatedBarDrawable extends AbstractScalingFeastBloatedBarDrawable {

	public ScalingFeastLowerBloatedBarDrawable() {
		super("sf_bloated_bar_lower", true);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		short bloatedAmount = ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount();
		return bloatedAmount % ModConsts.VANILLA_MAX_HUNGER != 0 && bloatedAmount > ModConsts.VANILLA_MAX_HUNGER;
	}

	@Override
	protected void bindTexture(Minecraft mc) {
		GUIIcons.bindSFGUIIcons(mc);
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return ModConsts.VANILLA_MAX_HUNGER / 2.0f;
	}

	@Override
	protected int getBaseUCoord(EntityPlayer player) {
		return 0;
	}

	@Override
	protected int getBloatedBaseVCoord(EntityPlayer player) {
		return AbstractScalingFeastBloatedBarDrawable.V_COORD_COLOURED_OVERLAY;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return DrawUtils.getBloatedColour(ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount() - ModConsts.VANILLA_MAX_HUNGER);
	}

	@Override
	protected int uCoordShift(float excess) {
		return 0;
	}

}
