package yeelp.scalingfeast.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.hunger.HungerEvent;
import squeek.applecore.api.hunger.StarvationEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.helpers.AppleSkinHelper;
import yeelp.scalingfeast.network.SatSyncMessage;

public class FoodHandler extends Handler 
{	
	private static final Map<UUID, Float> satLevels = new HashMap<UUID, Float>();
	
	@SubscribeEvent
	public void onGetMaxHunger(HungerEvent.GetMaxHunger evt)
	{
		evt.maxHunger = ScalingFeastAPI.accessor.getModifiedFoodCap(evt.player);
	}
	
	//Even if this event is canceled, we probably want to tick the starvation tracker. This is fine, as it will only succeed if the food level is zero.
	@SubscribeEvent(receiveCanceled=true)
	public void onStarve(StarvationEvent.Starve evt)
	{
		//only do any of this if there is max hunger to lose, otherwise this is a waste of processing.
		EntityPlayer player = evt.player;
		if(ModConfig.foodCap.starve.starveLoss != 0 && !player.isDead)
		{
			ScalingFeastAPI.mutator.tickPlayerStarvationTracker(player);
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
	}
	
	@SubscribeEvent
	public void onFoodEaten(FoodEvent.FoodEaten evt)
	{
		ScalingFeastAPI.mutator.capPlayerSaturation(evt.player);
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

	/*
	@SubscribeEvent
	public void onPlayerAttacked(LivingHurtEvent evt)
	{
		DamageSource src = evt.getSource();
		if(src.damageType.equals("mob") || (src.getTrueSource() instanceof EntityLivingBase && !(src.getTrueSource() instanceof EntityPlayer)))
		{
			EntityLivingBase entity = evt.getEntityLiving();
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entity;
				ScalingFeast.info(String.format("Damaging %s's foodstats by %f", player.getName(), evt.getAmount()));
				ScalingFeastAPI.mutator.damageFoodStats(player, evt.getAmount());
			}
		}
	}*/
}
