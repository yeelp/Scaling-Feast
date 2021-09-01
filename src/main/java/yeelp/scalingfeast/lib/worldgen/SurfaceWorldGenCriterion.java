package yeelp.scalingfeast.lib.worldgen;

import java.util.Arrays;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.scalingfeast.config.ModConfig;

public enum SurfaceWorldGenCriterion {
	EXTREME_CONDITIONS {
		@Override
		public boolean generateHere(BlockPos pos, World world) {
			float temp = world.provider.getBiomeProvider().getBiome(pos).getDefaultTemperature();
			return temp <= 0 || temp >= 1; 
		}
	},
	EVERYWHERE {
		@Override
		public boolean generateHere(BlockPos pos, World world) {
			return true;
		}
	},
	SPECIFIC {
		@Override
		public boolean generateHere(BlockPos pos, World world) {
			return Arrays.stream(ModConfig.general.worldGen.specificValidBiomes).anyMatch(world.provider.getBiomeProvider().getBiome(pos).getRegistryName().toString()::equals);
		}
	};
	
	public abstract boolean generateHere(BlockPos pos, World world);
}
