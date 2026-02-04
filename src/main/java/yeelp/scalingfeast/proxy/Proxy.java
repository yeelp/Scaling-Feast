package yeelp.scalingfeast.proxy;

import yeelp.scalingfeast.init.SFBlocks;
import yeelp.scalingfeast.init.SFItems;

public class Proxy extends AbstractProxy {
	@Override
	public void preInit() {
		SFBlocks.init();
		SFItems.init();
	}

	@Override
	public void init() {
		//nothing
	}

	@Override
	public void postInit() {
		//nothing
	}
}
