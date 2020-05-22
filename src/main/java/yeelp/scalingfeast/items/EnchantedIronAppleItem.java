package yeelp.scalingfeast.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;

/**
 * The Enchanted Iron Apple which gives the Iron Stomach effect and a Metabolism effect when eaten.
 * @author Yeelp
 *
 */
public class EnchantedIronAppleItem extends ItemFood 
{
	public EnchantedIronAppleItem()
	{
		super(0, 0.0f, false);
		this.setAlwaysEdible();
		this.setRegistryName("enchantedironapple");
		this.setUnlocalizedName(ModConsts.MOD_ID+".enchantedironapple");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		int gluttonyLevel = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.gluttony, entityLiving);
		float mod = (1 + 0.5f*gluttonyLevel);
		entityLiving.heal(4.0f*mod);
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.metabolism, 20*20, 3));
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.ironstomach, 6*60*20, 1));
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
