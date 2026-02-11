package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.config.ModConfig;
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
		if(ModConfig.hud.bloatedSet.isCustom()) {
			GUIIcons.bindSFGUIIcons(mc);
		}
		else {
			GUIIcons.unbindSFGUIIcons(mc);
		}
	}

	@Override
	protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
		return (int) Math.ceil(AbstractScalingFeastBloatedBarDrawable.getNumberOfBloatedIcons(player));
	}

	@Override
	protected int getBaseUCoord(EntityPlayer player) {
		if(ModConfig.hud.bloatedSet.isCustom()) {
			return (player.isPotionActive(MobEffects.HUNGER)) ? 27 : 0;
		}
		return HungerContainersDrawable.U_COORD_BASE + (player.isPotionActive(MobEffects.HUNGER) ? HungerContainersDrawable.HUNGER_EFFECT_U_COORD_OFFSET : 0);
	}

	@Override
	protected int getBloatedBaseVCoord(EntityPlayer player) {
		//because using vanilla texture map, subtract the icon set v offset. It will be added by the superclass and "canceled out".
		return (ModConfig.hud.bloatedSet.isCustom() ? ModConfig.hud.bloatedSet.getContainerVCoord() : AbstractVanillaStatBarDrawable.V_COORD_BASE) - ModConfig.hud.bloatedSet.getVOffset();
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
