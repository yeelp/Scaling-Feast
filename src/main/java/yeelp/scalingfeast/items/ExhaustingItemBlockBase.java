package yeelp.scalingfeast.items;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ExhaustingItemBlockBase extends ItemBlock {

	public ExhaustingItemBlockBase(Block block, boolean hasSubtypes) {
		super(block);
		this.setHasSubtypes(hasSubtypes);
		this.setRegistryName(this.block.getRegistryName());
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + this.getUnlocalizedMetadataNameFromState(this.block.getStateFromMeta(stack.getMetadata())).map((s) -> "." + s).orElse("");
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		this.getTooltipStrings().forEach(tooltip::add);
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	protected abstract Optional<String> getUnlocalizedMetadataNameFromState(IBlockState state);
	
	protected abstract Iterable<String> getTooltipStrings();
}
