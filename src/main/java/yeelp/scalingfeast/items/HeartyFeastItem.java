package yeelp.scalingfeast.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;

/**
 * Hearty Feast cake block item
 * @author Yeelp
 *
 */
public class HeartyFeastItem extends ItemBlock
{

	/**
	 * Create a new Hearty Feast
	 * @param block the block this ItemBlock 'links' to.
	 */
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
