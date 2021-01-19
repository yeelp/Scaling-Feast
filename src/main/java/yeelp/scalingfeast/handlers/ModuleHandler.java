package yeelp.scalingfeast.handlers;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
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
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.helpers.ModuleNotLoadedException;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCapModifier;

public class ModuleHandler extends Handler 
{	
	private static HashMap<UUID, Integer> eatingPlayers = new HashMap<UUID, Integer>();
	private static final UUID EFFICIENCY_UUID = UUID.fromString("1736a445-1a35-4230-8e3a-7aedda394df2");
	private static final ITextComponent PUNISH_FOOD_TOOLTIP = new TextComponentTranslation("modules.scalingfeast.spiceoflife.tooltip.punish").setStyle(new Style().setColor(TextFormatting.RED));
	private static final ITextComponent RESTORE_FOOD_TOOLTIP = new TextComponentTranslation("modules.scalingfeast.spiceoflife.tooltip.restore").setStyle(new Style().setColor(TextFormatting.GREEN));
	private static final Style GREEN_STYLE = new Style().setColor(TextFormatting.GREEN);
	private static final DecimalFormat PERCENT = new DecimalFormat("#%");
	
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
						eatingPlayers.put(player.getUniqueID(), SOLCarrotHelper.isEnabled() ? SOLCarrotHelper.getCountableFoodListLength(player) : 0);
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
					boolean b = eatingPlayers.get(evt.player.getUniqueID()) != SOLCarrotHelper.getCountableFoodListLength(evt.player);
					Deque<ITextComponent> msgs = new LinkedList<ITextComponent>();
					ITextComponent splash = new TextComponentTranslation("modules.scalingfeast.sol.splash"+SOLCarrotHelper.getRewardSplashNumber());
					splash.setStyle(GREEN_STYLE);
					if(SOLCarrotHelper.reachedMilestone(evt.player) && b)
					{
						ITextComponent msg = new TextComponentTranslation("modules.scalingfeast.sol.reward", SOLCarrotHelper.getLastRegularMilestoneReached(evt.player).getReward());
						msg.setStyle(GREEN_STYLE);
						ITextComponent fullMsg = new TextComponentString(splash.getFormattedText() +" "+ msg.getFormattedText());
						msgs.add(fullMsg);
					}
					if(SOLCarrotHelper.reachedFoodEfficiencyMilestone(evt.player) && b)
					{
						ITextComponent msg = new TextComponentTranslation("modules.scalingfeast.sol.efficiencyReward", PERCENT.format(SOLCarrotHelper.getLastEfficiencyMilestoneReached(evt.player).getReward()));
						msg.setStyle(GREEN_STYLE);
						ITextComponent fullMsg = new TextComponentString(splash.getFormattedText() +" "+ msg.getFormattedText());
						msgs.add(fullMsg);
					}
					if(SOLCarrotHelper.reachedAllMilestones(evt.player) && b)
					{
						ITextComponent msg = new TextComponentTranslation("modules.scalingfeast.sol.reachedAllMilestones");
						msg.setStyle(GREEN_STYLE);
						msgs.add(msg);
					}
					if(msgs.size() > 0)
					{
						ITextComponent lastMsg = msgs.removeLast();
						for(ITextComponent msg : msgs)
						{
							evt.player.sendStatusMessage(msg, false);
						}
						if(ModConfig.modules.sol.rewardMsgAboveHotbar)
						{
							lastMsg.setStyle(new Style());
						}
						evt.player.sendStatusMessage(lastMsg, ModConfig.modules.sol.rewardMsgAboveHotbar);
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
						evt.getToolTip().add(RESTORE_FOOD_TOOLTIP.getFormattedText());
						break;
					case BAD:
						evt.getToolTip().add(PUNISH_FOOD_TOOLTIP.getFormattedText());
						break;
					default:
						break;
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	@Optional.Method(modid = ModConsts.SOLCARROT_ID)
	public void onDeath(PlayerEvent.Clone evt)
	{
		if(evt.isWasDeath())
		{
			updatePlayer(evt.getEntityPlayer());
		}
	}
	
	public static short updatePlayer(EntityPlayer player)
	{
		short mod = 0;
		IFoodCapModifier curr = ScalingFeastAPI.accessor.getFoodCapModifier(player);
		if(SpiceOfLifeHelper.isEnabled() && ModConfig.modules.spiceoflife.enabled)
		{
			mod += SpiceOfLifeHelper.getPenalty(player);
		}
		if(SOLCarrotHelper.isEnabled() && ModConfig.modules.sol.enabled)
		{
			if(ModConfig.modules.sol.useMilestones)
			{
				mod += SOLCarrotHelper.getReward(player);
			}
			if(ModConfig.modules.sol.useFoodEfficiencyMilestones)
			{
				ScalingFeastAPI.mutator.setFoodEfficiencyModifier(player, EFFICIENCY_UUID, "SOL:Carrot Efficiency Milestone", SOLCarrotHelper.getEfficiencyModifier(player));
			}
		}
		if(curr.getModifier("modules") == mod)
		{
			return (short) curr.getModifier("modules");
		}
		else
		{
			curr.setModifier("modules", mod, FoodCapModifier.Operation.ADD);
			//We want to use the current IFoodCapModifier we have. 
			//We haven't synced it yet, and calling the convenience method from the API uses what's currently been synced, which is outdated.
			short currMax = ScalingFeastAPI.accessor.getFoodCap(player).getMaxFoodLevel(curr);
			FoodStats fs = player.getFoodStats();
			if(fs.getFoodLevel() > currMax)
			{
				AppleCoreAPI.mutator.setHunger(player, currMax);
				ScalingFeastAPI.mutator.capPlayerSaturation(player);
			}
			if(fs.getSaturationLevel() > currMax)
			{
				AppleCoreAPI.mutator.setSaturation(player, currMax);
				ScalingFeastAPI.mutator.capPlayerSaturation(player);
			}
			if(!player.world.isRemote)
			{
				CapabilityHandler.sync(player);
			}
			return mod;
		}
	}
}
