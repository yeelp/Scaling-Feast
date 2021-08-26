package yeelp.scalingfeast.config.modules;

/**
 * Base config category for modules
 * @author Yeelp
 *
 */
public abstract class SFAbstractModuleConfig {

	/**
	 * Get the enabled entry for this module
	 * @return the enabled entry.
	 */
	public abstract boolean getEnabledEntry();
}
