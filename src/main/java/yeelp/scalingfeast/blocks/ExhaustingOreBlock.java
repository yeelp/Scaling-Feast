package yeelp.scalingfeast.blocks;

import java.util.Arrays;
import java.util.Locale;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import yeelp.scalingfeast.ModConsts;

public final class ExhaustingOreBlock extends ExhaustionIncreasingBlock {
	public static final PropertyEnum<RockType> ROCK_TYPE = PropertyEnum.create("type", RockType.class);
	
	public enum RockType implements IStringSerializable {
		STONE,
		NETHERRACK;

		@Override
		public String getName() {
			return this.toString().toLowerCase(Locale.CANADA);
		}
	}
	
	public ExhaustingOreBlock() {
		super(Material.ROCK);
		this.setRegistryName("exhaustingore");
		this.setTranslationKey(ModConsts.MOD_ID + ".exhaustingore");
		this.setHardness(8f);
		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		RockType value = Arrays.stream(RockType.values()).sorted().skip(meta).findFirst().orElse(RockType.STONE);
		return this.getDefaultState().withProperty(ROCK_TYPE, value);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROCK_TYPE).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		Arrays.stream(RockType.values()).forEach((r) -> items.add(new ItemStack(this, 1, r.ordinal())));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROCK_TYPE);
	}

	@Override
	float getExhaustionIncreaseMultiplier(EntityPlayer player) {
		return 1.5f;
	}

	@Override
	protected float getAddedExhaustionOnBlockBreak(EntityPlayer player) {
		return 6.0f;
	}
}
