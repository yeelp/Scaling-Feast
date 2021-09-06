package yeelp.scalingfeast.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.init.SFSounds;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

public final class ExhaustingApple extends ItemFood {

	private static final ITextComponent SPLASH = new TextComponentTranslation("tooltips.scalingfeast.exhaustingapple.splash").setStyle(new Style().setColor(TextFormatting.GOLD));
	private static final ITextComponent INFO = new TextComponentTranslation("tooltips.scalingfeast.exhaustingapple.info");

	public ExhaustingApple() {
		super(0, 0, false);
		this.setRegistryName("exhaustingapple");
		this.setUnlocalizedName(ModConsts.MOD_ID + ".exhaustingapple");
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(SPLASH.getFormattedText());
		tooltip.add(INFO.getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 64;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			double curr = SFBuiltInModifiers.MaxHungerModifiers.SHANK.getModifierValueForPlayer(player);
			ScalingFeastAPI.accessor.getSFFoodStats(player).applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SHANK.createModifier(curr - ModConfig.items.shank.inc));
			int max = AppleCoreAPI.accessor.getMaxHunger(player);
			AppleCoreAPI.mutator.setHunger(player, max);
			AppleCoreAPI.mutator.setSaturation(player, Math.min(max, ScalingFeastAPI.accessor.getPlayerSaturationCap(player)));
			SFSounds.playSound(player, SFSounds.HUNGER_DECREASE, 1.0f, 1.0f);
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
