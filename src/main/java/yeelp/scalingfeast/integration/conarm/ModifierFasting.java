package yeelp.scalingfeast.integration.conarm;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import slimeknights.tconstruct.library.modifiers.IToolMod;
import yeelp.scalingfeast.init.SFEnchantments;

public final class ModifierFasting extends AbstractSFArmorModifier {

	public ModifierFasting() {
		super("fasting", 0xfcf3eb, SFEnchantments.fasting, 2);
	}

	@Override
	public Set<IToolMod> getIncompatibilities() {
		return ImmutableSet.of(SFConarmIntegration.gluttony);
	}

}
