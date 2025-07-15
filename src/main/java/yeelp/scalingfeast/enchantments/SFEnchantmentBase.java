package yeelp.scalingfeast.enchantments;

import java.util.Optional;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.Handler;

public abstract class SFEnchantmentBase extends Enchantment {
	
	private final String simpleName;
	
	public SFEnchantmentBase(String name, Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.simpleName = name;
		this.setRegistryName(ModConsts.MOD_ID, this.simpleName);
		this.setName(String.format("%s.%s", ModConsts.MOD_ID, this.simpleName));
	}
	
	/**
	 * Get the Handler for this enchantment if it has one.
	 * @return The handler for this enchantment, or an empty Optional if it doesn't.
	 */
	protected abstract Optional<Handler> getEnchantmentHandler();
	
	/**
	 * Callback for when enchantment is registered to register the handler
	 */
	public final void onRegister() {
		this.getEnchantmentHandler().ifPresent(Handler::register);
	}
	
	/**
	 * Should the enchantment handler be registered anyway?
	 * @return {@code false} by default
	 */
	public abstract boolean shouldRegisterHandlerAnyway();
	
	/**
	 * Is this enchantment enabled?
	 * @return True if enabled, false if not
	 */
	public abstract boolean enabled();
	
	public String getSimpleName() {
		return this.simpleName;
	}
}
