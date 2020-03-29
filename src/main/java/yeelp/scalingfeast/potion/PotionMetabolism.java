package yeelp.scalingfeast.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.FoodCapProvider;
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
			if(player.getFoodStats().getFoodLevel() < player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel())
			{
				ItemFood dummy = new ItemFood(1, 0, false);
				player.getFoodStats().addStats(dummy, new ItemStack(dummy));
			}
			else if(player.getFoodStats().getSaturationLevel() < player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel())
			{
				ItemFood dummy = new ItemFood(1, 0.5f, false);
				player.getFoodStats().addStats(dummy, new ItemStack(dummy));
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
