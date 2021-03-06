package yeelp.scalingfeast.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;

/**
 * The Metabolism effect. This regens hunger over time.
 * @author Yeelp
 *
 */
public class PotionMetabolism extends PotionBase 
{
	public PotionMetabolism() 
	{
		super(false, 0xF0B78C, 1, 0, false);
		this.setRegistryName("metabolism");
		this.setPotionName("effect.metabolism");
		this.setBeneficial();
	}
	
	public void performEffect(EntityLivingBase entity, int amplifier)
	{
		if(entity instanceof EntityPlayer && !entity.world.isRemote)
		{
			EntityPlayer player = (EntityPlayer)entity;
			FoodStats fs = player.getFoodStats();
			if(fs.getFoodLevel() < player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel(player.getCapability(FoodCapModifierProvider.foodCapMod, null)))
			{
				AppleCoreAPI.mutator.setHunger(player, fs.getFoodLevel() + 1);
			}
			else if(fs.getSaturationLevel() < player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel(player.getCapability(FoodCapModifierProvider.foodCapMod, null)))
			{
				AppleCoreAPI.mutator.setSaturation(player, fs.getSaturationLevel() + 1);
			}
			ScalingFeastAPI.mutator.capPlayerSaturation(player);
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
