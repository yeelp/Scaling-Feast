package yeelp.scalingfeast.lib.xpcalculators;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

public class FoodEfficiencyXPBonusCalculator extends AbstractXPBonusCalculator {
	
	private static FoodEfficiencyXPBonusCalculator instance;
	
	public FoodEfficiencyXPBonusCalculator() {
		super(SFBuiltInModifiers.FoodEfficiencyModifiers.XP, ModConfig.features.xpBonuses.efficiencyXPBonus);
	}

	@Override
	protected IAttributeInstance getAttributeInstance(EntityPlayer player) {
		return ScalingFeastAPI.accessor.getSFFoodStats(player).getFoodEfficiencyModifierAttribute();
	}

	public static XPBonusCalculator getInstance() {
		return instance == null ? instance = new FoodEfficiencyXPBonusCalculator() : instance;
	}
}
