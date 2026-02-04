package yeelp.scalingfeast.integration.tic.conarm;

import c4.conarm.lib.book.ArmoryBook;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import c4.conarm.lib.modifiers.ArmorModifierTrait;
import c4.conarm.lib.traits.AbstractArmorTrait;
import c4.conarm.lib.utils.RecipeMatchHolder;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.library.TinkerRegistry;
import yeelp.scalingfeast.init.SFItems;
import yeelp.scalingfeast.integration.IIntegratable;
import yeelp.scalingfeast.integration.tic.tinkers.SFTinkerIntegration;

public final class SFConarmIntegration implements IIntegratable {

	public static final ArmorModifierTrait GLUTTONY = new ModifierGluttony(), FASTING = new ModifierFasting();
	public static final AbstractArmorTrait NOURISHING = new TraitNourishing(), TORPORIC = new TraitTorporic();
	
	@Override
	public boolean preIntegrate(FMLPreInitializationEvent evt) {
		if(evt.getSide() == Side.CLIENT) {
			ArmoryBook.INSTANCE.addRepository(new FileRepository("scalingfeast:conarm/book"));
			ArmoryBook.INSTANCE.addTransformer(new ScalingFeastBookTransformer());
		}
		TinkerRegistry.addMaterialStats(SFTinkerIntegration.exhaustium, new CoreMaterialStats(20, 10), new TrimMaterialStats(7), new PlatesMaterialStats(1.35f, 3, 3));
		ArmorMaterials.addArmorTrait(SFTinkerIntegration.exhaustium, TORPORIC, NOURISHING);
		return true;
	}

	@Override
	public boolean integrate(FMLInitializationEvent evt) {
		RecipeMatchHolder.addItem(GLUTTONY, SFItems.heartyfeastitem, 1, 1);
		RecipeMatchHolder.addItem(FASTING, SFItems.exhaustingapple, 1, 1);
		return true;
	}

	@Override
	public boolean postIntegrate(FMLPostInitializationEvent evt) {
		return true;
	}
}
