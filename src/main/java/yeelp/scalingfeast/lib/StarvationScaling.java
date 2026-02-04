package yeelp.scalingfeast.lib;

import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.features.SFConfigStarvation;

public enum StarvationScaling {
	NONE {
		@Override
		public float compute(int x) {
			return 0;
		}
	},
	CONSTANT {
		@Override
		public float compute(int x) {
			return d();
		}
	},
	POLYNOMIAL {
		@Override
		public float compute(int x) {
			double base = x + b();
			if(base < 0 && Math.ceil(c()) != c()) {
				return 0;
			}
			if(base == 0 && c() < 0) {
				return 0;
			}
			if(base == 0 && c() == 0) {
				//0^0 here is 1
				return a() + d();
			}
			return (float) (a()*Math.pow(base, c()) + d());
		}
	},
	LOGARITHMIC {
		@Override
		public float compute(int x) {
			if(b() <= 0 || b() == 1 || x + c() <= 0) {
				return 0;
			}
			double lnx = Math.log(x + c());  				
			double lnb = Math.log(b());
			return (float) (a()*(lnx/lnb)+d());
		}
	},
	EXPONENTIAL {
		@Override
		public float compute(int x) {
			if(b() == 0) {
				return 0;
			}
			return (float) (a()*Math.pow(b(), c()*x + d()));
		}
	};
	
	public abstract float compute(int x);
	
	protected static float a() {
		return getConfigRoot().a;
	}
	
	protected static float b() {
		return getConfigRoot().b;
	}
	
	protected static float c() {
		return getConfigRoot().c;
	}
	
	protected static float d() {
		return getConfigRoot().d;
	}
	
	private static SFConfigStarvation.CounterCategory getConfigRoot() {
		return ModConfig.features.starve.counter;
	}
}
