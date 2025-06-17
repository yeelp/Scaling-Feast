package yeelp.scalingfeast.integration.conarm;

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
import yeelp.scalingfeast.integration.module.IIntegratable;
import yeelp.scalingfeast.integration.tinkers.SFTinkerIntegration;

public final class SFConarmIntegration implements IIntegratable {

	public static final ArmorModifierTrait gluttony = new ModifierGluttony(), fasting = new ModifierFasting();
	public static final AbstractArmorTrait nourishing = new TraitNourishing(), torporic = new TraitTorporic();
	
	@Override
	public boolean preIntegrate(FMLPreInitializationEvent evt) {
		if(evt.getSide() == Side.CLIENT) {
			ArmoryBook.INSTANCE.addRepository(new FileRepository("scalingfeast:conarm/book"));
			ArmoryBook.INSTANCE.addTransformer(new ScalingFeastBookTransformer());
		}
		return true;
	}

	@Override
	public boolean integrate(FMLInitializationEvent evt) {
		TinkerRegistry.addMaterialStats(SFTinkerIntegration.exhaustium, new CoreMaterialStats(20, 10), new TrimMaterialStats(7), new PlatesMaterialStats(1.35f, 3, 3));
		ArmorMaterials.addArmorTrait(SFTinkerIntegration.exhaustium, torporic, nourishing);
		RecipeMatchHolder.addItem(gluttony, SFItems.heartyfeastitem, 1, 1);
		RecipeMatchHolder.addItem(fasting, SFItems.exhaustingapple, 1, 1);
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
}
