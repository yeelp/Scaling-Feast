package yeelp.scalingfeast.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import yeelp.scalingfeast.ModConsts;

/**
 * Hearty Shank food item. Eating it increases max hunger
 * @author Yeelp
 *
 */
public class HeartyShankItem extends ItemFood
{
	public HeartyShankItem(int food, float sat)
	{
		super(food, sat, false);
		this.setAlwaysEdible();
		this.setRegistryName("heartyshank");
		this.setUnlocalizedName(ModConsts.MOD_ID+".heartyshank");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 64;
	}
}
