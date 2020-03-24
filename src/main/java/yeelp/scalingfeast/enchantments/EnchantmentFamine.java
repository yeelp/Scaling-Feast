package yeelp.scalingfeast.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;
/**
 * The Famine Enchantment. Entities struck with this enchantment suffer a hunger and weakness effect, depending on the level.
 * @author Yeelp
 *
 */
public class EnchantmentFamine extends Enchantment {
	/**
	 * Create a new Fasting Enchantment
	 */
	public EnchantmentFamine()
	{
		super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		this.setRegistryName("famine");
		this.setName(ModConsts.MOD_ID+".famine");
	}
	
	public int getMinEnchantability(int level)
	{
		return 7*level + 11;
	}
	
	public int getMaxEnchantability(int level)
	{
		return 3*level + 40;
	}
	
	public int getMaxLevel()
	{
		return 5;
	}
}
