package yeelp.scalingfeast;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import yeelp.scalingfeast.command.SFCommand;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.handlers.EnchantmentHandler;
import yeelp.scalingfeast.handlers.FoodHandler;
import yeelp.scalingfeast.handlers.LootTableInjector;
import yeelp.scalingfeast.handlers.PacketHandler;
import yeelp.scalingfeast.handlers.PotionHandler;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.proxy.Proxy;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.StarvationTracker;

@Mod(modid = ModConsts.MOD_ID, name = ModConsts.MOD_NAME, version = ModConsts.MOD_VERSION)
public class ScalingFeast
{

    public static Logger logger;
    public static boolean hasAppleSkin;
    
    @SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
    public static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        hasAppleSkin = Loader.isModLoaded("appleskin");
        proxy.preInit();
        SFEnchantments.init();
        SFPotion.init();
        info("Registering capability");
        FoodCap.register();
        StarvationTracker.register();
        new CapabilityHandler().register();
        info("Registered capability");
        SFPotion.addBrewingRecipes();
        PacketHandler.init();
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
