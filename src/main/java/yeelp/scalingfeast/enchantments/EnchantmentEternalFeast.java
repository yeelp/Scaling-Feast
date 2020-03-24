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

	public int getMinEnchantability(int level)
	{
		return 3*level + 13;
	}
	
	public int getMaxEnchantability(int level)
	{
		return 3*level + 24;
	}
	
	public boolean isTreasureEnchantment()
	{
		return true;
	}
	
	public int getMaxLevel()
	{
		return 3;
	}
}
