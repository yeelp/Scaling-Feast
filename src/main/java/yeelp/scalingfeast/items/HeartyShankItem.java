package yeelp.scalingfeast.items;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.init.SFSounds;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier;

/**
 * Hearty Shank food item. Eating it increases max hunger
 * 
 * @author Yeelp
 *
 */
public class HeartyShankItem extends ItemFood implements IItemDescribable {
	private static final String TEXT_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyshank.info1").setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText();
	private static final String AT_MAX_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyshank.atmax").setStyle(new Style().setColor(TextFormatting.RED).setBold(true)).getFormattedText();
	private static final String NO_BONUS_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyshank.nobonus").setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText();
	private static final DecimalFormat formatter = new DecimalFormat("##.#");

	public HeartyShankItem(int food, float sat) {
		super(food, sat, false);
		this.setAlwaysEdible();
		this.setRegistryName("heartyshank");
		this.setTranslationKey(ModConsts.MOD_ID + ".heartyshank");
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 64;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(player);
			if(canConsumeForMaxHunger(player)) {
				double currentShankBonus = getCurrentShankBonus(player);
				double currentStarvePenalty = getCurrentStarvePenalty(player);
				double currentDeathPenalty = getCurrentDeathPenalty(player);
				List<AttributeModifier> newMods = new LinkedList<AttributeModifier>();
				double overflow = currentStarvePenalty + ModConfig.items.shank.inc;
				double newStarvePenalty = Math.min(overflow, 0.0);
				overflow = currentDeathPenalty + Math.max(overflow, 0);
				double newDeathPenalty = Math.min(overflow, 0.0);
				double newShankBonus = Math.min(currentShankBonus + Math.max(overflow, 0), getShankUsageCap());
				if(newShankBonus != currentShankBonus) {
					newMods.add(SFBuiltInModifiers.MaxHungerModifiers.SHANK.createModifier(newShankBonus));
				}
				if(newStarvePenalty != currentStarvePenalty) {
					newMods.add(SFBuiltInModifiers.MaxHungerModifiers.PENALTY.createModifier(newStarvePenalty));
				}
				if(newDeathPenalty != currentDeathPenalty) {
					newMods.add(SFBuiltInModifiers.MaxHungerModifiers.DEATH.createModifier(newDeathPenalty));
				}
				newMods.forEach(sfstats::applyMaxHungerModifier);
				SFSounds.playSound(player, SFSounds.HUNGER_INCREASE, 1.0f, 1.0f);
			}
			if(ModConfig.features.starve.tracker.shankResetsCounter) {
				sfstats.resetStarvationTracker();
			}
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public Collection<String> getDescription(EntityPlayer player) {
		List<String> tooltips = new LinkedList<String>();
		tooltips.add(TEXT_SPLASH);
		if(canConsumeForMaxHunger(player)) {
			tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyshank.info2", formatter.format(getUseBonus(player) / 2.0f)).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
			if(ModConfig.items.shank.heartyShankCap != -1) {
				tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyshank.info3", getUsesLeft(player)).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
			}
		}
		else if(atMaxUses(player)) {
			tooltips.add(AT_MAX_SPLASH);
		}
		else {
			tooltips.add(NO_BONUS_SPLASH);
		}
		return tooltips;
	}

	public static boolean canConsumeForMaxHunger(EntityPlayer player) {
		return ModConfig.items.shank.heartyShankCap == -1 || getUsesLeft(player) > 0;
	}

	private static boolean atMaxUses(EntityPlayer player) {
		return ModConfig.items.shank.heartyShankCap != -1 && getUsesLeft(player) == 0;
	}

	private static int getUsesLeft(EntityPlayer player) {
		// penalties are negative, so we use subtraction to 'add' them to the cap total.
		return (int) Math.ceil((getShankUsageCap() - getCurrentStarvePenalty(player) - getCurrentDeathPenalty(player) - getCurrentShankBonus(player)) / ModConfig.items.shank.inc);
	}

	private static int getUseBonus(EntityPlayer player) {
		return (int) MathHelper.clamp(getShankUsageCap() - getCurrentStarvePenalty(player) - getCurrentDeathPenalty(player) - getCurrentShankBonus(player), 0, ModConfig.items.shank.inc);
	}

	private static double getCurrentShankBonus(EntityPlayer player) {
		return getAttributeValue(player, SFBuiltInModifiers.MaxHungerModifiers.SHANK);
	}

	private static double getCurrentStarvePenalty(EntityPlayer player) {
		return getAttributeValue(player, SFBuiltInModifiers.MaxHungerModifiers.PENALTY);
	}

	private static double getCurrentDeathPenalty(EntityPlayer player) {
		return getAttributeValue(player, SFBuiltInModifiers.MaxHungerModifiers.DEATH);
	}

	private static double getAttributeValue(EntityPlayer player, BuiltInModifier mod) {
		return ScalingFeastAPI.accessor.getSFFoodStats(player).getMaxHungerModifier(mod.getUUID()).map(AttributeModifier::getAmount).orElse(0.0);
	}
	
	private static int getShankUsageCap() {
		return ModConfig.items.shank.heartyShankCap < 0 ? Integer.MAX_VALUE : ModConfig.items.shank.heartyShankCap;
	}
}
