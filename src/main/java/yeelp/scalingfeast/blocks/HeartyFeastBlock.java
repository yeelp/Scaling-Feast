package yeelp.scalingfeast.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.BlockCake;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import squeek.applecore.api.IAppleCoreAccessor;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdibleBlock;
import squeek.applecore.api.food.ItemFoodProxy;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.FoodCapProvider;

/**
 * The Hearty Feast Block. The hunger restored scales to a player's max hunger
 * @author Yeelp
 *
 */
public class HeartyFeastBlock extends BlockCake implements IEdibleBlock
{
	public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
	protected static final AxisAlignedBB[] CAKE_AABB =
			new AxisAlignedBB[] {new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
					new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
					new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
					new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
					new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
					new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
					new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};
	private boolean alwaysEdible = false;
	private int food = 0;
	private float sat = 0;
	public HeartyFeastBlock()
	{
		super();
		this.setRegistryName("heartyfeast");
		this.setUnlocalizedName(ModConsts.MOD_ID+".heartyfeast");
	}
	@Override
	public FoodValues getFoodValues(ItemStack itemStack) 
	{
		// TODO Auto-generated method stub
		return new FoodValues(food, sat);
	}

	@Override
	public void setEdibleAtMaxHunger(boolean value) 
	{
		this.alwaysEdible = value;

	}
	@Override
	public boolean onBlockActivated(@Nullable World world, @Nullable BlockPos pos, @Nullable IBlockState state, EntityPlayer player, @Nullable EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		this.food = player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel()/7;
		this.sat = this.food/4 + 0.5f;
		return this.eat(world, pos, state, player);
	}
	
	private boolean eat(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (!player.canEat(alwaysEdible))
		{
			return false;
		}
		else
		{
			onEatenCompatibility(new ItemStack(this), player);
			int bites = state.getValue(BITES);
			if (bites < 6)
			{
				world.setBlockState(pos, state.withProperty(BITES, bites + 1), 3);
			}
			else
			{
				world.setBlockToAir(pos);
			}

			return true;
		}
	}
	private void onEatenCompatibility(ItemStack itemStack, EntityPlayer player)
	{
		player.getFoodStats().addStats(new ItemFoodProxy(this), itemStack);
	}
}
