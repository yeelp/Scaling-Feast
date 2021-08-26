package yeelp.scalingfeast.api;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

/**
 * An interface to alter the food efficiency attribute. It assumes the player is already acquired.
 * @author Yeelp
 *
 */
public interface IFoodEfficiencyChanger {

	/**
	 * Get the food efficiency attribute for the player.
	 * @return
	 */
	IAttributeInstance getFoodEfficiencyModifierAttribute();
	
	/**
	 * Apply a modifier to the food efficiency attribute
	 * @param mod the built in modifier to apply
	 */
	default void applyFoodEfficiencyModifier(AttributeModifier modifier) {
		IAttributeInstance instance = this.getFoodEfficiencyModifierAttribute();
		if(instance.hasModifier(modifier)) {
			instance.removeModifier(modifier);
		}
		instance.applyModifier(modifier);
	}
	
	/**
	 * Get a built in modifier for the food efficiency attribute
	 * @param type the type to get.
	 * @return An Optional describing the result; the AttributeModifier if it exists, or {@link Optional#empty()}
	 */
	default Optional<AttributeModifier> getFoodEfficiencyModifier(UUID type) {
		return Optional.ofNullable(this.getFoodEfficiencyModifierAttribute().getModifier(type));
	}
}
