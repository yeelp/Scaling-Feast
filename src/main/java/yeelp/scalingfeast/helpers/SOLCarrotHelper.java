package yeelp.scalingfeast.helpers;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ScalingFeast;

/**
 * Helper class for Spice of Life: Carrot Edition integration.
 * Uses reflection.
 * @author Yeelp
 *
 */
public final class SOLCarrotHelper 
{
	private static Class sol;
	private static boolean enabled;
	
	/**
	 * Initialize this module
	 */
	public static void init()
	{
		if(ScalingFeast.hasSolCarrot)
		{
			try 
			{
				sol = Class.forName("com.cazsius.solcarrot.tracking.FoodList");
				enabled = true;
			} 
			catch (ClassNotFoundException e) 
			{
				ScalingFeast.err("Scaling Feast encountered problems trying to find Spice of Life: Carrot Edition. Check your installation and try again.");
				e.printStackTrace();
				enabled = false;
			}
		}
		else
		{
			ScalingFeast.err("Spice of Life: Carrot Edition not found! This module will be disabled!");
			enabled = false;
		}
	}
	
	/**
	 * Has the module loaded successfully?
	 * @return true if Scaling Feast found Spice of Life: Carrot Edition, and found the SOLCarrotAPI class.
	 */
	public static boolean isEnabled()
	{
		return enabled;
	}
	/**
	 * Get the FoodList length for this player.
	 * @param player the player to get the FoodList of.
	 * @return The length of the FoodList for this player.
	 * @throws ModuleNotLoadedException If this module is not loaded.
	 */
	public static int getFoodListLength(EntityPlayer player) throws ModuleNotLoadedException
	{
		if(enabled)
		{
			try 
			{
				Object foodList = sol.cast(sol.getMethod("get", EntityPlayer.class).invoke(null, player));
				return ((Integer)(sol.getMethod("getEatenFoodCount").invoke(foodList))).intValue();
			} 
			catch (NoSuchMethodException e) 
			{
				e.printStackTrace();
			} 
			catch (SecurityException e)
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} 
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			return -1;
		}
		else
		{
			throw new ModuleNotLoadedException("Spice of Life: Carrot Edition Module not loaded!");
		}
	}
}
