package yeelp.scalingfeast;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import squeek.applecore.api.food.IEdibleBlock;
import yeelp.scalingfeast.command.SFCommand;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.handlers.EnchantmentHandler;
import yeelp.scalingfeast.handlers.FoodHandler;
import yeelp.scalingfeast.handlers.LootTableInjector;
import yeelp.scalingfeast.handlers.PotionHandler;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFFood;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.proxy.Proxy;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.FoodStatsMap;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Logger;

@Mod(modid = ModConsts.MOD_ID, name = ModConsts.MOD_NAME, version = ModConsts.MOD_VERSION)
public class ScalingFeast
{

    public static Logger logger;
    public static Set<ItemFood> alwaysEdibleFoods;
    public static Set<Block> alwaysEdibleBlocks;
    public static boolean hasAppleSkin;
    
    @SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
    public static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        hasAppleSkin = Loader.isModLoaded("appleskin");
        alwaysEdibleFoods = new HashSet<ItemFood>();
        proxy.preInit();
        SFEnchantments.init();
        SFPotion.init();
        info("Registering capability");
        FoodCapProvider.register();
        new CapabilityHandler().register();
        info("Registered capability");
        SFPotion.addBrewingRecipes();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init();
    	info("Registering handlers...");
    	new FoodHandler().register();
        new EnchantmentHandler().register();
        new PotionHandler().register();
        new LootTableInjector().register();
        info("Handlers registered");
    }
    
    @EventHandler 
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    } 
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new SFCommand());
    }
    
    public static void info(String msg)
    {
    	logger.info("[SCALING FEAST] "+msg);
    }
    
    public static void warn(String msg)
    {
    	logger.warn("[SCALING FEAST] "+msg);
    }
    
    public static void err(String msg)
    {
    	logger.error("[SCALING FEAST] "+msg);
    }
}
