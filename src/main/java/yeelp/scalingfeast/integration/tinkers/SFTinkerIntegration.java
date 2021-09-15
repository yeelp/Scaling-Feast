package yeelp.scalingfeast.integration.tinkers;

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
import yeelp.scalingfeast.integration.module.IIntegratable;
import yeelp.scalingfeast.integration.tinkers.proxy.TiCClientProxy;
import yeelp.scalingfeast.integration.tinkers.proxy.TiCProxy;

public class SFTinkerIntegration implements IIntegratable {

	public static final AbstractTrait exhausting1 = new TraitExhausting(1), exhausting2 = new TraitExhausting(2);
	public static final AbstractTrait feasting = new TraitFeasting();

	public static Material exhaustium = new Material("exhaustium", 0xC69174);
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
		TinkerRegistry.integrate(new MaterialIntegration(exhaustium, moltenExhaustion, "Exhausting")).toolforge().preInit();
		proxy.preInit();
		TinkerRegistry.addMaterialStats(exhaustium, new HeadMaterialStats(350, 9, 6.5f, HarvestLevels.DIAMOND), new HandleMaterialStats(1.35f, 3), new ExtraMaterialStats(380));
		TinkerRegistry.addMaterialStats(exhaustium, new BowMaterialStats(3.14f, 2.71f, 3));
		return true;
	}

	@Override
	public boolean integrate(FMLInitializationEvent evt) {
		proxy.init();
		exhaustium.addTrait(exhausting2, MaterialTypes.HEAD);
		exhaustium.addTrait(feasting, MaterialTypes.HEAD);
		exhaustium.addTrait(exhausting1);
		exhaustium.addCommonItems("Exhausting");
		exhaustium.setRepresentativeItem(SFItems.exhaustingIngot);
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
