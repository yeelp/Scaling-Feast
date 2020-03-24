package yeelp.scalingfeast.enchantments;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.init.SFEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * The Fasting Enchantment. This enchantment reduces exhaustion by 10% per level.
 * @author Yeelp
 *
 */
public class EnchantmentFasting extends Enchantment {

	/**
	 * Create a new Fasting Enchantment
	 */
	public EnchantmentFasting()
	{
		super(Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST});
		this.setRegistryName("fasting");
		this.setName(ModConsts.MOD_ID+".fasting");
	}
	
	public int getMinEnchantability(int level)
	{
		return 3*level + 14;
	}
	
	public int getMaxEnchantability(int level)
	{
		return -1*level + 40;
	}
	
	public boolean canApplyTogether(Enchantment ench)
	{
		return super.canApplyTogether(ench) && ench != SFEnchantments.gluttony;
	}
	
	public int getMaxLevel()
	{
		return 3;
	}
}
