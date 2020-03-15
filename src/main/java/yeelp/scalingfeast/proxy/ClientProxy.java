package yeelp.scalingfeast.proxy;

import yeelp.scalingfeast.handlers.HUDOverlayHandler;
import yeelp.scalingfeast.init.SFFood;

public class ClientProxy extends Proxy 
{
	public void preInit()
	{
		super.preInit();
		SFFood.registerRenders();
	}
	public void init() 
	{
		super.init();
		new HUDOverlayHandler().register();
	}
	public void postInit()
	{
		super.postInit();
	}
}
