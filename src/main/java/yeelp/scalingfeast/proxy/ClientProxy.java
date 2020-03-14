package yeelp.scalingfeast.proxy;

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
	}
	public void postInit()
	{
		super.postInit();
	}
}
