package yeelp.scalingfeast.integration.tic.conarm;

import com.google.common.collect.ImmutableSet;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import yeelp.scalingfeast.init.SFEnchantments;

import java.util.Set;

public final class ModifierGluttony extends AbstractSFArmorModifier {

	public ModifierGluttony() {
		super(0xffaa00, SFEnchantments.gluttony, 2);
	}

	@Override
	public Set<IToolMod> getIncompatibilities() {
		return ImmutableSet.of(SFConarmIntegration.FASTING);
	}
}
