package yeelp.scalingfeast.items;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.blocks.ExhaustingOreBlock;
import yeelp.scalingfeast.blocks.ExhaustingOreBlock.RockType;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ExhaustingOreItemBlock extends ExhaustingItemBlockBase {

	private static final ITextComponent SPLASH = new TextComponentTranslation("tooltips.scalingfeast.exhaustingore.info1").setStyle(new Style().setColor(TextFormatting.GREEN));
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.exhaustingore.info2").setStyle(new Style().setColor(TextFormatting.RED));
	public ExhaustingOreItemBlock(Block block) {
		super(block, true);
	}

	@Override
	protected Optional<String> getUnlocalizedMetadataNameFromState(@Nonnull IBlockState state) {
		return Optional.of(state.getValue(ExhaustingOreBlock.ROCK_TYPE).toString().toLowerCase(Locale.CANADA));
	}

	@Override
	protected Iterable<String> getTooltipStrings() {
		return ImmutableList.of(SPLASH.getFormattedText(), INFO.getFormattedText());
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	public Map<Integer, ModelResourceLocation> getMetadataModels() {
		final ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(this.block);
		final String namePrefix = ExhaustingOreBlock.ROCK_TYPE.getName();
		final Map<Integer, ModelResourceLocation> map = Maps.newHashMap();
		for(RockType r : ExhaustingOreBlock.ROCK_TYPE.getAllowedValues()) {
			int meta = this.block.getMetaFromState(this.block.getDefaultState().withProperty(ExhaustingOreBlock.ROCK_TYPE, r));
			String nameSuffix = ExhaustingOreBlock.ROCK_TYPE.getName(r);
			@SuppressWarnings("DataFlowIssue")
			ModelResourceLocation model = new ModelResourceLocation(loc, namePrefix + "=" + nameSuffix);
			map.put(meta, model);
		}
		return map;
	}

}
