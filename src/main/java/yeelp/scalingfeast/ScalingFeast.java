package yeelp.scalingfeast;

import java.io.File;
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
import yeelp.scalingfeast.handlers.ModuleHandler;
import yeelp.scalingfeast.handlers.PacketHandler;
import yeelp.scalingfeast.handlers.PotionHandler;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.proxy.Proxy;
import yeelp.scalingfeast.util.ConfigVersionChecker;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.StarvationTracker;

@Mod(modid = ModConsts.MOD_ID, name = ModConsts.MOD_NAME, version = ModConsts.MOD_VERSION)
public class ScalingFeast
{
    public static Logger logger;
    public static boolean hasAppleSkin;
    public static boolean hasSolCarrot;
    public static boolean hasSpiceOfLife;
    public static File config;
    @SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
    public static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        hasAppleSkin = Loader.isModLoaded(ModConsts.APPLESKIN_ID);
        hasSolCarrot = Loader.isModLoaded(ModConsts.SOLCARROT_ID);
        hasSpiceOfLife = Loader.isModLoaded(ModConsts.SPICEOFLIFE_ID);
        config = new File(event.getModConfigurationDirectory(), "scalingfeast.cfg");
        ConfigVersionChecker checker = new ConfigVersionChecker(ModConfig.class, config);
        if(checker.isConfigOutdated())
        {
        	ScalingFeast.warn("This config file is outdated! ScalingFeast will try to transfer your changes to the newer config version!");
        	checker.updateConfig();
        }
        if(hasAppleSkin)
        {
        	info("Scaling Feast found AppleSkin!");
        }
        if(hasSolCarrot)
        {
        	info("Scaling Feast found Spice of Life: Carrot Edition!");
        }
        if(hasSpiceOfLife)
        {
        	info("Scaling Feast found Spice of Life!");
        }
        proxy.preInit();
        SFEnchantments.init();
        SFPotion.init();
        FoodCap.register();
        StarvationTracker.register();
        FoodCapModifier.register();
        new CapabilityHandler().register();
        PacketHandler.init();
        info("Scaling Feast pre-initialization complete!");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init();
    	new FoodHandler().register();
        new EnchantmentHandler().register();
        new PotionHandler().register();
        new LootTableInjector().register();
        new ModuleHandler().register();
        info("Scaling Feast initialization complete!");
    }
    
    @EventHandler 
    public void postInit(FMLPostInitializationEvent event)
    {
    	if(hasSolCarrot)
    	{
    		SOLCarrotHelper.init();
    	}
    	if(hasSpiceOfLife)
    	{
    		SpiceOfLifeHelper.init();
    	}
    	info("Scaling Feast post-initialization complete!");
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
