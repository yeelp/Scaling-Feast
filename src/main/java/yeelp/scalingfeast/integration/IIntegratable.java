package yeelp.scalingfeast.integration;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * A base interface for SF integration modules
 * @author Yeelp
 *
 */
public interface IIntegratable {
	
	/**
	 * Any setup that needs to be done early.
	 * @param evt the {@link FMLPreInitializationEvent}
	 * @return true if pre integration was successful, will register for integration.
	 */
	boolean preIntegrate(FMLPreInitializationEvent evt);
	
	/**
	 * integrate this module with Scaling Feast; including registering any handlers needed
	 * @param evt the {@link FMLInitializationEvent}
	 * @return true if successful, will register for post integration
	 */
	boolean integrate(FMLInitializationEvent evt);
	
	/**
	 * Callback during post initialization if anything needs to be done.
	 * @param evt the {@link FMLPostInitializationEvent}
	 * @return true if post integration successful, will add to loaded integrations.
	 */
	boolean postIntegrate(FMLPostInitializationEvent evt);
	
	/**
	 * Is this module enabled?
	 * @return true if enabled.
	 */
	default boolean enabled() {
		return true;
	}
}
