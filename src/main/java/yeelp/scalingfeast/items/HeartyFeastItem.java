package yeelp.scalingfeast.items;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import squeek.applecore.api.food.FoodValues;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.blocks.HeartyFeastBlock;
import yeelp.scalingfeast.config.ModConfig;

import java.util.Collection;
import java.util.List;

/**
 * Hearty Feast cake block item
 * 
 * @author Yeelp
 *
 */
public class HeartyFeastItem extends ItemBlock implements IItemDescribable {

	private static final ITextComponent SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyfeast.splash");
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.heartyfeast.info");
	
	/**
	 * Create a new Hearty Feast
	 * 
	 * @param block the block this ItemBlock 'links' to.
	 */
	public HeartyFeastItem(Block block) {
		super(block);
		this.setRegistryName("heartyfeastitem");
		this.setTranslationKey(ModConsts.MOD_ID + ".heartyfeast");
		this.setCreativeTab(CreativeTabs.FOOD);
		this.setMaxStackSize(1);
	}

	@Override
	public Collection<String> getDescription(EntityPlayer player) {
		List<String> tooltips = Lists.newLinkedList(ImmutableList.of(SPLASH.getFormattedText(), INFO.getFormattedText()));
		if(ModConfig.items.feast.heartyFeastCap >= 0) {
			tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyfeast.cap", ModConfig.items.feast.heartyFeastCap).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText());
		}
		FoodValues fv = HeartyFeastBlock.getFoodValuesFor(player);
		tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyfeast.statincrease", player.getName(), fv.hunger, fv.hunger*fv.saturationModifier).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)).getFormattedText());
		int durationSeconds = ModConfig.items.feast.heartyFeastEffectDuration / 20;
		int durationMinutes = durationSeconds/60;
		durationSeconds %= 60;
		String time = String.format("%d:%02d", durationMinutes, durationSeconds);
		tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyfeast.effect", time).setStyle(new Style().setColor(TextFormatting.GREEN)).getFormattedText());
		return tooltips;
	}
}
