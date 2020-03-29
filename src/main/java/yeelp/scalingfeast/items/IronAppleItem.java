package yeelp.scalingfeast.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.init.SFPotion;

/**
 * Iron Apple. Eating it restores some health and regens hunger over time.
 * @author Yeelp
 *
 */
public class IronAppleItem extends ItemFood 
{
	public IronAppleItem()
	{
		super(0, 0, false);
		this.setAlwaysEdible();
		this.setRegistryName("ironapple");
		this.setUnlocalizedName(ModConsts.MOD_ID+".ironapple");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		entityLiving.heal(4.0f);
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.metabolism, 20*20, 1));
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
