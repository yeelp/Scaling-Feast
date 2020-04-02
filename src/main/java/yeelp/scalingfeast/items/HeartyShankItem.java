package yeelp.scalingfeast.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;

/**
 * Hearty Shank food item. Eating it increases max hunger
 * @author Yeelp
 *
 */
public class HeartyShankItem extends ItemFood
{
	public HeartyShankItem(int food, float sat)
	{
		super(food, sat, false);
		this.setAlwaysEdible();
		this.setRegistryName("heartyshank");
		this.setUnlocalizedName(ModConsts.MOD_ID+".heartyshank");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Eat this to gain " + ModConfig.foodCap.inc/2.0f + " hunger shanks");
		tooltip.add("added to your overall max hunger!");
	}
	
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 64;
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			IFoodCap currCap = ((EntityPlayer)entityLiving).getCapability(FoodCapProvider.capFoodStat, null);
			if(ModConfig.foodCap.globalCap != -1 && ModConfig.foodCap.globalCap > currCap.getMaxFoodLevel())
			{
				currCap.increaseMax((short)ModConfig.foodCap.inc);
				CapabilityHandler.syncCap((EntityPlayer)entityLiving);
			}
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
