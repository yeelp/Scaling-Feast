package yeelp.scalingfeast;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.capability.IBloatedHunger;
import yeelp.scalingfeast.capability.IStarvationTracker;
import yeelp.scalingfeast.capability.IStarveExhaustionTracker;
import yeelp.scalingfeast.command.SFCommand;
import yeelp.scalingfeast.features.SFFeatures;
import yeelp.scalingfeast.handlers.BloatedHandler;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.handlers.GenericHandler;
import yeelp.scalingfeast.handlers.LootTableInjector;
import yeelp.scalingfeast.handlers.PacketHandler;
import yeelp.scalingfeast.helpers.AppleSkinHelper;
import yeelp.scalingfeast.init.SFAttributes;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.integration.ModIntegrationKernel;
import yeelp.scalingfeast.items.IItemDescribable;
import yeelp.scalingfeast.potion.PotionExhaustion;
import yeelp.scalingfeast.proxy.Proxy;

@Mod(modid = ModConsts.MOD_ID, name = ModConsts.MOD_NAME, version = ModConsts.MOD_VERSION, dependencies = "required-after:applecore@[3.4.0,)")
public class ScalingFeast {
	public static Logger logger;
	public static boolean hasAppleSkin;
	public static boolean hasSolCarrot;
	public static boolean hasSpiceOfLife;
	private static final boolean debug = false;
	@SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
	public static Proxy proxy;

	/**
	 * Scaling Feast pre initialization
	 * @param event the FMLPreInitializationEvent
	 */
	@SuppressWarnings("static-method")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		ScalingFeastAPI.init();
		proxy.preInit();
		new SFAttributes().register();
		SFEnchantments.init();
		SFPotion.init();
		IStarvationTracker.register();
		IBloatedHunger.register();
		IStarveExhaustionTracker.register();		
		PacketHandler.init();
		info("Scaling Feast pre-initialization complete!");	
	}

	/**
	 * Scaling Feast initialization
	 * @param event the FMLInitializationEvent
	 */
	@SuppressWarnings("static-method")
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		new CapabilityHandler().register();
		new GenericHandler().register();
		new LootTableInjector().register();
		new PotionExhaustion.ExhaustionHandler().register();
		new IItemDescribable.TooltipHandler().register();
		new BloatedHandler().register();
		SFFeatures.init();
		ModIntegrationKernel.load();
		info("Scaling Feast initialization complete!");
	}

	/**
	 * Scaling Feast post initialization
	 * @param event the FMLPostInitializationEvent
	 */
	@SuppressWarnings("static-method")
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		if(hasAppleSkin) {
			try {
				AppleSkinHelper.init();
			}
			catch(ClassNotFoundException e) {
				err("Couldn't load AppleSkin compatibility! Encountered a ClassNotFoundException.");
				err(Arrays.toString(e.getStackTrace()));
			}
		}
		info("Scaling Feast post-initialization complete!");
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new SFCommand());
	}

	/**
	 * Log a message at the info level only when in debug mode
	 * 
	 * @param msg message to log
	 */
	public static void debug(String msg) {
		if(debug) {
			info(msg);
		}
	}

	/**
	 * Log a message at the info level
	 * 
	 * @param msg message to log
	 */
	public static void info(String msg) {
		logger.info("[SCALING FEAST] " + msg);
	}

	/**
	 * Log a message at the warning level
	 * 
	 * @param msg message to log
	 */
	public static void warn(String msg) {
		logger.warn("[SCALING FEAST] " + msg);
	}

	/**
	 * Log a message at the error level
	 * 
	 * @param msg message to log
	 */
	public static void err(String msg) {
		logger.error("[SCALING FEAST] " + msg);
	}

	/**
	 * Log a message at the fatal level
	 * 
	 * @param msg message to log
	 */
	public static void fatal(String msg) {
		logger.fatal("[SCALING FEAST] " + msg);
	}
}
