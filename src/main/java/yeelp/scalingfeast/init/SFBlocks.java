package yeelp.scalingfeast.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.blocks.HeartyFeastBlock;

public class SFBlocks {
	public static Block heartyfeast;

	public static void init() {
		heartyfeast = new HeartyFeastBlock();
		ForgeRegistries.BLOCKS.register(heartyfeast);
	}
}
