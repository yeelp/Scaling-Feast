package yeelp.scalingfeast.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.FoodStatsMap;

/**
 * The Metabolism effect. This regens hunger over time.
 * @author Yeelp
 *
 */
public class PotionMetabolism extends PotionBase 
{
	public PotionMetabolism() 
	{
		super(false, 0xF0B78C, 1, 0);
		this.setRegistryName("metabolism");
		this.setPotionName("effect.metabolism");
		this.setBeneficial();
	}
	
	public void performEffect(EntityLivingBase entity, int amplifier)
	{
		if(entity instanceof EntityPlayer && !entity.world.isRemote)
		{
			EntityPlayer player = (EntityPlayer)entity;
			if(player.getFoodStats().getFoodLevel() < ModConsts.VANILLA_MAX_HUNGER)
			{
				player.getFoodStats().addStats(1, 0);
			}
			else if(FoodStatsMap.hasPlayer(player.getUniqueID()) && FoodStatsMap.getExtraFoodLevel(player.getUniqueID()) < FoodStatsMap.getMaxFoodLevel(player.getUniqueID()))
			{
				FoodStatsMap.addFoodStats(player.getUniqueID(), 1, 0);
			}
			else if(player.getFoodStats().getSaturationLevel() < ModConsts.VANILLA_MAX_SAT)
			{
				player.getFoodStats().addStats(1, 0.5f);
			}
			else if(FoodStatsMap.hasPlayer(player.getUniqueID()) && FoodStatsMap.getExtraSatLevels(player.getUniqueID()) < FoodStatsMap.getExtraFoodLevel(player.getUniqueID()))
			{
				FoodStatsMap.addFoodStats(player.getUniqueID(), 1, 1.0f);
			}
		}
	}
	
	//This should tick exactly as often as regen of the same level. We borrow the same code.
	public boolean isReady(int duration, int amplifier)
	{
		int k = 50 >> amplifier;

        if (k > 0)
        {
            return duration % k == 0;
        }
        else
        {
            return true;
        }
	}
}
