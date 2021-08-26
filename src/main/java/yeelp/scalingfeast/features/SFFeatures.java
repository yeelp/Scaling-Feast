package yeelp.scalingfeast.features;

import com.google.common.collect.ImmutableList;

public final class SFFeatures {
	private static final Iterable<FeatureBase<?>> features = ImmutableList.of(new SFBloatedOverflow(),
														 					  new SFDeathFeatures(),
														 					  new SFHealthRegen(),
														 					  new SFHungerDamage(),
														 					  new SFXPBonuses(),
														 					  new SFStarvationFeatures());
	public static final void init() {
		features.forEach((f) -> f.getFeatureHandler().register());
	}
}
