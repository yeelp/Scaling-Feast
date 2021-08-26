package yeelp.scalingfeast.integration.module;

/**
 * A base interface for SF integration modules
 * @author Yeelp
 *
 */
public interface IIntegratable {
	/**
	 * integrate this module with Scaling Feast; including registering any handlers needed
	 * @return true if successful
	 */
	boolean integrate();
	
	/**
	 * Is this module enabled?
	 * @return true if enabled.
	 */
	boolean enabled();
}
