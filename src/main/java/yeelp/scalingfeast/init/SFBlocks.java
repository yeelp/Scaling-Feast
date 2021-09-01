package yeelp.scalingfeast.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.blocks.ExhaustingBlock;
import yeelp.scalingfeast.blocks.ExhaustingOreBlock;
import yeelp.scalingfeast.blocks.HeartyFeastBlock;

public class SFBlocks {
	public static Block heartyfeast;
	public static ExhaustingOreBlock exhaustingOre;
	public static ExhaustingBlock exhaustingBlock;

	public static void init() {
		heartyfeast = new HeartyFeastBlock();
		exhaustingOre = new ExhaustingOreBlock();
		exhaustingBlock = new ExhaustingBlock();
		ForgeRegistries.BLOCKS.registerAll(heartyfeast, exhaustingOre, exhaustingBlock);
	}
}
