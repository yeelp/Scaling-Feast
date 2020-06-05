package yeelp.scalingfeast.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.scalingfeast.ModConsts;

public class CurseLaziness extends Enchantment
{
	public CurseLaziness()
	{
		super(Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST});
		this.setRegistryName("lazinesscurse");
		this.setName(ModConsts.MOD_ID + ".lazinesscurse");
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
