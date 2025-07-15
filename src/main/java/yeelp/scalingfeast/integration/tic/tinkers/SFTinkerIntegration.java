package yeelp.scalingfeast.integration.tic.tinkers;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
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
import yeelp.scalingfeast.init.SFItems;
import yeelp.scalingfeast.init.SFOreDict;
import yeelp.scalingfeast.integration.module.IIntegratable;
import yeelp.scalingfeast.integration.tic.TiCConsts;
import yeelp.scalingfeast.integration.tic.tinkers.proxy.TiCClientProxy;
import yeelp.scalingfeast.integration.tic.tinkers.proxy.TiCProxy;

public class SFTinkerIntegration implements IIntegratable {

	public static final AbstractTrait EXHAUSTING_1 = new TraitExhausting(1), EXHAUSTING_2 = new TraitExhausting(2);
	public static final AbstractTrait FEASTING = new TraitFeasting();

	public static Material exhaustium = new Material("exhaustium", TiCConsts.EXHAUSTING_COLOUR);
	public static MoltenExhaustium moltenExhaustion = new MoltenExhaustium();
	public static BlockMolten moltenBlock;

	private static TiCProxy proxy = getSidedProxy(FMLCommonHandler.instance().getSide());

	static {
		FluidRegistry.registerFluid(moltenExhaustion);
		FluidRegistry.addBucketForFluid(moltenExhaustion);
		moltenBlock = moltenExhaustion.getBlockMolten();
	}

	@Override
	public boolean preIntegrate(FMLPreInitializationEvent evt) {
		TinkerRegistry.addMaterialStats(exhaustium, new HeadMaterialStats(350, 9, 6.5f, HarvestLevels.DIAMOND), new HandleMaterialStats(1.35f, 3), new ExtraMaterialStats(380));
		TinkerRegistry.addMaterialStats(exhaustium, new BowMaterialStats(3.14f, 2.71f, 3));
		exhaustium.addCommonItems(SFOreDict.SUFFIX);
		exhaustium.addTrait(EXHAUSTING_2, MaterialTypes.HEAD);
		exhaustium.addTrait(FEASTING, MaterialTypes.HEAD);
		exhaustium.addTrait(EXHAUSTING_1);
		exhaustium.setRepresentativeItem(SFItems.exhaustingIngot);
		TinkerRegistry.integrate(new MaterialIntegration(exhaustium, moltenExhaustion, SFOreDict.SUFFIX)).toolforge().preInit();
		proxy.preInit();
		return true;
	}

	@Override
	public boolean integrate(FMLInitializationEvent evt) {
		proxy.init();
		return true;
	}

	@Override
	public boolean postIntegrate(FMLPostInitializationEvent evt) {
		proxy.postInit();
		return true;
	}

	@Override
	public boolean enabled() {
		return true;
	}

	private static TiCProxy getSidedProxy(Side side) {
		switch(side) {
			case CLIENT:
				return new TiCClientProxy();
			default:
				return new TiCProxy();
		}
	}
}
