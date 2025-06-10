package yeelp.scalingfeast.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;

public class ExhaustingBlock extends ExhaustionIncreasingBlock {

	public ExhaustingBlock() {
		super(Material.IRON);
		this.setRegistryName("exhaustingblock");
		this.setTranslationKey(ModConsts.MOD_ID + ".exhaustingblock");
		this.setHardness(6f);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	float getExhaustionIncreaseMultiplier(EntityPlayer player) {
		return 2.0f;
	}

	@Override
	protected float getAddedExhaustionOnBlockBreak(EntityPlayer player) {
		return AppleCoreAPI.accessor.getMaxExhaustion(player) / 4.0f;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}

}
