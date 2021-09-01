package yeelp.scalingfeast.proxy;

import yeelp.scalingfeast.handlers.HUDOverlayHandler;
import yeelp.scalingfeast.init.SFItems;

public class ClientProxy extends Proxy {
	@Override
	public void preInit() {
		super.preInit();
		SFItems.registerRenders();
	}

	@Override
	public void init() {
		super.init();
		new HUDOverlayHandler().register();
	}

	@Override
	public void postInit() {
		super.postInit();
	}
}
