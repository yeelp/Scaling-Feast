
package yeelp.scalingfeast.handlers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.network.BloatedHungerMessage;
import yeelp.scalingfeast.network.FoodCapMessage;
import yeelp.scalingfeast.network.FoodCapModifierMessage;
import yeelp.scalingfeast.network.StarvationTrackerMessage;
import yeelp.scalingfeast.network.StarveExhaustMessage;
import yeelp.scalingfeast.util.BloatedHunger;
import yeelp.scalingfeast.util.FoodCap;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;
import yeelp.scalingfeast.util.IStarvationTracker;
import yeelp.scalingfeast.util.StarvationTracker;
import yeelp.scalingfeast.util.StarveExhaustionTracker;

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
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "BloatedAmount"),  new BloatedHunger());
			evt.addCapability(new ResourceLocation(ModConsts.MOD_ID, "ExhaustionSinceStarve"), new StarveExhaustionTracker());
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt)
	{
		IAttributeInstance instance = ScalingFeastAPI.accessor.getMaxHungerAttributeModifier(evt.player);
		IFoodCapModifier modifier = ScalingFeastAPI.accessor.getFoodCapModifier(evt.player);
		float[] mods = new float[] {0, 0, 0};
		boolean flag = false;
		for(FoodCapModifier.Operation op : FoodCapModifier.Operation.values())
		{
			int i = op.ordinal();
			for(AttributeModifier mod : instance.getModifiersByOperation(i))
			{
				if(i != 2)
				{
					mods[i] += mod.getAmount();
				}
				else
				{
					if(!flag)
					{
						mods[2] = (float) (1 + mod.getAmount());
						flag = true;
					}
					mods[2] *= (float) (1 + mod.getAmount());
				}
			}
		}
		modifier.setModifier("attributes", mods[0], FoodCapModifier.Operation.ADD);
		modifier.setModifier("attributesMult", mods[1], FoodCapModifier.Operation.PERCENT_STACK_ADDITVELY);
		modifier.setModifier("attributesPercent", mods[2]-1, FoodCapModifier.Operation.PERCENT_STACK_MULTIPLICATIVELY);
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
		EntityPlayer player = (EntityPlayer) evt.getEntity();
		sync(player);
		short foodCap = ScalingFeastAPI.accessor.getFoodCap(player).getUnmodifiedMaxFoodLevel();
		FoodStats fs = player.getFoodStats();
		if(fs.getFoodLevel() > foodCap)
		{
			AppleCoreAPI.mutator.setHunger(player, foodCap);
			if(fs.getSaturationLevel() > fs.getFoodLevel())
			{
				fs.setFoodSaturationLevel(fs.getFoodLevel());
			}
		}
		ScalingFeastAPI.mutator.capPlayerHunger(player);
		ScalingFeastAPI.mutator.capPlayerSaturation(player);
		ModuleHandler.updatePlayer(player);
	}
	
	@SubscribeEvent 
	public void onClone(PlayerEvent.Clone evt)
	{
		EntityPlayer oldPlayer = evt.getOriginal();
		EntityPlayer newPlayer = evt.getEntityPlayer();
		IFoodCap oldFoodCap = ScalingFeastAPI.accessor.getFoodCap(oldPlayer);
		IFoodCap newFoodCap = ScalingFeastAPI.accessor.getFoodCap(newPlayer);
		IStarvationTracker oldTracker = ScalingFeastAPI.accessor.getStarvationTracker(oldPlayer);
		IStarvationTracker newTracker = ScalingFeastAPI.accessor.getStarvationTracker(newPlayer);
		IFoodCapModifier oldMod = ScalingFeastAPI.accessor.getFoodCapModifier(oldPlayer);
		IFoodCapModifier newMod = ScalingFeastAPI.accessor.getFoodCapModifier(newPlayer);
		newFoodCap.deserializeNBT(oldFoodCap.serializeNBT());
		newTracker.deserializeNBT(oldTracker.serializeNBT());
		newMod.deserializeNBT(oldMod.serializeNBT());
		if(evt.isWasDeath())
		{
			FoodStats newFs = evt.getEntityPlayer().getFoodStats();
			FoodStats oldFs = evt.getOriginal().getFoodStats();
			
			AppleCoreAPI.mutator.setHunger(newPlayer, newFoodCap.getMaxFoodLevel(newMod));
			AppleCoreAPI.mutator.setSaturation(newPlayer, newFoodCap.getMaxFoodLevel(newMod) < 5 ? newFoodCap.getMaxFoodLevel(newMod) : 5);
			ScalingFeastAPI.mutator.capPlayerSaturation(newPlayer);
			
			short maxToLose = (short) ModConfig.foodCap.death.maxLossAmount;
			short hungerToLose = (short) ModConfig.foodCap.death.hungerLossOnDeath;
			
			if(maxToLose != 0)
			{
				newFoodCap.decreaseMax((short) maxToLose);
			}
			if(hungerToLose != 0)
			{
				if(oldFs.getFoodLevel() - hungerToLose >= 20)
				{
					AppleCoreAPI.mutator.setHunger(newPlayer, oldFs.getFoodLevel() - hungerToLose);
				}
			}
		}
		if(!evt.isWasDeath() || !ModConfig.foodCap.starve.doesFreqReset)
		{
			syncTracker(newPlayer);
		}
		syncCap(newPlayer);
		syncMod(newPlayer);
	}
	
	public static void sync(EntityPlayer player)
	{
		syncCap(player);
		syncTracker(player);
		syncMod(player);
		syncBloatedHunger(player);
		syncStarveExhaust(player);
	}
	
	public static void syncCap(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new FoodCapMessage(ScalingFeastAPI.accessor.getFoodCap(player)), (EntityPlayerMP) player);
	}
	
	public static void syncTracker(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new StarvationTrackerMessage(ScalingFeastAPI.accessor.getStarvationTracker(player)), (EntityPlayerMP) player);
	}
	
	public static void syncMod(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new FoodCapModifierMessage(ScalingFeastAPI.accessor.getFoodCapModifier(player)), (EntityPlayerMP) player);
	}
	
	public static void syncBloatedHunger(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new BloatedHungerMessage(ScalingFeastAPI.accessor.getBloatedHunger(player)), (EntityPlayerMP) player);
	}
	
	public static void syncStarveExhaust(EntityPlayer player)
	{
		PacketHandler.INSTANCE.sendTo(new StarveExhaustMessage(ScalingFeastAPI.accessor.getStarveExhaustionTracker(player)), (EntityPlayerMP) player);
	}
}
