package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.util.Colour;

import javax.annotation.Nullable;

public final class CustomHungerBarBaseDrawable extends CustomHungerBarDrawable {

    public CustomHungerBarBaseDrawable() {
        super("sf_custom_hunger_bar_base");
    }

    @Nullable
    @Override
    protected Colour getColour(EntityPlayer player) {
        return null;
    }

    @Override
    protected int uCoordShift(float excess) {
        return excess >= 0.5f ? 9 : 0;
    }

    @Override
    public boolean shouldDraw(EntityPlayer player) {
        return true;
    }

    @Override
    protected int getBaseVCoord(EntityPlayer player) {
        return ModConfig.hud.iconSet.getContainerVCoord() - 9;
    }
}
