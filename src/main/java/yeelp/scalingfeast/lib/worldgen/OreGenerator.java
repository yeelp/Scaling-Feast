package yeelp.scalingfeast.lib.worldgen;

import java.util.Iterator;
import java.util.Random;

import com.google.common.collect.Iterators;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import yeelp.scalingfeast.blocks.ExhaustingOreBlock;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.init.SFBlocks;

public final class OreGenerator implements IWorldGenerator {
	
	private static final class PosIterator implements Iterator<BlockPos> {

		private final int xMin, zMin, yMin, yMax;
		private final Random rand;
		private int amount;
		PosIterator(int rate, int chunkX, int chunkZ, int yMin, int yMax, Random rand) {
			this.amount = rate;
			this.xMin = chunkX * 16;
			this.zMin = chunkZ * 16;
			this.yMin = yMin;
			this.yMax = yMax;
			this.rand = rand;
		}
		@Override
		public boolean hasNext() {
			return this.amount > 0;
		}

		@Override
		public BlockPos next() {
			this.amount--;
			return new BlockPos(this.xMin + this.rand.nextInt(16), this.yMin + this.rand.nextInt(this.yMax - this.yMin), this.zMin + this.rand.nextInt(16));
		}
	}

	private static OreGenerator instance;

	private final WorldGenMinable exhaustingStone, exhaustingNetherrack;

	@SuppressWarnings("deprecation")
	private OreGenerator() {
		this.exhaustingStone = new WorldGenMinable(SFBlocks.exhaustingOre.getStateFromMeta(ExhaustingOreBlock.RockType.STONE.ordinal()), 5, BlockMatcher.forBlock(Blocks.STONE));
		this.exhaustingNetherrack = new WorldGenMinable(SFBlocks.exhaustingOre.getStateFromMeta(ExhaustingOreBlock.RockType.NETHERRACK.ordinal()), 15, BlockMatcher.forBlock(Blocks.NETHERRACK));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider instanceof WorldProviderHell) {
			new PosIterator(30, chunkX, chunkZ, 0, 128, random).forEachRemaining((p) -> this.exhaustingNetherrack.generate(world, random, p));
		}
		else if(world.provider instanceof WorldProviderSurface) {
			Iterators.filter(new PosIterator(10, chunkX, chunkZ, 0, 43, random), (p) -> ModConfig.general.worldGen.surfaceWorldRestrictions.generateHere(p, world)).forEachRemaining((p) -> this.exhaustingStone.generate(world, random, p));
		}
	}

	public static OreGenerator getInstance() {
		return instance == null ? instance = new OreGenerator() : instance;
	}

}
