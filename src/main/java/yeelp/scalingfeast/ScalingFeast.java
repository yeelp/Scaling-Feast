package yeelp.scalingfeast;

import java.io.File;
import java.util.Arrays;
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
import net.minecraftforge.fml.relauncher.Side;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.ScalingFeastAPIImpl;
import yeelp.scalingfeast.command.SFCommand;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.handlers.CompatibilityHandler;
import yeelp.scalingfeast.handlers.EnchantmentHandler;
import yeelp.scalingfeast.handlers.FoodHandler;
import yeelp.scalingfeast.handlers.LootTableInjector;
import yeelp.scalingfeast.handlers.ModuleHandler;
import yeelp.scalingfeast.handlers.PacketHandler;
import yeelp.scalingfeast.handlers.PotionHandler;
import yeelp.scalingfeast.handlers.TooltipHandler;
import yeelp.scalingfeast.helpers.AppleSkinHelper;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.proxy.Proxy;
import yeelp.scalingfeast.util.BloatedHunger;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.HeartyShankUsageTicker;
import yeelp.scalingfeast.util.SFAttributes;
import yeelp.scalingfeast.util.StarvationTracker;
import yeelp.scalingfeast.util.StarveExhaustionTracker;

@Mod(modid = ModConsts.MOD_ID, name = ModConsts.MOD_NAME, version = ModConsts.MOD_VERSION, dependencies="required-after:applecore@[3.4.0,)")
public class ScalingFeast
{
    public static Logger logger;
    public static boolean hasAppleSkin;
    public static boolean hasSolCarrot;
    public static boolean hasSpiceOfLife;
    private static final boolean debug = false;
    @SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
    public static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ScalingFeastAPI.init();
        hasAppleSkin = Loader.isModLoaded(ModConsts.APPLESKIN_ID);
        hasSolCarrot = Loader.isModLoaded(ModConsts.SOLCARROT_ID);
        hasSpiceOfLife = Loader.isModLoaded(ModConsts.SPICEOFLIFE_ID);
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
        new SFAttributes().register();
        SFEnchantments.init();
        SFPotion.init();
        FoodCap.register();
        StarvationTracker.register();
        FoodCapModifier.register();
        BloatedHunger.register();
        HeartyShankUsageTicker.register();
        StarveExhaustionTracker.register();
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
        new TooltipHandler().register();
        if(ModConfig.modules.sol.enabled || ModConfig.modules.spiceoflife.enabled)
        {	
        	new ModuleHandler().register();
        }
        if(hasAppleSkin && event.getSide() == Side.CLIENT)
        {
        	//new CompatibilityHandler().register();
        }
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
    	if(hasAppleSkin)
    	{
    		try
			{
				AppleSkinHelper.init();
			}
			catch (ClassNotFoundException e)
			{
				err("Couldn't load AppleSkin compatibility! Encountered a ClassNotFoundException.");
				err(Arrays.toString(e.getStackTrace()));
			}
    	}
    	info("Scaling Feast post-initialization complete!");
    } 
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new SFCommand());
    }
    
    /**
     * Log a message at the info level only when in debug mode
     * @param msg message to log 
     */
    public static void debug(String msg)
    {
    	if(debug)
    	{
    		info(msg);
    	}
    }
    
    /**
     * Log a message at the info level
     * @param msg message to log
     */
    public static void info(String msg)
    {
    	logger.info("[SCALING FEAST] "+msg);
    }
    
    /**
     * Log a message at the warning level
     * @param msg message to log
     */
    public static void warn(String msg)
    {
    	logger.warn("[SCALING FEAST] "+msg);
    }
    
    /**
     * Log a message at the error level
     * @param msg message to log
     */
    public static void err(String msg)
    {
    	logger.error("[SCALING FEAST] "+msg);
    }
    
    /**
     * Log a message at the fatal level
     * @param msg message to log
     */
    public static void fatal(String msg)
    {
    	logger.fatal("[SCALING FEAST "+msg);
    }
}
