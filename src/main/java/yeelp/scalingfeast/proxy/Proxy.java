package yeelp.scalingfeast.proxy;

import net.minecraftforge.common.MinecraftForge;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.handlers.EnchantmentHandler;
import yeelp.scalingfeast.handlers.FoodHandler;
import yeelp.scalingfeast.handlers.LootTableInjector;
import yeelp.scalingfeast.init.SFBlocks;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFFood;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.util.FoodCapProvider;

public class Proxy 
{
	public void preInit()
	{
		SFBlocks.init();
		SFFood.init();
	}
	public void init()
	{
	
	}
	public void postInit()
	{
		
	}
}
