package yeelp.scalingfeast.helpers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import squeek.spiceoflife.foodtracker.FoodEaten;
import squeek.spiceoflife.foodtracker.FoodHistory;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;

/**
 * Spice of Life helper class for Spice of Life integration.
 * @author Yeelp
 *
 */
public class SpiceOfLifeHelper 
{	
	
	private static boolean enabled;
	private static short penalty;
	private static int requiredAmount;
	private static boolean useFoodGroups;
	/**
	 * Initialize this module
	 */
	public static void init()
	{
		if(ScalingFeast.hasSpiceOfLife)
		{
			enabled = true;
			penalty = (short)ModConfig.modules.spiceoflife.penalty;
			requiredAmount = ModConfig.modules.spiceoflife.uniqueRequired;
			useFoodGroups = ModConfig.modules.spiceoflife.useFoodGroups;
		}
		else
		{
			ScalingFeast.err("ScalingFeast encountered a problem trying to find Spice of Life. Check your installation, and try again.");
			enabled = false;
		}
	}
	
	public static void update()
	{
		penalty = (short)ModConfig.modules.spiceoflife.penalty;
		requiredAmount = ModConfig.modules.spiceoflife.uniqueRequired;
		useFoodGroups = ModConfig.modules.spiceoflife.useFoodGroups;
	}
	
	/**
	 * Is this module enabled?
	 * @return true if initialized and enabled, false otherwise
	 */
	public static boolean isEnabled()
	{
		return enabled && ModConfig.modules.spiceoflife.enabled;
	}
	
	/**
	 * Get a list of foods eaten by a player
	 * @param player the player to target
	 * @return a List of FoodEaten objects containing foods eaten by the specified player.
	 * @throws ModuleNotLoadedException If this module isn't loaded yet.
	 */
	public static List<FoodEaten> getEatenFoodsFor(EntityPlayer player) throws ModuleNotLoadedException
	{
		if(enabled)
		{
			FoodHistory history = FoodHistory.get(player);
			return history.getHistory();
		}
		else
		{
			throw new ModuleNotLoadedException("Spice of Life Module not loaded!");
		}
	}
	
	/**
	 * Gets a Set of unique foods eaten by this player in their FoodHistory
	 * @param player the player to get unique foods for
	 * @return A Set of Item objects, containing unique foods eaten by the specified player
	 * @throws ModuleNotLoadedException If this module hasn't been loaded
	 */
	public static Set<Item> getUniqueFoodsFor(EntityPlayer player) throws ModuleNotLoadedException
	{
		if(enabled)
		{
			HashSet<Item> foods = new HashSet<Item>();
			for(FoodEaten f : getEatenFoodsFor(player))
			{
				foods.add(f.itemStack.getItem());
			}
			return foods;
		}
		else
		{
			throw new ModuleNotLoadedException("Spice of Life Module not enabled!");
		}
	}
	
	/**
	 * Get the max hunger penalty for a player
	 * @param player the player to get the max hunger penalty for
	 * @return the max hunger penalty for the specified player, or 0 if the player has no penalty
	 */
	public static short getPenalty(EntityPlayer player)
	{
		Set<?> entries = new HashSet<Object>();
		int historyLength = 0;
		if(useFoodGroups)
		{
			entries = FoodHistory.get(player).getDistinctFoodGroups();
		}
		else
		{
			try 
			{
				entries = SpiceOfLifeHelper.getUniqueFoodsFor(player);
			} 
			catch (ModuleNotLoadedException e) 
			{
				ScalingFeast.err("Scaling Feast expected Spice of Life to be loaded, but it wasn't! This doesn't make any sense!");
				ScalingFeast.err(Arrays.toString(e.getStackTrace()));
				return 0;
			}
		}
		try
		{
			historyLength = getEatenFoodsFor(player).size();
		}
		catch(ModuleNotLoadedException e)
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return 0;
		}
		int diff = requiredAmount - entries.size();
		if(diff <= 0 || historyLength < requiredAmount)
		{
			return 0;
		}
		else
		{
			return (short) (-1*(diff)*penalty);
		}
	}
	
	/**
	 * What tooltip should be added to a ItemStack?
	 * @param stack the stack to query
	 * @param player the player receiving the tooltip
	 * @return {@link ToolTipType#GOOD} if eating this food item will increase max hunger, {@link ToolTipType#BAD} if it will reduce max hunger, or null, if no change will occur.
	 */
	public static ToolTipType getToolTipType(ItemStack stack, EntityPlayer player)
	{
		short currPenalty = getPenalty(player);
		FoodHistory history = FoodHistory.get(player);
		FoodHistory dummy = new FoodHistory();
		for(FoodEaten f : history.getHistory())
		{
			dummy.addFood(f);
		}
		
		dummy.addFood(new FoodEaten(stack, player));
		int amount = 0;
		if(useFoodGroups)
		{
			amount = dummy.getDistinctFoodGroups().size();
		}
		else
		{
			HashSet<Item> entries = new HashSet<Item>();
			for(FoodEaten f : dummy.getHistory())
			{
				entries.add(f.itemStack.getItem());
			}
			amount = entries.size();
		}
		int diff = requiredAmount - amount;
		if(diff <= 0 || dummy.getHistory().size() < requiredAmount)
		{
			return null;
		}
		else
		{
			if((-1*diff*penalty) < currPenalty)
			{
				return ToolTipType.BAD;
			}
			else if((-1*diff*penalty) > currPenalty)
			{
				return ToolTipType.GOOD;
			}
			else
			{
				return null;
			}
		}
	}
	/**
	 * Tooltip types for SpiceOfLife module.
	 * @author Yeelp.
	 *
	 */
	public enum ToolTipType
	{
		GOOD,
		BAD;
	}
}
