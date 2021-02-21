package yeelp.scalingfeast.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;
import squeek.applecore.api.hunger.HungerEvent;
import squeek.applecore.api.hunger.HungerRegenEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.helpers.AppleSkinHelper;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.network.SatSyncMessage;

public class FoodHandler extends Handler 
{	
	private static final Map<UUID, Float> satLevels = new HashMap<UUID, Float>();
	
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt)
	{
		evt.maxHunger = ScalingFeastAPI.accessor.getModifiedFoodCap(evt.player);
	}

	@SubscribeEvent
	public void onStarve(StarvationEvent.Starve evt)
	{
		if(ScalingFeastAPI.accessor.getBloatedHungerAmount(evt.player) > 0)
		{
			evt.setCanceled(true);
		}
		else
		{
			EntityPlayer player = evt.player;
			int bonusDamage = ModConfig.foodCap.starve.doDynamicStarvation ? ModConfig.foodCap.starve.bonusStarveDamageMult*ScalingFeastAPI.accessor.getBonusExhaustionDamage(player) : 0;
			ScalingFeastAPI.accessor.getStarveExhaustionTracker(player).reset();
			CapabilityHandler.syncStarveExhaust(player);
			evt.starveDamage += bonusDamage;
			if(ModConfig.foodCap.starve.starveLoss != 0 && !player.isDead)
			{
				if(bonusDamage > 0)
				{
					ScalingFeastAPI.mutator.tickPlayerStarvationTracker(player, bonusDamage);
				}
				ScalingFeastAPI.mutator.tickPlayerStarvationTracker(player);
			}
		}
	}
	
	@SubscribeEvent
	public void onFoodStatsAddition(FoodEvent.FoodStatsAddition evt)
	{
		//Only sync if the food level is zero, as that is the only time the tracker should be resetting. The tracker should always be zero if the frequency resets and the food level > 0.
		EntityPlayer player = evt.player;
		if(ModConfig.foodCap.starve.doesFreqReset && player.getFoodStats().getFoodLevel() == 0)
		{
			ScalingFeastAPI.accessor.getStarvationTracker(player).reset();
			if(!player.world.isRemote)
			{
				CapabilityHandler.syncTracker(player);
			}
		}
		
		if(!player.isPotionActive(SFPotion.bloated) && ModConfig.foodCap.doBloatedOverflow)
		{
			int diff = evt.foodValuesToBeAdded.hunger - (AppleCoreAPI.accessor.getMaxHunger(player) - player.getFoodStats().getFoodLevel());
			int level = diff > 0 ? diff/4 - 1 : -1;
			
			if(level >= 0)
			{
				player.addPotionEffect(new PotionEffect(SFPotion.bloated, ModConfig.foodCap.bloatedOverflowDuration, level));
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent evt)
	{
		//AppleSkin will do this for us, no point in duplicating behaviour if it's loaded.
		if(AppleSkinHelper.isLoaded() || !(evt.getEntity() instanceof EntityPlayerMP))
		{
			return;
		}
		else
		{
			EntityPlayerMP player = (EntityPlayerMP) evt.getEntity();
			float currSatLevel = player.getFoodStats().getSaturationLevel();
			//we use the wrapper class to simplify the case where satLevels.get returns null
			//However, we should use Float.floatValue() when comparing to the primitive float currSatLevel.
			Float lastSatLevel = satLevels.get(player.getUniqueID());
			if(lastSatLevel == null || lastSatLevel.floatValue() != currSatLevel)
			{
				PacketHandler.INSTANCE.sendTo(new SatSyncMessage(currSatLevel), player);
				satLevels.put(player.getUniqueID(), currSatLevel);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(PlayerTickEvent evt)
	{
		if(evt.player.getFoodStats().getFoodLevel() > ScalingFeastAPI.accessor.getModifiedFoodCap(evt.player))
		{
			AppleCoreAPI.mutator.setHunger(evt.player, ScalingFeastAPI.accessor.getModifiedFoodCap(evt.player));
		}
		ScalingFeastAPI.mutator.capPlayerHunger(evt.player);
		ScalingFeastAPI.mutator.capPlayerSaturation(evt.player);
		ScalingFeastAPI.mutator.setFoodEfficiencyXPBonus(evt.player);
		ScalingFeastAPI.mutator.setMaxHungerXPBonus(evt.player);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt)
	{
		if(!(evt.player instanceof EntityPlayerMP))
		{
			return;
		}
		else
		{
			satLevels.remove(evt.player.getUniqueID());
		}
	}
	
	@SubscribeEvent
	public void onExhaustion(ExhaustionEvent.ExhaustionAddition evt)
	{
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.fasting, evt.player);
		float fastingMod = 1.0f;
		if(level != 0)
		{
			fastingMod = (1-0.1f*level);
			fastingMod = fastingMod < 0 ? 0 : fastingMod;
		}
		double foodEfficiency = ScalingFeastAPI.accessor.getFoodEfficiency(evt.player).getAttributeValue();
		ScalingFeast.debug("foodEfficiency:" + foodEfficiency);
		if(foodEfficiency < 1)
		{
			foodEfficiency = -1.0/(foodEfficiency-2);
		}
		double reduction = fastingMod/foodEfficiency;
		evt.deltaExhaustion *= reduction < 0 ? 0 : reduction;
		if(evt.player.getFoodStats().getFoodLevel() == 0 && !evt.player.isDead)
		{
			ScalingFeastAPI.mutator.addStarveExhaustion(evt.player, evt.deltaExhaustion);
		}
	}
	
	@SubscribeEvent
	public void onExhausted(ExhaustionEvent.Exhausted evt)
	{
		if(ScalingFeastAPI.accessor.getBloatedHungerAmount(evt.player) > 0)
		{
			EntityPlayer player = evt.player;
			short remainder = ScalingFeastAPI.accessor.getBloatedHunger(player).deductBloatedAmount((short) -(evt.deltaHunger + evt.deltaSaturation));
			FoodStats fs = player.getFoodStats();
			float currSatLevel = fs.getSaturationLevel();
			if(currSatLevel == 0)
			{
				evt.deltaHunger = remainder;
			}
			else
			{
				if(currSatLevel >= remainder)
				{
					evt.deltaSaturation = remainder;
					evt.deltaHunger = 0;
				}
				else
				{
					evt.deltaSaturation = currSatLevel;
					evt.deltaHunger = (int) (remainder - currSatLevel);
				}
			}
			CapabilityHandler.syncBloatedHunger(player);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onGetExhaustionCap(ExhaustionEvent.GetExhaustionCap evt)
	{
		evt.exhaustionLevelCap = Float.MAX_VALUE;
	}
	
	@SubscribeEvent
	public void onHungerRegen(HealthRegenEvent.AllowRegen evt)
	{
		if(!evt.getResult().equals(Result.DENY))
		{
			switch(ModConfig.foodCap.hungerRegen)
			{
				case DISABLED:
					evt.setResult(Result.DENY);
					break;
				case VANILLA:
					break;
				case VANILLA_LIKE:
					EntityPlayer player = evt.player;
					if(player.getFoodStats().getFoodLevel() >= ScalingFeastAPI.accessor.getModifiedFoodCap(player) - 2)
					{
						evt.setResult(Result.ALLOW);
					}
					else
					{
						evt.setResult(Result.DENY);
					}
					break;
				default:
					break;
			}
		}
	}
	
	@SubscribeEvent
	public void onSaturatedRegen(HealthRegenEvent.AllowSaturatedRegen evt)
	{
		if(!evt.getResult().equals(Result.DENY))
		{
			switch(ModConfig.foodCap.satRegen)
			{
				case DISABLED:
					evt.setResult(Result.DENY);
					break;
				case VANILLA:
					break;
				case VANILLA_LIKE:
					EntityPlayer player = evt.player;
					if(player.getFoodStats().getFoodLevel() >= ScalingFeastAPI.accessor.getModifiedFoodCap(player) && player.getFoodStats().getSaturationLevel() > 0.0f)
					{
						evt.setResult(Result.ALLOW);
					}
					else
					{
						evt.setResult(Result.DENY);
					}
					break;
				default:
					break;
			}
		}
	}

	@SubscribeEvent
	public void onPlayerAttacked(LivingDamageEvent evt)
	{
		DamageSource src = evt.getSource();
		if(src.damageType.equals("mob") || (src.getTrueSource() instanceof EntityLivingBase && !(src.getTrueSource() instanceof EntityPlayer)))
		{
			EntityLivingBase entity = evt.getEntityLiving();
			if(entity instanceof EntityPlayer && ModConfig.foodCap.hungerDamageMultiplier != 0)
			{
				EntityPlayer player = (EntityPlayer) entity;
				ScalingFeastAPI.mutator.damageFoodStats(player, (float) (ModConfig.foodCap.hungerDamageMultiplier*evt.getAmount()));
			}
		}
	}
}
