package yeelp.scalingfeast.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import yeelp.scalingfeast.ModConsts;

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
}
