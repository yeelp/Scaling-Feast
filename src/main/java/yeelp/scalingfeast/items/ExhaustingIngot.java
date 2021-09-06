package yeelp.scalingfeast.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;

public final class ExhaustingIngot extends Item {
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.exhaustingingot.info");
	
	public ExhaustingIngot() {
		this.setRegistryName("exhaustingingot");
		this.setUnlocalizedName(ModConsts.MOD_ID + ".exhaustingingot");
		this.setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(INFO.getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public boolean isBeaconPayment(ItemStack stack) {
		return true;
	}
}
