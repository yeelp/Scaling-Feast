package yeelp.scalingfeast.integration.tic.conarm;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import slimeknights.tconstruct.library.modifiers.IToolMod;
import yeelp.scalingfeast.init.SFEnchantments;

public final class ModifierGluttony extends AbstractSFArmorModifier {

	public ModifierGluttony() {
		super(0xffaa00, SFEnchantments.gluttony, 2);
	}

	@Override
	public Set<IToolMod> getIncompatibilities() {
		return ImmutableSet.of(SFConarmIntegration.FASTING);
	}
}
