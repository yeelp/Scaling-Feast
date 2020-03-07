package yeelp.scalingfeast.enchantments;
import yeelp.scalingfeast.ModConsts;
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
	
	public int getMaxLevel()
	{
		return 3;
	}
}
