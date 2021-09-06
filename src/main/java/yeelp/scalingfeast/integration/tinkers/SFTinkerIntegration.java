package yeelp.scalingfeast.integration.tinkers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.smeltery.block.BlockMolten;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.init.SFItems;
import yeelp.scalingfeast.integration.module.IIntegratable;

public class SFTinkerIntegration implements IIntegratable {

	public static final AbstractTrait exhausting1 = new TraitExhausting(1), exhausting2 = new TraitExhausting(2);
	public static final AbstractTrait feasting = new TraitFeasting();

	public static Material exhaustium = new Material("exhaustium", 0xC69174);
	public static MoltenExhaustium moltenExhaustion = new MoltenExhaustium();
	public static BlockMolten moltenBlock;
	private static MaterialIntegration integration;
	
	static {
		FluidRegistry.registerFluid(moltenExhaustion);
		FluidRegistry.addBucketForFluid(moltenExhaustion);
	}

	@Override
	public boolean preIntegrate(FMLPreInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(this);
		integration = TinkerRegistry.integrate(new MaterialIntegration(exhaustium, moltenExhaustion, "Exhausting")).toolforge();
		integration.preInit();
		moltenBlock = moltenExhaustion.getBlockMolten();
		ForgeRegistries.BLOCKS.register(moltenBlock);
		ForgeRegistries.ITEMS.register(new ItemBlock(moltenBlock).setRegistryName(moltenBlock.getRegistryName()));

		Block fluidBlock = moltenExhaustion.getBlock();
		Item item = Item.getItemFromBlock(fluidBlock);
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, StateMapper.INSTANCE);
		ModelLoader.setCustomStateMapper(fluidBlock, StateMapper.INSTANCE);
		TinkerRegistry.addMaterialStats(exhaustium, new HeadMaterialStats(350, 9, 6.5f, HarvestLevels.DIAMOND), new HandleMaterialStats(1.35f, 3), new ExtraMaterialStats(380));
		TinkerRegistry.addMaterialStats(exhaustium, new BowMaterialStats(3.14f, 2.71f, 3));
		return true;
	}

	@Override
	public boolean integrate(FMLInitializationEvent evt) {
		exhaustium.addTrait(exhausting2, MaterialTypes.HEAD);
		exhaustium.addTrait(feasting, MaterialTypes.HEAD);
		exhaustium.addTrait(exhausting1);
		exhaustium.addCommonItems("Exhausting");
		exhaustium.setRepresentativeItem(SFItems.exhaustingIngot);
		return true;
	}

	@Override
	public boolean postIntegrate(FMLPostInitializationEvent evt) {
		return true;
	}

	@Override
	public boolean enabled() {
		return true;
	}
	
	private static class StateMapper extends StateMapperBase implements ItemMeshDefinition {
		static final StateMapper INSTANCE = new StateMapper();
		private static final ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(ModConsts.MOD_ID, "fluid_block"), moltenExhaustion.getName());
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
