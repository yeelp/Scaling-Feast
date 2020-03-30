package yeelp.scalingfeast.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;

public class HeartyFeastItem extends ItemBlock
{

	
	public HeartyFeastItem(Block block) 
	{
		super(block);
		this.setRegistryName("heartyfeastitem");
		this.setUnlocalizedName(ModConsts.MOD_ID+".heartyfeast");
		this.setCreativeTab(CreativeTabs.FOOD);
		this.setMaxDamage(6);
		this.setNoRepair();
		this.setMaxStackSize(1);
	}
}
