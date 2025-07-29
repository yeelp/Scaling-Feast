package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.handlers.GUIIcons;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastBloatedBarMeatDrawable extends AbstractScalingFeastBloatedBarDrawable {

	public ScalingFeastBloatedBarMeatDrawable() {
		super("sf_bloated_bar_meat_part", true);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return true;
	}

	@Override
	protected void bindTexture(Minecraft mc) {
		GUIIcons.bindSFGUIIcons(mc);
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return AbstractScalingFeastBloatedBarDrawable.getNumberOfBloatedIcons(player);
	}

	@Override
	protected int getBaseUCoord(EntityPlayer player) {
		return 0;
	}

	@Override
	protected int getBaseVCoord(EntityPlayer player) {
		return AbstractScalingFeastBloatedBarDrawable.V_COORD_MEAT_PART;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return null;
	}

	@Override
	protected int uCoordShift(float excess) {
		return excess == 0.5f ? 9 : 0;
	}

}
