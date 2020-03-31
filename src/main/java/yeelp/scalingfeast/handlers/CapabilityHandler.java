package yeelp.scalingfeast.handlers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.network.FoodCapMessage;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;

public class CapabilityHandler extends Handler
{

	@SubscribeEvent
	public void onAddCapabilities(AttachCapabilitiesEvent<Entity> evt)
	{
		if(evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "FoodCap"), new FoodCap());
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
		newFoodCap.deserializeNBT(oldFoodCap.serializeNBT());
		ScalingFeast.info(String.format("%d -> %d", newFoodCap.getMaxFoodLevel(), oldFoodCap.getMaxFoodLevel()));
		sync(evt.getEntityPlayer());
	}
	
	public static void sync(EntityPlayer player)
	{
		IFoodCap cap = player.getCapability(FoodCapProvider.capFoodStat, null);
		ScalingFeast.info(String.format("Sending %d", cap.getMaxFoodLevel()));
		PacketHandler.INSTANCE.sendTo(new FoodCapMessage(cap), (EntityPlayerMP) player);
	}
}
