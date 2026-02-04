package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;

public abstract class AbstractScalingFeastBloatedBarDrawable extends AbstractStatBarDrawable {

	protected static final int V_COORD_COLOURED_OVERLAY = 18;
	protected static final int V_COORD_MEAT_PART = 27;
	
	public AbstractScalingFeastBloatedBarDrawable(String name, boolean hasHungerOverlay) {
		super(name, hasHungerOverlay);
	}

	@Override
	protected int jitterIndexOffset(EntityPlayer player) {
		return (int) Math.min(Math.ceil(AppleCoreAPI.accessor.getMaxHunger(player) / 2.0f), 10);
	}

	@Override
	protected final int getBaseVCoord(EntityPlayer player) {
		return this.getBloatedBaseVCoord(player) + ModConfig.hud.iconSet.getVOffset();
	}

	protected abstract int getBloatedBaseVCoord(EntityPlayer player);

	/**
	 * Get the number of bloated icons that should be drawn. a result ending in 0.5
	 * indicates a half shank.
	 * 
	 * @param player player to reference
	 * @return number of bloated icons to draw.
	 */
	protected static float getNumberOfBloatedIcons(EntityPlayer player) {
		return Math.min(ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount(), ModConsts.VANILLA_MAX_HUNGER) / 2.0f;
	}
}
