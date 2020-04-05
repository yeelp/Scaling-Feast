package yeelp.scalingfeast.handlers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.network.FoodCapMessage;
import yeelp.scalingfeast.network.StarvationTrackerMessage;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.StarvationTracker;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

public class CapabilityHandler extends Handler
{

	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		if(evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "FoodCap"), new FoodCap());
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "StarvationTracker"), new StarvationTracker());
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(EntityJoinWorldEvent evt)
	{
		if(evt.getWorld().isRemote)
		{
			return;
		}
		if(!(evt.getEntity() instanceof EntityPlayer))
		{
			return;
		}
		sync((EntityPlayer) evt.getEntity());
	}
	
	@SubscribeEvent 
	public void onClone(PlayerEvent.Clone evt)
	{
		IFoodCap oldFoodCap = evt.getOriginal().getCapability(FoodCapProvider.capFoodStat, null);
		IFoodCap newFoodCap = evt.getEntityPlayer().getCapability(FoodCapProvider.capFoodStat, null);
		IStarvationTracker oldTracker = evt.getOriginal().getCapability(StarvationTrackerProvider.starvationTracker, null);
		IStarvationTracker newTracker = evt.getEntityPlayer().getCapability(StarvationTrackerProvider.starvationTracker, null);
		newFoodCap.deserializeNBT(oldFoodCap.serializeNBT());
		newTracker.deserializeNBT(oldTracker.serializeNBT());
		ScalingFeast.info(String.format("%d -> %d, %d -> %d", newFoodCap.getMaxFoodLevel(), oldFoodCap.getMaxFoodLevel(), newTracker.getCount(), oldTracker.getCount()));
		if(evt.isWasDeath())
		{
			if(ModConfig.foodCap.death.maxLossAmount != 0)
			{
				newFoodCap.decreaseMax((short) ModConfig.foodCap.death.maxLossAmount);
			}
			if(ModConfig.foodCap.death.hungerLossOnDeath != 0)
			{
				FoodStats newFs = evt.getEntityPlayer().getFoodStats();
				FoodStats oldFs = evt.getOriginal().getFoodStats();
				if(oldFs.getFoodLevel() - ModConfig.foodCap.death.hungerLossOnDeath >= 20)
				{
					newFs.setFoodLevel(oldFs.getFoodLevel() - ModConfig.foodCap.death.hungerLossOnDeath);
				}
			}
		}
		if(!evt.isWasDeath() || !ModConfig.foodCap.doesFreqReset)
		{
			syncTracker(evt.getEntityPlayer());
		}
		syncCap(evt.getEntityPlayer());
	}
	
	public static void sync(EntityPlayer player)
	{
		syncCap(player);
		syncTracker(player);
	}
	
	public static void syncCap(EntityPlayer player)
	{
		IFoodCap cap = player.getCapability(FoodCapProvider.capFoodStat, null);
		PacketHandler.INSTANCE.sendTo(new FoodCapMessage(cap), (EntityPlayerMP) player);
	}
	
	public static void syncTracker(EntityPlayer player)
	{
		IStarvationTracker tracker = player.getCapability(StarvationTrackerProvider.starvationTracker, null);
		PacketHandler.INSTANCE.sendTo(new StarvationTrackerMessage(tracker), (EntityPlayerMP)player);
	}
}
