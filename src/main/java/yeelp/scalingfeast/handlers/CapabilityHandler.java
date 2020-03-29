package yeelp.scalingfeast.handlers;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.FoodStatsMap;
import yeelp.scalingfeast.util.IFoodCap;

public class CapabilityHandler extends Handler
{
	
	private Set<UUID> deadPlayers = new HashSet<UUID>(); 
	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		if(evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "FoodCap"), new FoodCapProvider());
		}
	}
	
	
	@SubscribeEvent
	public void onPlayerDied(LivingDeathEvent evt)
	{
		if(evt.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) evt.getEntity();
			deadPlayers.add(player.getUniqueID());
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent evt)
	{
		if(deadPlayers.contains(evt.player.getUniqueID()))
		{
			deadPlayers.remove(evt.player.getUniqueID());
			evt.player.getFoodStats().setFoodLevel((int)((1-ModConfig.extendedFoodStats.death.percentLoss)*evt.player.getFoodStats().getFoodLevel()));
		}
	}
}
