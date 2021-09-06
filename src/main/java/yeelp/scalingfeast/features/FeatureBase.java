package yeelp.scalingfeast.features;

import javax.annotation.Nonnull;

import yeelp.scalingfeast.handlers.Handler;

public abstract class FeatureBase<Config> {

	@Nonnull
	public abstract Handler getFeatureHandler();
	
	protected abstract Config getConfig();

}
