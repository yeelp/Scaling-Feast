package yeelp.scalingfeast.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.ModConsts;

public abstract class CustomHungerBarDrawable extends AbstractScalingFeastStatBarDrawable {

    public CustomHungerBarDrawable(String name) {
        super(name, 0, 0, true);
    }

    @Override
    protected float getAmountToDraw(EntityPlayer player, FoodStats stats) {
        int food = stats.getFoodLevel();
        if(food > ModConsts.VANILLA_MAX_HUNGER) {
            return 10;
        }
        float result = AbstractScalingFeastStatBarDrawable.getIconCountForTopmostBar(food);
        float uOffset = food < ModConsts.VANILLA_MAX_HUNGER ? 0.25f : 0;
        return stats.getFoodLevel() % 2 == 0 ? result : result + uOffset;
    }
}
