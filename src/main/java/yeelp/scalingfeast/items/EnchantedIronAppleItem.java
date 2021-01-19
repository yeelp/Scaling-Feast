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
 * The Enchanted Iron Apple which gives the Iron Stomach effect and a Metabolism effect when eaten.
 * @author Yeelp
 *
 */
public class EnchantedIronAppleItem extends ItemFood 
{
	private static final String TEXT_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.enchantedironapple.splash").setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText();
	private static final String INFO1 = new TextComponentTranslation("tooltips.scalingfeast.enchantedironapple.info1").setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText();
	private static final String INFO2 = new TextComponentTranslation("tooltips.scalingfeast.enchantedironapple.info2").setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText();
	public EnchantedIronAppleItem()
	{
		super(0, 0.0f, false);
		this.setAlwaysEdible();
		this.setRegistryName("enchantedironapple");
		this.setUnlocalizedName(ModConsts.MOD_ID+".enchantedironapple");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TEXT_SPLASH);
		tooltip.add(INFO1);
		tooltip.add(INFO2);
    }
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		int gluttonyLevel = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.gluttony, entityLiving);
		float mod = (1 + 0.5f*gluttonyLevel);
		entityLiving.heal(4.0f*mod);
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.metabolism, 20*20, 3));
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.ironstomach, 6*60*20, 1));
		entityLiving.addPotionEffect(new PotionEffect(SFPotion.bloated, 20*60*2, 3));
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
