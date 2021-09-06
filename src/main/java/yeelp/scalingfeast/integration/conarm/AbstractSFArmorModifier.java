package yeelp.scalingfeast.integration.conarm;

import java.util.Set;

import c4.conarm.lib.modifiers.ArmorModifierTrait;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import yeelp.scalingfeast.enchantments.SFEnchantmentBase;

public abstract class AbstractSFArmorModifier extends ArmorModifierTrait {

	private final int level;
	private final Enchantment enchant;
	protected AbstractSFArmorModifier(String identifier, int color, SFEnchantmentBase enchantment, int enchantmentLevel) {
		super(identifier, color);
		this.enchant = enchantment;
		this.level = enchantmentLevel;
	}
	
	@Override
	public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
		for(int i = 0; i < this.level; ToolBuilder.addEnchantment(rootCompound, this.enchant), i++);
	}
	
	@Override
	public boolean canApplyCustom(ItemStack stack) {
		return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST && super.canApplyCustom(stack);
	}

	@Override
	public boolean canApplyTogether(IToolMod otherModifier) {
		return !this.getIncompatibilities().contains(otherModifier);
	}
	
	public abstract Set<IToolMod> getIncompatibilities();
}
