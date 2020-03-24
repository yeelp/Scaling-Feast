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
		ScalingFeast.info("Registering enchantments...");
        MinecraftForge.EVENT_BUS.register(new SFEnchantments());
        ScalingFeast.info("Enchantments have been successfully registered.");
        ScalingFeast.info("registering food item...");
        SFFood.init();
        ScalingFeast.info("registering potion");
        MinecraftForge.EVENT_BUS.register(new SFPotion());
        ScalingFeast.info("Food item registered!");
        ScalingFeast.info("Registering capability");
        ExtendedFoodStatsProvider.register();
        new CapabilityHandler().register();
        ScalingFeast.info("Registered capaility");
        SFPotion.addBrewingRecipes();
	}
	public void init()
	{
		ScalingFeast.info("Registering handlers...");
    	new FoodHandler().register();
        new EnchantmentHandler().register();
        new PotionHandler().register();
        new LootTableInjector().register();
        ScalingFeast.info("Handlers registered");
	}
	public void postInit()
	{
		
	}
}
