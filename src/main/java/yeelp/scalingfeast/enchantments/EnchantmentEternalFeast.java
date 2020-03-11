package yeelp.scalingfeast.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;
/**
 * The Eternal Feast enchantment.
 * @author Yeelp
 *
 */
public class EnchantmentEternalFeast extends Enchantment
{
	/**
	 * Create a new Eternal Feast enchantment. This enchantment restores hunger every time the user kills an entity.
	 */
	public EnchantmentEternalFeast() 
	{
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		this.setRegistryName("eternalfeast");
		this.setName(ModConsts.MOD_ID+".eternalfeast");
	}

	public int getMaxLevel()
	{
		return 3;
	}
}
