package yeelp.scalingfeast.lib;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier;

public abstract class AbstractXPBonusCalculator implements XPBonusCalculator {

	private XPBonusType type;
	private List<XPMilestone> milestones;
	private final BuiltInModifier modifier;

	protected AbstractXPBonusCalculator(BuiltInModifier modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public void applyXPBonus(EntityPlayer player) {
		this.calcNewAttributeModifier(player).ifPresent((mod) -> {
			IAttributeInstance instance = this.getAttributeInstance(player);
			if(instance.hasModifier(mod)) {
				instance.removeModifier(mod);				
			}
			instance.applyModifier(mod);
		});
	}

	@Override
	public void setMilestoneList(List<XPMilestone> lst) {
		this.milestones = lst;
	}
	
	protected final List<XPMilestone> getMilestones() {
		return this.milestones;
	}

	@Override
	public void setFunction(XPBonusType type) {
		this.type = type;
	}
	
	protected final XPBonusType getFunction() {
		return this.type;
	}
	
	protected abstract IAttributeInstance getAttributeInstance(EntityPlayer player);
	
	private Optional<AttributeModifier> calcNewAttributeModifier(EntityPlayer player) {
		double currMod = this.modifier.getModifierValueForPlayer(player);
		OptionalInt xp = this.type.getCurrentXPValue(player);
		if(xp.isPresent()) {
			double newMod = this.milestones.stream().filter((m) -> m.reachedTarget(xp.getAsInt())).mapToDouble(XPMilestone::getReward).sum();
			if(newMod != currMod) {
				return Optional.of(this.modifier.createModifier(newMod));
			}
		}
		return Optional.empty();
	}

}
