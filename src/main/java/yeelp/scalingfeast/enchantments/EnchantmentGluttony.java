package yeelp.scalingfeast.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.init.SFEnchantments;
/**
 * The Gluttony Enchantment. This enchantment increases food values by 50% per level, rounded down.
 * @author Yeelp
 *
 */
public class EnchantmentGluttony extends Enchantment
{
	/**
	 * Create a new Gluttony Enchantment
	 */
	public EnchantmentGluttony()
	{
		super(Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST});
		this.setRegistryName("gluttony");
		this.setName(ModConsts.MOD_ID+".gluttony");
	}
	
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 6*enchantmentLevel + 20;
	}
	
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return 10*enchantmentLevel + 30;
	}
	
	public boolean isTreasureEnchantment()
	{
		return true;
	}
	
	public boolean canApplyTogether(Enchantment ench)
	{
		return super.canApplyTogether(ench) && ench != SFEnchantments.fasting;
	}
	
	public int getMaxLevel()
	{
		return 3;
	}
}
