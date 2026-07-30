package yeelp.scalingfeast.integration.tic.conarm;

import com.google.common.collect.ImmutableSet;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import yeelp.scalingfeast.init.SFEnchantments;

import java.util.Set;

public final class ModifierFasting extends AbstractSFArmorModifier {

	public ModifierFasting() {
		super(0xfcf3eb, SFEnchantments.fasting, 2);
	}

	@Override
	public Set<IToolMod> getIncompatibilities() {
		return ImmutableSet.of(SFConarmIntegration.GLUTTONY);
	}

}
