package yeelp.scalingfeast.proxy;

import net.minecraftforge.common.MinecraftForge;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.handlers.EnchantmentHandler;
import yeelp.scalingfeast.handlers.FoodHandler;
import yeelp.scalingfeast.handlers.LootTableInjector;
import yeelp.scalingfeast.handlers.PotionHandler;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFFood;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.util.ExtendedFoodStatsProvider;

public class Proxy 
{
	public void preInit()
	{
		SFFood.init();
	}
	public void init()
	{
	
	}
	public void postInit()
	{
		
	}
}
