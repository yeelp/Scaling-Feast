package yeelp.scalingfeast.items;

import java.util.Optional;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public final class ExhaustingBlockItem extends ExhaustingItemBlockBase {

	public ExhaustingBlockItem(Block block) {
		super(block, false);
	}

	private static final ITextComponent SPLASH = new TextComponentTranslation("tooltips.scalingfeast.exhaustingblock.info1");
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.exhaustingblock.info2").setStyle(new Style().setColor(TextFormatting.RED));
	@Override
	protected Optional<String> getUnlocalizedMetadataNameFromState(@Nonnull IBlockState state) {
		return Optional.empty();
	}

	@Override
	protected Iterable<String> getTooltipStrings() {
		return ImmutableList.of(SPLASH.getFormattedText(), INFO.getFormattedText());
	}

}
