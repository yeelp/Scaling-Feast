package yeelp.scalingfeast.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import yeelp.scalingfeast.ModConsts;

public class HeartyFeastItem extends ItemBlock
{

	public HeartyFeastItem(Block block) 
	{
		super(block);
		this.setRegistryName("heartyfeast");
		this.setUnlocalizedName(ModConsts.MOD_ID+".heartyfeast");
		this.setCreativeTab(CreativeTabs.FOOD);
	}

}
