package yeelp.scalingfeast.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;

public class EnchantmentHandler extends Handler
{
	private static final Map<UUID, Boolean> fullSwing = new HashMap<UUID, Boolean>();
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=true)
	public void getPlayerSpecificFoodValues(FoodEvent.GetPlayerFoodValues evt)
	{
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.gluttony, evt.player);
		if(level != 0 && evt.player != null) //Player can apparently be null, according to AppleCore.
		{
			float mod = (1 + 0.5f*level);
			FoodValues foodvals = evt.foodValues;
			int newHunger = (int)(foodvals.hunger*mod);
			evt.foodValues = new FoodValues(newHunger, foodvals.saturationModifier);
		}
	}
	
	@SubscribeEvent
	public void onSwing(AttackEntityEvent evt)
	{
		fullSwing.put(evt.getEntityPlayer().getUniqueID(), evt.getEntityPlayer().getCooledAttackStrength(0) == 1.0f);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onAttackEvent(LivingAttackEvent evt)
	{
		Entity source = evt.getSource().getTrueSource();
		if(!(source instanceof EntityLivingBase))
		{
			return;
		}
		EntityLivingBase defender = evt.getEntityLiving();
		boolean isPlayerAttacker = source instanceof EntityPlayer;
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.famine, (EntityLivingBase) source);
		if(level != 0)
		{
			if(!(defender instanceof EntityPlayer))
			{
				int wlevel = (level < 3 ? 0 : 1);
				defender.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 15*20,  wlevel));
			}
			else
			{
				boolean canPlayerUseFamine = false;
				if(isPlayerAttacker)
				{
					EntityPlayer attacker = (EntityPlayer) source;
					canPlayerUseFamine = fullSwing.get(attacker.getUniqueID());
					fullSwing.remove(attacker.getUniqueID());
				}
				if(!isPlayerAttacker || canPlayerUseFamine) //Player attacker => PlayerUsedFamine
				{
					EntityPlayer player = (EntityPlayer) defender;
					ScalingFeastAPI.mutator.damageFoodStats(player, 1.25f*level);
				}
			}	
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void getMaxExhaustion(ExhaustionEvent.GetMaxExhaustion evt)
	{
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.lazinessCurse, evt.player);
		if(level != 0)
		{
			evt.maxExhaustionLevel /= 2.0f;
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void getStarvationRate(StarvationEvent.GetStarveTickPeriod evt)
	{
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.deprivationCurse, evt.player);
		if(level != 0)
		{
			evt.starveTickPeriod /= 2.0f;
		}
	}
	
	@SubscribeEvent
	public void onFoodStatsAddition(FoodEvent.FoodStatsAddition evt)
	{
		EntityPlayer player = evt.player;
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.sensitivityCurse, player);
		if(level != 0)
		{
			int overflow = (evt.foodValuesToBeAdded.hunger + player.getFoodStats().getFoodLevel()) - ScalingFeastAPI.accessor.getModifiedFoodCap(player);
			ScalingFeast.debug(Integer.toString(overflow));
			if(overflow > 0)
			{
				player.addPotionEffect(new PotionEffect(SFPotion.softstomach, 15*20, overflow-1));
			}
		}
	}
	
	@SubscribeEvent
	public void onKillEvent(LivingDeathEvent evt)
	{
		Entity entity = evt.getSource().getTrueSource();
		if(entity != null && entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.eternalfeast, player);
			if(level!=0)
			{
				player.getFoodStats().addStats(2*level, 0);
			}
		}
	}
}
