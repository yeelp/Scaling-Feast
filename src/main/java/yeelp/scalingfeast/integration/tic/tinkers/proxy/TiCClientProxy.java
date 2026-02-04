package yeelp.scalingfeast.integration.tic.tinkers.proxy;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.integration.tic.tinkers.SFTinkerIntegration;

import javax.annotation.ParametersAreNonnullByDefault;

public class TiCClientProxy extends TiCProxy {

	@Override
	public void preInit() {
		super.preInit();
		Block fluidBlock = SFTinkerIntegration.moltenExhaustion.getBlock();
		Item item = Item.getItemFromBlock(fluidBlock);
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, StateMapper.INSTANCE);
		ModelLoader.setCustomStateMapper(fluidBlock, StateMapper.INSTANCE);
	}

	@MethodsReturnNonnullByDefault
	@ParametersAreNonnullByDefault
	private static class StateMapper extends StateMapperBase implements ItemMeshDefinition {
		static final StateMapper INSTANCE = new StateMapper();
		private static final ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(ModConsts.MOD_ID, "fluid_block"), SFTinkerIntegration.moltenExhaustion.getName());
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return location;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return location;
		}
	}
}
