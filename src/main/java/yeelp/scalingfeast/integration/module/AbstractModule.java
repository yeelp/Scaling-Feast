package yeelp.scalingfeast.integration.module;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.Handler;

/**
 * A skeleton implementation of and IModule
 * 
 * @author Yeelp
 * @param <Config> the config entry class this module uses.
 */
public abstract class AbstractModule<Config> implements IIntegratable {

	final class ModuleHandler extends Handler {
		@SubscribeEvent
		public final void onPlayerJoinWorld(final EntityJoinWorldEvent evt) {
			if(evt.getEntity() instanceof EntityPlayerMP && AbstractModule.this.enabled()) {
				EntityPlayerMP player = (EntityPlayerMP) evt.getEntity();
				AbstractModule.this.processPlayerUpdate(player);
			}
		}

		@SubscribeEvent
		public final void onPostConfigChanged(final ConfigChangedEvent.PostConfigChangedEvent evt) {
			if(evt.getModID().equals(ModConsts.MOD_ID)) {
				boolean configUpdated = AbstractModule.this.updateFromConfig();
				if(evt.isWorldRunning()) {
					List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
					if(AbstractModule.this.previouslyEnabled() && !AbstractModule.this.enabled()) {
						players.forEach(AbstractModule.this::processPlayerDisable);
					}
					else if((configUpdated || !AbstractModule.this.previouslyEnabled()) && AbstractModule.this.enabled()) {
						players.forEach(AbstractModule.this::processPlayerUpdate);
					}
				}
				AbstractModule.this.setPreviouslyEnabled(AbstractModule.this.enabled());
			}
		}
	}

	private boolean enabled;
	private final Config config;

	/**
	 * Build a new module
	 * 
	 * @param config  the config
	 * @param enabled starting enabled status of the module
	 */
	protected AbstractModule(Config config, boolean enabled) {
		this.config = config;
		this.enabled = enabled;
	}

	@Override
	public boolean preIntegrate(FMLPreInitializationEvent evt) {
		return true;
	}

	@Override
	public final boolean integrate(FMLInitializationEvent evt) {
		this.getHandler().register();
		this.new ModuleHandler().register();
		if(this.enabled()) {
			this.updateFromConfig();
		}
		return true;
	}

	@Override
	public boolean postIntegrate(FMLPostInitializationEvent evt) {
		return true;
	}

	/**
	 * Was this module previously enabled?
	 * 
	 * @return true if the module was last enabled. This doesn't mean it's currently
	 *         disabled.
	 */
	protected final boolean previouslyEnabled() {
		return this.enabled;
	}

	/**
	 * Set if this module was previously enabled
	 * 
	 * @param status new status
	 */
	final void setPreviouslyEnabled(boolean status) {
		this.enabled = status;
	}

	/**
	 * Get the config category for this module
	 * 
	 * @return the config category
	 */
	public final Config getConfig() {
		return this.config;
	}

	/**
	 * Get the handler for this module
	 * 
	 * @return the handler
	 */
	protected abstract Handler getHandler();

	/**
	 * Callback for when this module is enabled, the config updates or when the
	 * player joins the world
	 * 
	 * @param player player to execute the callback on.
	 */
	protected abstract void processPlayerUpdate(EntityPlayerMP player);

	/**
	 * Callback for when this module is disabled
	 * 
	 * @param player player to execute the callback on.
	 */
	protected abstract void processPlayerDisable(EntityPlayerMP player);

	/**
	 * Update this module with information from configs, if needed.
	 * 
	 * @return true if data changed from the update
	 */
	protected abstract boolean updateFromConfig();
}
