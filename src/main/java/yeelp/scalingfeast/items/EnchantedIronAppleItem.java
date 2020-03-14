package yeelp.scalingfeast.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import yeelp.scalingfeast.ModConsts;

/**
 * The Enchanted Iron Apple which gives the Iron Stomach effect and a Metabolism effect when eaten.
 * @author Andrew
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
}
