package yeelp.scalingfeast.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;

public class ExhaustingPotato extends ItemFood {
	private static final ITextComponent SPLASH = new TextComponentTranslation("tooltips.scalingfeast.exhaustingpotato.splash").setStyle(new Style().setColor(TextFormatting.GOLD));
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.exhaustingpotato.info");

	public ExhaustingPotato() {
		super(-4, 0.0f, false);
		this.setAlwaysEdible();
		this.setRegistryName("exhaustingpotato");
		this.setTranslationKey(ModConsts.MOD_ID + ".exhaustingpotato");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(SPLASH.getFormattedText());
		tooltip.add(INFO.getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}