package yeelp.scalingfeast.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.helpers.ModuleNotLoadedException;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCapModifier;

public class ModuleHandler extends Handler 
{	
	private HashMap<UUID, Integer> eatingPlayers = new HashMap<UUID, Integer>();
	private ITextComponent punishFoodTooltip = new TextComponentTranslation("modules.scalingfeast.spiceoflife.tooltip.punish").setStyle(new Style().setColor(TextFormatting.RED));
	private ITextComponent restoreFoodTooltip = new TextComponentTranslation("modules.scalingfeast.spiceoflife.tooltip.restore").setStyle(new Style().setColor(TextFormatting.GREEN));
	
	
	@SubscribeEvent 
	public void start(LivingEntityUseItemEvent.Start evt)
	{
		if(!(evt.getEntityLiving() instanceof EntityPlayer) || evt.isCanceled())
		{
			return;
		}
		else
		{
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !player.world.isRemote))
			{
				if(!eatingPlayers.containsKey(player.getUniqueID()))
				{
					try 
					{
						eatingPlayers.put(player.getUniqueID(), SOLCarrotHelper.getCountableFoodListLength(player));
					} 
					catch (ModuleNotLoadedException e)
					{
						ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
						ScalingFeast.err(Arrays.toString(e.getStackTrace()));
					}
				}
			}
		}
	}
	
	@SubscribeEvent 
	public void stop(LivingEntityUseItemEvent.Stop evt)
	{
		if(!(evt.getEntityLiving() instanceof EntityPlayer) || evt.isCanceled())
		{
			return;
		}
		else
		{
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !player.world.isRemote))
			{
				eatingPlayers.remove(player.getUniqueID());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onFoodEaten(FoodEvent.FoodEaten evt)
	{
		if(eatingPlayers.containsKey(evt.player.getUniqueID()))
		{
			updatePlayer(evt.player);
			if(SOLCarrotHelper.isEnabled())
			{
				try 
				{
					if(SOLCarrotHelper.reachedMilestone(evt.player) && eatingPlayers.get(evt.player.getUniqueID()) != SOLCarrotHelper.getCountableFoodListLength(evt.player))
					{
						ITextComponent splash = new TextComponentTranslation("modules.scalingfeast.sol.splash"+SOLCarrotHelper.getRewardSplashNumber());
						ITextComponent msg = new TextComponentTranslation("modules.scalingfeast.sol.reward", SOLCarrotHelper.getLastMilestoneReached(evt.player).getReward());
						if(!ModConfig.modules.sol.rewardMsgAboveHotbar)
						{
							msg.setStyle(new Style().setColor(TextFormatting.GREEN));
							splash.setStyle(new Style().setColor(TextFormatting.GREEN));
						}
						ITextComponent fullMsg = new TextComponentString(splash.getFormattedText() +" "+ msg.getFormattedText());
					
						evt.player.sendStatusMessage(fullMsg, ModConfig.modules.sol.rewardMsgAboveHotbar);
						evt.player.world.playSound(null, evt.player.posX, evt.player.posY, evt.player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
					}
				}	 
				catch (ModuleNotLoadedException e) 
				{
					ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
					ScalingFeast.err(Arrays.toString(e.getStackTrace()));
				}
			}
			eatingPlayers.remove(evt.player.getUniqueID());
		}
	}
	
	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	@Optional.Method(modid = ModConsts.SPICEOFLIFE_ID)
	public void onTooltip(ItemTooltipEvent evt)
	{
		if(SpiceOfLifeHelper.isEnabled() && evt.getEntityPlayer() != null && evt.getItemStack() != null && AppleCoreAPI.accessor.isFood(evt.getItemStack()))
		{
			SpiceOfLifeHelper.ToolTipType type = SpiceOfLifeHelper.getToolTipType(evt.getItemStack(), evt.getEntityPlayer());
			if(type != null)
			{
				switch(type)
				{
					case GOOD:
						evt.getToolTip().add(restoreFoodTooltip.getFormattedText());
						break;
					case BAD:
						evt.getToolTip().add(punishFoodTooltip.getFormattedText());
						break;
					default:
						break;
				}
			}
		}
	}
	
	public static void updatePlayer(EntityPlayer player)
	{
		short mod = 0;
		IFoodCapModifier curr = player.getCapability(FoodCapModifierProvider.foodCapMod, null);
		if(SpiceOfLifeHelper.isEnabled() && ModConfig.modules.spiceoflife.enabled)
		{
			mod += SpiceOfLifeHelper.getPenalty(player);
			ScalingFeast.info(""+mod);
		}
		if(SOLCarrotHelper.isEnabled() && ModConfig.modules.sol.enabled)
		{
			mod += SOLCarrotHelper.getReward(player);
		}
		if(curr.getModifier() == mod)
		{
			return;
		}
		else
		{
			curr.setModifier(mod);
			int currMax = player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel(curr);
			FoodStats fs = player.getFoodStats();
			if(fs.getFoodLevel() > currMax)
			{
				fs.setFoodLevel(currMax);
			}
			if(fs.getSaturationLevel() > currMax)
			{
				fs.setFoodSaturationLevel(fs.getFoodLevel());
			}
			if(!player.world.isRemote)
			{
				CapabilityHandler.sync(player);
			}
		}
	}
}
