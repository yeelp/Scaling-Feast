package yeelp.scalingfeast.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;

/**
 * Iron Apple. Eating it restores some health and regens hunger over time.
 * 
 * @author Yeelp
 *
 */
public class IronAppleItem extends ItemFood {
	private static final ITextComponent TEXT_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.ironapple.splash").setStyle(new Style().setColor(TextFormatting.GOLD));
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.ironapple.info").setStyle(new Style().setColor(TextFormatting.GRAY));

	public IronAppleItem() {
		super(0, 0, false);
		this.setAlwaysEdible();
		this.setRegistryName("ironapple");
		this.setUnlocalizedName(ModConsts.MOD_ID + ".ironapple");
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TEXT_SPLASH.getFormattedText());
		tooltip.add(INFO.getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		int gluttonyLevel = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.gluttony, entityLiving);
		float mod = (1 + 0.5f * gluttonyLevel);
		entityLiving.heal(4.0f * mod);
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.metabolism, 20 * 20, 1));
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.bloated, 20 * 60 * 2));
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
