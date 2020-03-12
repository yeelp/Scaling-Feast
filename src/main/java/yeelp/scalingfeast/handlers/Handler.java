package yeelp.scalingfeast.handlers;

import net.minecraftforge.common.MinecraftForge;

/**
 * Base Handler class
 * @author Yeelp
 *
 */
public abstract class Handler 
{
	/**
	 * Register this handler
	 */
	public void register()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
}
