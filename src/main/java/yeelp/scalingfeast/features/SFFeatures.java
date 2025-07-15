package yeelp.scalingfeast.features;

import com.google.common.collect.ImmutableList;

public final class SFFeatures {
	private static final Iterable<FeatureBase<?>> FEATURES = ImmutableList.of(new SFBloatedOverflow(),
														 					  new SFDeathFeatures(),
														 					  new SFHealthRegen(),
														 					  new SFHungerDamage(),
														 					  new SFXPBonuses(),
														 					  new SFStarvationFeatures(),
														 					  new SFExhaustionScaling());
	public static final void init() {
		FEATURES.forEach((f) -> f.getFeatureHandler().register());
	}
}
