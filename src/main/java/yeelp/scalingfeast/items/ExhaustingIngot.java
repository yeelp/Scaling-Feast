package yeelp.scalingfeast.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConsts;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public final class ExhaustingIngot extends Item {
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.exhaustingingot.info");

	public ExhaustingIngot() {
		this.setRegistryName("exhaustingingot");
		this.setTranslationKey(ModConsts.MOD_ID + ".exhaustingingot");
		this.setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(INFO.getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public boolean isBeaconPayment(ItemStack stack) {
		return true;
	}
}
