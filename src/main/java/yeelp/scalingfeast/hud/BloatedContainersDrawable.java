package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.handlers.GUIIcons;
import yeelp.scalingfeast.util.Colour;

public final class BloatedContainersDrawable extends AbstractScalingFeastBloatedBarDrawable {
	
	public BloatedContainersDrawable() {
		super("sf_bloated_containers", false);
	}

	@Override
	public boolean shouldDraw(EntityPlayer player) {
		return true;
	}

	@Override
	protected void bindTexture(Minecraft mc) {
		GUIIcons.unbindSFGUIIcons(mc);
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return (int) Math.ceil(AbstractScalingFeastBloatedBarDrawable.getNumberOfBloatedIcons(player));
	}

	@Override
	protected int getBaseUCoord(EntityPlayer player) {
		return HungerContainersDrawable.U_COORD_BASE + (player.isPotionActive(MobEffects.HUNGER) ? HungerContainersDrawable.HUNGER_EFFECT_U_COORD_OFFSET : 0);
	}

	@Override
	protected int getBaseVCoord(EntityPlayer player) {
		return AbstractVanillaStatBarDrawable.V_COORD_BASE;
	}

	@Override
	protected Colour getColour(EntityPlayer player) {
		return null;
	}

	@Override
	protected int uCoordShift(float excess) {
		return 0;
	}

}
