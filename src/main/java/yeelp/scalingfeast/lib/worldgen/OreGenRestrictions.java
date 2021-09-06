package yeelp.scalingfeast.lib.worldgen;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;

public enum OreGenRestrictions {
	SURFACE_ONLY {
		@Override
		public boolean canGenerateHere(WorldProvider provider) {
			return provider instanceof WorldProviderSurface;
		}
	},
	NETHER_ONLY {
		@Override
		public boolean canGenerateHere(WorldProvider provider) {
			return provider instanceof WorldProviderHell;
		}
	},
	SURFACE_AND_NETHER {
		@Override
		public boolean canGenerateHere(WorldProvider provider) {
			return SURFACE_ONLY.canGenerateHere(provider) || NETHER_ONLY.canGenerateHere(provider);
		}
	},
	NOWHERE {
		@Override
		public boolean canGenerateHere(WorldProvider provider) {
			return false;
		}
	};
	
	public abstract boolean canGenerateHere(WorldProvider provider);
}
