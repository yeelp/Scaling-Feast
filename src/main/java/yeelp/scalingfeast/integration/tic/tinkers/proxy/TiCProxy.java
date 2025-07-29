package yeelp.scalingfeast.integration.tic.tinkers.proxy;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.integration.tic.tinkers.SFTinkerIntegration;
import yeelp.scalingfeast.proxy.AbstractProxy;

public class TiCProxy extends AbstractProxy {

	@Override
	public void preInit() {
		ForgeRegistries.BLOCKS.register(SFTinkerIntegration.moltenBlock);
		ForgeRegistries.ITEMS.register(new ItemBlock(SFTinkerIntegration.moltenBlock).setRegistryName(SFTinkerIntegration.moltenBlock.getRegistryName()));
	}

	@Override
	public void init() {
		//no-op
	}

	@Override
	public void postInit() {
		//no-op
	}

}
