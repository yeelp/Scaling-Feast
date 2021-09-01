package yeelp.scalingfeast.proxy;

import yeelp.scalingfeast.init.SFBlocks;
import yeelp.scalingfeast.init.SFItems;

public class Proxy {
	@SuppressWarnings("static-method")
	public void preInit() {
		SFBlocks.init();
		SFItems.init();
	}

	@SuppressWarnings("static-method")
	public void init() {
		return;
	}

	@SuppressWarnings("static-method")
	public void postInit() {
		return;
	}
}
