package yeelp.scalingfeast;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.blocks.ExhaustionIncreasingBlock;
import yeelp.scalingfeast.capability.IBloatedHunger;
import yeelp.scalingfeast.capability.IStarvationStats;
import yeelp.scalingfeast.capability.IStarveExhaustionTracker;
import yeelp.scalingfeast.command.SFCommand;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.features.SFFeatures;
import yeelp.scalingfeast.handlers.*;
import yeelp.scalingfeast.init.*;
import yeelp.scalingfeast.integration.ModIntegrationKernel;
import yeelp.scalingfeast.integration.OptionalMixinLoader;
import yeelp.scalingfeast.items.IItemDescribable;
import yeelp.scalingfeast.lib.worldgen.OreGenerator;
import yeelp.scalingfeast.potion.PotionExhaustion;
import yeelp.scalingfeast.proxy.Proxy;

import java.io.File;

@Mod(modid = ModConsts.MOD_ID, name = ModConsts.MOD_NAME, version = ModConsts.MOD_VERSION, dependencies = "required-after:applecore@[3.4.0,)", certificateFingerprint = "3114a3e1038120ee58038ff5a4cda0cf7d46bed0")
public class ScalingFeast {
	public static Logger logger;
	@SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
	public static Proxy proxy;
	private static boolean fingerprintViolation;
	private static File configDirectory;

	/**
	 * Scaling Feast pre initialization
	 * @param event the FMLPreInitializationEvent
	 */
	@SuppressWarnings("static-method")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		ScalingFeastAPI.init();
		info(String.format("Scaling Feast is version %s", ModConsts.MOD_VERSION));
		if(OptionalMixinLoader.wasFermiumBooterPresent()) {
			info("Scaling Feast loaded optional mixins with Fermium Booter!");
		}
		ModIntegrationKernel.load();
		configDirectory = event.getModConfigurationDirectory();
		if(fingerprintViolation) {
			proxy.handleFingerprintViolation();
		}
		proxy.preInit();
		SFOreDict.init();
		new SFAttributes().register();
		SFEnchantments.init();
		SFPotion.init();
		SFSounds.init();
		IStarvationStats.register();
		IBloatedHunger.register();
		IStarveExhaustionTracker.register();		
		PacketHandler.init();
		ModIntegrationKernel.preInit(event);
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
		GameRegistry.registerWorldGenerator(OreGenerator.getInstance(), 1);
		new CapabilityHandler().register();
		new GenericHandler().register();
		new LootTableInjector().register();
		new PotionExhaustion.ExhaustionHandler().register();
		new IItemDescribable.TooltipHandler().register();
		new BloatedHandler().register();
		new ExhaustionIncreasingBlock.ExhaustionHandler().register();
		SFFeatures.init();
		ModIntegrationKernel.init(event);
		info("Scaling Feast initialization complete!");
	}

	/**
	 * Scaling Feast post initialization
	 * @param event the FMLPostInitializationEvent
	 */
	@SuppressWarnings("static-method")
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		SFRecipes.init();
		ModIntegrationKernel.postInit(event);
		
		info("Scaling Feast post-initialization complete!");
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new SFCommand());
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void fingerprintViolation(FMLFingerprintViolationEvent event) {
		if(event.isDirectory()) {
			debug("Fingerprint doesn't matter in dev environment");
		}
		else {
			fingerprintViolation = true;
		}
	}

	/**
	 * Log a message at the info level only when in debug mode
	 * 
	 * @param msg message to log
	 */
	@SuppressWarnings("unused")
	public static void debug(String msg) {
		if(ModConfig.debug) {
			logger.info("[SCALING FEAST (DEBUG)] {}", msg);
		}
	}

	/**
	 * Log a message at the info level
	 * 
	 * @param msg message to log
	 */
	public static void info(String msg) {
		logger.info("[SCALING FEAST] {}", msg);
	}

	/**
	 * Log a message at the warning level
	 * 
	 * @param msg message to log
	 */
	public static void warn(String msg) {
		logger.warn("[SCALING FEAST] {}", msg);
	}

	/**
	 * Log a message at the error level
	 * 
	 * @param msg message to log
	 */
	public static void err(String msg) {
		logger.error("[SCALING FEAST] {}", msg);
	}

	/**
	 * Log a message at the fatal level
	 * 
	 * @param msg message to log
	 */
	public static void fatal(String msg) {
		logger.fatal("[SCALING FEAST] {}", msg);
	}

	public static File getModConfigDirectory() {
		return configDirectory;
	}
}
