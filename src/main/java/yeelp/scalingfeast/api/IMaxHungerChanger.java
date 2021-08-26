package yeelp.scalingfeast.api;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

/**
 * An interface for altering max hunger.
 * 
 * This interface assumes the player involved is already acquired.
 * @author Yeelp
 *
 */
public interface IMaxHungerChanger {
	
	/**
	 * Get the max hunger attribute
	 * @return
	 */
	IAttributeInstance getMaxHungerAttribute();
	
	/**
	 * Apply a modifier to max hunger
	 * @param mod the modifier to apply
	 */
	default void applyMaxHungerModifier(AttributeModifier modifier) {
		IAttributeInstance instance = this.getMaxHungerAttribute();
		if(instance.hasModifier(modifier)) {
			instance.removeModifier(modifier);
		}
		instance.applyModifier(modifier);
	}
	
	/**
	 * Get an AttributeModifier that was applied.
	 * @param type The type of built in max hunger modifier to get.
	 * @return An Optional describing the result; the AttributeModifier if it exists, or {@link Optional#empty()} if it doesn't.
	 */
	default Optional<AttributeModifier> getMaxHungerModifier(UUID type) {
		return Optional.ofNullable(this.getMaxHungerAttribute().getModifier(type));
	}
	
	/**
	 * Get the max hunger value. Just syntactic sugar for {@code getMaxHungerAttribute().getAttributeValue()}
	 * @return The max hunger represented by this attribute.
	 */
	@Deprecated
	default int getMaxHunger() {
		return (int) this.getMaxHungerAttribute().getAttributeValue();
	}
}
