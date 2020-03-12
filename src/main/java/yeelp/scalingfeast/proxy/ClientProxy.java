package yeelp.scalingfeast.proxy;

import yeelp.scalingfeast.init.SFFood;

public class ClientProxy implements Proxy {

	@Override
	public void init() 
	{
		SFFood.registerRenders();

	}

}
