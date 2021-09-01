package yeelp.scalingfeast.lib.xpcalculators;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

public class MaxHungerXPBonusCalculator extends AbstractXPBonusCalculator {

	private static MaxHungerXPBonusCalculator instance;
	
	public MaxHungerXPBonusCalculator() {
		super(SFBuiltInModifiers.MaxHungerModifiers.XP, ModConfig.features.xpBonuses.maxHungerXPBonus);
	}

	@Override
	protected IAttributeInstance getAttributeInstance(EntityPlayer player) {
		return ScalingFeastAPI.accessor.getSFFoodStats(player).getMaxHungerAttribute();
	}
	
	public static XPBonusCalculator getInstance() {
		return instance == null ? instance = new MaxHungerXPBonusCalculator() : instance;
	}
}
