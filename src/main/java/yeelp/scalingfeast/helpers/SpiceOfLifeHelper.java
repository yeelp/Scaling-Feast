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
	
	/**
	 * Is this module enabled?
	 * @return true if enabled, false otherwise
	 */
	public static boolean isEnabled()
	{
		return enabled;
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
				ScalingFeast.info(f.itemStack.getItem().getUnlocalizedName());
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
		ScalingFeast.info(String.format("MIN: %d, HAVE: %d", requiredAmount, entries.size()));
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
	 * Should a warning tooltip be added to a ItemStack?
	 * @param stack the stack to query
	 * @param player the player receiving the tooltip
	 * @return true if eating the food item would reduce max hunger.
	 */
	public static boolean shouldAddToolTip(ItemStack stack, EntityPlayer player)
	{
		short currPenalty = getPenalty(player);
		FoodHistory history = FoodHistory.get(player).clone();
		
		history.addFood(new FoodEaten(stack, player));
		Set<?> entries = new HashSet<Object>();
		if(useFoodGroups)
		{
			entries = history.getDistinctFoodGroups();
		}
		else
		{
			for(FoodEaten f : history.getHistory())
			{
				entries.add(f.itemStack.getItem());
			}
		}
		int diff = requiredAmount - entries.size();
		if(diff <= 0 || history.getHistory().size() < requiredAmount)
		{
			return false;
		}
		else
		{
			return (-1*diff*penalty) < currPenalty;
		}
	}
}
