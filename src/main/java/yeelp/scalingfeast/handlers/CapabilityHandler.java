
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
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.network.FoodCapMessage;
import yeelp.scalingfeast.network.FoodCapModifierMessage;
import yeelp.scalingfeast.network.StarvationTrackerMessage;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.SaturationUtil;
import yeelp.scalingfeast.util.StarvationTracker;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

public class CapabilityHandler extends Handler
{

	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		if(evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "FoodCap"), new FoodCap((short)ModConfig.foodCap.startingHunger));
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "StarvationTracker"), new StarvationTracker());
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "Modifier"), new FoodCapModifier());
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent evt)
	{
		EntityPlayer player = evt.player;
		short foodCap = player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel(player.getCapability(FoodCapModifierProvider.foodCapMod, null));
		FoodStats fs = player.getFoodStats();
		if(fs.getFoodLevel() > foodCap)
		{
			AppleCoreAPI.mutator.setHunger(player, foodCap);
			if(fs.getSaturationLevel() > fs.getFoodLevel())
			{
				fs.setFoodSaturationLevel(fs.getFoodLevel());
			}
		}
		ModuleHandler.updatePlayer(player);
		SaturationUtil.capSaturation(player);
	}
	
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent evt)
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
		IFoodCapModifier oldMod = evt.getOriginal().getCapability(FoodCapModifierProvider.foodCapMod, null);
		IFoodCapModifier newMod = evt.getEntityPlayer().getCapability(FoodCapModifierProvider.foodCapMod, null);
		newFoodCap.deserializeNBT(oldFoodCap.serializeNBT());
		newTracker.deserializeNBT(oldTracker.serializeNBT());
		newMod.deserializeNBT(oldMod.serializeNBT());
		if(evt.isWasDeath())
		{
			FoodStats newFs = evt.getEntityPlayer().getFoodStats();
			FoodStats oldFs = evt.getOriginal().getFoodStats();
			
			AppleCoreAPI.mutator.setHunger(evt.getEntityPlayer(), newFoodCap.getMaxFoodLevel(newMod));
			AppleCoreAPI.mutator.setSaturation(evt.getEntityPlayer(), newFoodCap.getMaxFoodLevel(newMod) < 5 ? newFoodCap.getMaxFoodLevel(newMod) : 5);
			SaturationUtil.capSaturation(evt.getEntityPlayer());
			
			if(ModConfig.foodCap.death.maxLossAmount != 0)
			{
				newFoodCap.decreaseMax((short) ModConfig.foodCap.death.maxLossAmount);
			}
			if(ModConfig.foodCap.death.hungerLossOnDeath != 0)
			{
				if(oldFs.getFoodLevel() - ModConfig.foodCap.death.hungerLossOnDeath >= 20)
				{
					AppleCoreAPI.mutator.setHunger(evt.getEntityPlayer(), oldFs.getFoodLevel() - ModConfig.foodCap.death.hungerLossOnDeath);
				}
			}
		}
		if(!evt.isWasDeath() || !ModConfig.foodCap.starve.doesFreqReset)
		{
			syncTracker(evt.getEntityPlayer());
		}
		syncCap(evt.getEntityPlayer());
		syncMod(evt.getEntityPlayer());
	}
	
	public static void sync(EntityPlayer player)
	{
		syncCap(player);
		syncTracker(player);
		syncMod(player);
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
	
	public static void syncMod(EntityPlayer player)
	{
		IFoodCapModifier mod = player.getCapability(FoodCapModifierProvider.foodCapMod, null);
		PacketHandler.INSTANCE.sendTo(new FoodCapModifierMessage(mod), (EntityPlayerMP)player);
	}
}
