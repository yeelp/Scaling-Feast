package yeelp.scalingfeast.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;
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
	
	public int getMaxLevel()
	{
		return 3;
	}
}
