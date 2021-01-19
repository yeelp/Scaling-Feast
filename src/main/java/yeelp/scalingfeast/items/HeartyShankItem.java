package yeelp.scalingfeast.items;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

/**
 * Hearty Shank food item. Eating it increases max hunger
 * @author Yeelp
 *
 */
public class HeartyShankItem extends ItemFood
{
	private static final HeartyShankItem INSTANCE = new HeartyShankItem(ModConfig.items.heartyShankFoodLevel, (float)ModConfig.items.heartyShankSatLevel);
	
	private static final String TEXT_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyshank.info1").setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText();
	private static final String AT_MAX_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyshank.atmax").setStyle(new Style().setColor(TextFormatting.RED).setBold(true)).getFormattedText();
	private static final String NO_BONUS_SPLASH = new TextComponentTranslation("tooltips.scalingfeast.heartyshank.nobonus").setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText();
	private static final DecimalFormat formatter = new DecimalFormat("##.#");
	
	public HeartyShankItem(int food, float sat)
	{
		super(food, sat, false);
		this.setAlwaysEdible();
		this.setRegistryName("heartyshank");
		this.setUnlocalizedName(ModConsts.MOD_ID+".heartyshank");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 64;
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if(entityLiving instanceof EntityPlayer && !ModConfig.modules.isShankDisabled)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;
			IFoodCap currCap = player.getCapability(FoodCapProvider.capFoodStat, null);
			if((ModConfig.foodCap.globalCap == -1 || ModConfig.foodCap.globalCap > currCap.getUnmodifiedMaxFoodLevel()) && canConsumeForMaxHunger(player))
			{
				if(currCap.getUnmodifiedMaxFoodLevel() + ModConfig.foodCap.inc > ModConfig.foodCap.globalCap && ModConfig.foodCap.globalCap != -1)
				{
					currCap.setMax((short) ModConfig.foodCap.globalCap);
				}
				else
				{
					currCap.increaseMax((short)ModConfig.foodCap.inc);
					worldIn.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 1.0f, 1.0f);
				}
			}
			if(ModConfig.foodCap.starve.shankResetsCounter)
			{
				player.getCapability(StarvationTrackerProvider.starvationTracker, null).reset();
			}
			ScalingFeastAPI.mutator.tickPlayerShankUsageTicker(player);
			//A quick guarantee that things should work as intended.
			//We may not need to do this, but I'm not sure yet.
			//At the very least, syncing on multiplayer still works.
			//So, if it's multiplayer, sync it. Otherwise, it shouldn't matter?
			if(!worldIn.isRemote)
			{
				CapabilityHandler.sync((EntityPlayer)entityLiving);
			}
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
	
	private boolean canConsumeForMaxHunger(EntityPlayer player)
	{
		return ModConfig.foodCap.heartyShankCap == -1 || ScalingFeastAPI.accessor.getShankUsageCount(player) < ModConfig.foodCap.heartyShankCap;
	}
	
	public static List<String> buildTooltips(EntityPlayer player)
	{
		List<String> tooltips = new LinkedList<String>();
		tooltips.add(TEXT_SPLASH);
		if(INSTANCE.canConsumeForMaxHunger(player))
		{
			if(ModConfig.foodCap.globalCap == -1 || ScalingFeastAPI.accessor.getFoodCap(player).getUnmodifiedMaxFoodLevel() < ModConfig.foodCap.globalCap)
			{
				tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyshank.info2", formatter.format(ModConfig.foodCap.inc/2.0f)).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
				if(ModConfig.foodCap.heartyShankCap != -1)
				{
					tooltips.add(new TextComponentTranslation("tooltips.scalingfeast.heartyshank.info3", ModConfig.foodCap.heartyShankCap - ScalingFeastAPI.accessor.getShankUsageCount(player)).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
				}
			}
			else
			{
				tooltips.add(AT_MAX_SPLASH);
			}
		}
		else
		{
			tooltips.add(NO_BONUS_SPLASH);
		}
		return tooltips;
	}
}
