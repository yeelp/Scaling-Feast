package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.util.Colour;

import javax.annotation.Nullable;

public class CustomHungerBarFirstBarDrawable extends CustomHungerBarDrawable {

    public CustomHungerBarFirstBarDrawable() {
        super("sf_custom_first_hunger_bar");
    }

    @Nullable
    @Override
    protected Colour getColour(EntityPlayer player) {
        return DrawUtils.getHungerColour(19);
    }

    @Override
    protected int uCoordShift(float excess) {
        return excess >= 0.75f ? 18 : excess >= 0.5f ? 9 : 0;
    }

    @Override
    public boolean shouldDraw(EntityPlayer player) {
        return player.getFoodStats().getFoodLevel() <= (ModConfig.hud.replaceVanilla ? 1 : 2) * ModConsts.VANILLA_MAX_HUNGER;
    }
}
