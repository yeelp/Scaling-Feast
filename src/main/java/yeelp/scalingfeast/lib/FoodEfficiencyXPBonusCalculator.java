package yeelp.scalingfeast.lib;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;

public class FoodEfficiencyXPBonusCalculator extends AbstractXPBonusCalculator {
	
	private static FoodEfficiencyXPBonusCalculator instance;
	
	public FoodEfficiencyXPBonusCalculator() {
		super(SFBuiltInModifiers.FoodEfficiencyModifiers.XP);
	}

	@Override
	protected IAttributeInstance getAttributeInstance(EntityPlayer player) {
		return ScalingFeastAPI.accessor.getSFFoodStats(player).getFoodEfficiencyModifierAttribute();
	}

	public static XPBonusCalculator getInstance() {
		return instance == null ? instance = new FoodEfficiencyXPBonusCalculator() : instance;
	}
}
