package yeelp.scalingfeast.handlers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.util.ExtendedFoodStatsProvider;
import yeelp.scalingfeast.util.FoodStatsMap;
import yeelp.scalingfeast.util.ICappedFoodStats;

public class CapabilityHandler
{
	
	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		if(evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "ExtendedFoodStats"), new ExtendedFoodStatsProvider());
			ScalingFeast.info("Adding capability");
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt)
	{
		ICappedFoodStats fs = evt.player.getCapability(ExtendedFoodStatsProvider.capFoodStat, null);
		if(!FoodStatsMap.hasPlayer(evt.player.getUniqueID()) && fs != null)
		{
			FoodStatsMap.addPlayer(evt.player.getUniqueID(), fs.getFoodLevel(), fs.getSatLevel(), fs.getMaxFoodLevel());
			ScalingFeast.info(String.format("%s logged in with %d extra food, %f saturation, and %d max food", evt.player.getName(), fs.getFoodLevel(), fs.getSatLevel(), fs.getMaxFoodLevel()));
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent evt)
	{
		if(FoodStatsMap.hasPlayer(evt.player.getUniqueID()))
		{
			ICappedFoodStats fs = evt.player.getCapability(ExtendedFoodStatsProvider.capFoodStat, null);
			fs.setFoodLevel(FoodStatsMap.getExtraFoodLevel(evt.player.getUniqueID()));
			fs.setSatLevel(FoodStatsMap.getExtraSatLevels(evt.player.getUniqueID()));
			fs.setMax(FoodStatsMap.getMaxFoodLevel(evt.player.getUniqueID()));
			FoodStatsMap.removePlayer(evt.player.getUniqueID());
		}
	}
}
