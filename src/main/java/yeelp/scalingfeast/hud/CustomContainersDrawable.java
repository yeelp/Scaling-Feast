package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.util.Colour;

import javax.annotation.Nullable;

public final class CustomContainersDrawable extends AbstractScalingFeastStatBarDrawable {

    public CustomContainersDrawable() {
        super("sf_custom_hunger_containers", 0, 0, true);
    }

    @Override
    protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
        return DrawUtils.getContainerCount(player);
    }

    @Nullable
    @Override
    protected Colour getColour(EntityPlayer player) {
        return null;
    }

    @Override
    protected int uCoordShift(float excess) {
        return 0;
    }

    @Override
    public boolean shouldDraw(EntityPlayer player) {
        return true;
    }

    @Override
    protected int getBaseVCoord(EntityPlayer player) {
        return ModConfig.hud.iconSet.getContainerVCoord();
    }
}
