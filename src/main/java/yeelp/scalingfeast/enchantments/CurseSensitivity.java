package yeelp.scalingfeast.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;

public class CurseSensitivity extends Enchantment
{
	public CurseSensitivity()
	{
		super(Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST});
		this.setRegistryName("sensitivitycurse");
		this.setName(ModConsts.MOD_ID+".sensitivitycurse");
	}
	
	@Override
	public boolean isTreasureEnchantment()
	{
		return true;
	}
	
	@Override
	public boolean isCurse()
	{
		return true;
	}
	
	@Override
	public int getMinEnchantability(int level)
	{
		return 10;
	}
	
	@Override
	public int getMaxEnchantability(int level)
	{
		return 50;
	}
}
