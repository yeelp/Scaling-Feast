package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.Colour;

public final class ScalingFeastUpperBloatedBarDrawable extends AbstractScalingFeastBloatedBarDrawable {

	public ScalingFeastUpperBloatedBarDrawable() {
		super("sf_bloated_bar_upper", true);
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
		short bloat = ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount();
		float count = AbstractScalingFeastStatBarDrawable.getIconCountForTopmostBar(bloat);
		if(bloat %2 != 0 && bloat < ModConsts.VANILLA_MAX_HUNGER) {
			return count + 0.25f;
		}
		return count;
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
		return DrawUtils.getBloatedColour(ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount());
	}

	@Override
	protected int uCoordShift(float excess) {
		if(excess == 0.5f) {
			return 9;
		}
		else if(excess == 0.75f) {
			return 18;
		}
		return 0;
	}

}
