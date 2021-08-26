package yeelp.scalingfeast.api;

import yeelp.scalingfeast.api.impl.ScalingFeastAPIImpl;

/**
 * Collection of methods used by Scaling Feast.
 * 
 * @author Yeelp
 *
 */
public abstract class ScalingFeastAPI {
	public static IScalingFeastAccessor accessor;
	public static IScalingFeastMutator mutator;

	/**
	 * Initialize the API
	 */
	public static final void init() {
		ScalingFeastAPIImpl.values();
	}
}
