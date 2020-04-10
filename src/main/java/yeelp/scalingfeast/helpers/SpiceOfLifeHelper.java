package yeelp.scalingfeast.helpers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import squeek.spiceoflife.foodtracker.FoodEaten;
import squeek.spiceoflife.foodtracker.FoodHistory;
import yeelp.scalingfeast.ScalingFeast;

/**
 * Spice of Life helper class for Spice of Life integration.
 * @author Yeelp
 *
 */
public class SpiceOfLifeHelper 
{	
	
	private static boolean enabled;
	/**
	 * Initialize this module
	 */
	public static void init()
	{
		if(ScalingFeast.hasSpiceOfLife)
		{
			enabled = true;
		}
		else
		{
			ScalingFeast.err("ScalingFeast encountered a problem trying to find Spice of Life. Check your installation, and try again.");
			enabled = false;
		}
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
	 * @return A Set of FoodEaten objects, containing unique foods eaten by the specified player
	 * @throws ModuleNotLoadedException If this module hasn't been loaded
	 */
	public static Set<FoodEaten> getUniqueFoodsFor(EntityPlayer player) throws ModuleNotLoadedException
	{
		if(enabled)
		{
			HashSet<FoodEaten> foods = new HashSet<FoodEaten>();
			for(FoodEaten f : getEatenFoodsFor(player))
			{
				foods.add(f);
			}
			return foods;
		}
		else
		{
			throw new ModuleNotLoadedException("Spice of Life Module not enabled!");
		}
	}
}
