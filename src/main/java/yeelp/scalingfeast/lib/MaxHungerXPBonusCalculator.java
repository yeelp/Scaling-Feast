package yeelp.scalingfeast.lib;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;

public class MaxHungerXPBonusCalculator extends AbstractXPBonusCalculator {

	private static MaxHungerXPBonusCalculator instance;
	
	public MaxHungerXPBonusCalculator() {
		super(SFBuiltInModifiers.MaxHungerModifiers.XP);
	}

	@Override
	protected IAttributeInstance getAttributeInstance(EntityPlayer player) {
		return ScalingFeastAPI.accessor.getSFFoodStats(player).getMaxHungerAttribute();
	}
	
	public static XPBonusCalculator getInstance() {
		return instance == null ? instance = new MaxHungerXPBonusCalculator() : instance;
	}
}
