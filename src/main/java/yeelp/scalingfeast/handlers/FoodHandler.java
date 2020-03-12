package yeelp.scalingfeast.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.hunger.HungerEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.init.SFFood;
import yeelp.scalingfeast.util.ExtendedFoodStats;
import yeelp.scalingfeast.util.FoodStatsMap;

public class FoodHandler {
	
	
	//This list should help prevent a double eat from happening.
	private static final Set<UUID> playersEating = new HashSet<UUID>();
	
	@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled=false)
	public void onFoodStatsAddition(FoodEvent.FoodStatsAddition evt)
	{
		if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !evt.player.world.isRemote))
		{
			int foodLevelBefore = evt.player.getFoodStats().getFoodLevel();
			float satLevelBefore = evt.player.getFoodStats().getSaturationLevel();
			
			int foodLevelAdded = evt.foodValuesToBeAdded.hunger;
			float satLevelAdded = evt.foodValuesToBeAdded.getUnboundedSaturationIncrement();
			
			//Get the amount of excess hunger this food heals
			int hungerOverflow = ModConsts.VANILLA_MAX_HUNGER - (foodLevelBefore + foodLevelAdded);
			
			//Get the amount of excess saturation this food heals
			float satOverflow = ModConsts.VANILLA_MAX_SAT - (satLevelBefore + satLevelAdded);
			
			hungerOverflow = (hungerOverflow < 0 ? Math.abs(hungerOverflow) : 0);
			satOverflow = (satOverflow < 0 ? Math.abs(satOverflow) : 0);
			
			if((hungerOverflow > 0 || satOverflow > 0) && playersEating.contains(evt.player.getUniqueID()))
			{
				//Check if the player is in the map. If not, add them.
				if(!FoodStatsMap.hasPlayer(evt.player.getUniqueID()))
				{
					FoodStatsMap.addPlayer(evt.player.getUniqueID());
					ScalingFeast.logger.info("Player: " + evt.player.getName() + " with UUID "+ evt.player.getUniqueID() +" wasn't in the map! Adding them...");
				}
				ScalingFeast.logger.info(String.format("Player %s (UUID: %s), with foodstats (%d, %f) gained %d food and %f saturation. They get %d bonus food and %f bonus saturation.", evt.player.getName(), evt.player.getUniqueID(), evt.player.getFoodStats().getFoodLevel(), evt.player.getFoodStats().getSaturationLevel(), evt.foodValuesToBeAdded.hunger, evt.foodValuesToBeAdded.getUnboundedSaturationIncrement(), hungerOverflow, satOverflow));
				FoodStatsMap.addFoodStats(evt.player.getUniqueID(), hungerOverflow, satOverflow);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled=true)
	public void onPlayerTick(PlayerTickEvent evt)
	{
		if(evt.isCanceled())
		{
			return;
		}
		//If the player isn't in the food map, we'll add them in, but do nothing.
		else if(!FoodStatsMap.hasPlayer(evt.player.getUniqueID()))
		{
			FoodStatsMap.addPlayer(evt.player.getUniqueID());
			return;
		}
		else
		{
			if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !evt.player.world.isRemote))
			{
				int foodLevel = evt.player.getFoodStats().getFoodLevel();
				float satLevel = evt.player.getFoodStats().getSaturationLevel();
				int foodGiven = 0;
				float satGiven = 0;
				//Find out how much saturation we need. We won't give it to the player yet, we want to make sure this is done in the right order.
				float satNeeded = ModConsts.VANILLA_MAX_SAT - satLevel;
				if(satNeeded > 0)
				{
					satGiven = FoodStatsMap.consumeSat(evt.player.getUniqueID(), satNeeded);
				}
				//Find out how much food we need. We won't give it to the player yet, much like with saturation.
				int foodNeeded = ModConsts.VANILLA_MAX_HUNGER - foodLevel;
				if(foodNeeded > 0)
				{
					foodGiven = FoodStatsMap.consumeFood(evt.player.getUniqueID(), foodNeeded);
				}
				
				//Now, we'll give the player the food stats. We give the food first, then the saturation
				if(foodGiven > 0) evt.player.getFoodStats().setFoodLevel(foodLevel + foodGiven);
				if(satGiven > 0) evt.player.getFoodStats().setFoodSaturationLevel(satLevel + satGiven);
				if(foodGiven > 0 || satGiven > 0) ScalingFeast.info(String.format("Player: %s, UUID: %s, ExtraFood: %d, ExtraSat: %f, ExtraFoodCap: %d", evt.player.getName(), evt.player.getUniqueID(), FoodStatsMap.getExtraFoodLevel(evt.player.getUniqueID()), FoodStatsMap.getExtraSatLevels(evt.player.getUniqueID()), FoodStatsMap.getMaxFoodLevel(evt.player.getUniqueID())));
			}
		}
	}
	
	/* TODO Get a list of all food items during init. Keep a list of which ones are always edible normally. Then, set every food to be always edible.
	 * When LivingEntityUseItemEvent.Start is fired, check if the item is an instance of ItemFood. If so, check if this item is in the 'normally always edible' list.
	 * If so, continue. Else, Check if the player's food stats are full. If not, continue. Else, check if the player's extended food stats are full if not, continue.
	 * Else, cancel the eating.
	 */
	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = false)
	public void onItemUse(LivingEntityUseItemEvent.Start evt)
	{
		if(!evt.isCanceled() && evt.getEntity() instanceof EntityPlayer && evt.getItem().getItem() instanceof ItemFood)
		{
			EntityPlayer player = (EntityPlayer) evt.getEntity();
			ItemFood food = (ItemFood) evt.getItem().getItem();
			if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !player.world.isRemote))
			{
			//Find out if the player is allowed to eat that item
				boolean a = ScalingFeast.alwaysEdibleFoods.contains(food);
				boolean b = FoodStatsMap.hasPlayer(player.getUniqueID());
				boolean c = (b ? FoodStatsMap.getExtraFoodLevel(player.getUniqueID()) < FoodStatsMap.getMaxFoodLevel(player.getUniqueID()) : false);
				boolean d = player.getFoodStats().getFoodLevel() < ModConsts.VANILLA_MAX_HUNGER;
					
				//Allow eating on A | D | (B & C)
				if(a || d || (b && c))
				{
					if(!playersEating.contains(player.getUniqueID()))
					{
						playersEating.add(player.getUniqueID());
					}
				}
				else
				{
					evt.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled = false)
	public void onItemUse(LivingEntityUseItemEvent.Stop evt)
	{
		if(!evt.isCanceled() && evt.getEntity() instanceof EntityPlayer && evt.getItem().getItem() instanceof ItemFood)
		{
			EntityPlayer player = (EntityPlayer) evt.getEntity();
			if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !player.world.isRemote))
			{
				if(playersEating.contains(player.getUniqueID()))
				{
					playersEating.remove(player.getUniqueID());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Finish evt)
	{
		if(!evt.isCanceled() && evt.getEntity() instanceof EntityPlayer && evt.getItem().getItem() instanceof ItemFood)
		{
			EntityPlayer player = (EntityPlayer) evt.getEntity();
			if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !player.world.isRemote))
			{
				if(playersEating.contains(player.getUniqueID()))
				{
					playersEating.remove(player.getUniqueID());
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = false)
	public void onFoodEaten(FoodEvent.FoodEaten evt)
	{
		if(FMLCommonHandler.instance().getSide() == Side.SERVER || (FMLCommonHandler.instance().getSide() == Side.CLIENT && !evt.player.world.isRemote))
		{
			if(Item.getIdFromItem(evt.food.getItem()) == Item.getIdFromItem(SFFood.shank) && playersEating.contains(evt.player.getUniqueID()))
			{
				FoodStatsMap.increaseMax(evt.player.getUniqueID());
			}
		}
	}
}
