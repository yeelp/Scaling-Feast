package yeelp.scalingfeast.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.util.SOLCarrotMilestone;

/**
 * Helper class for Spice of Life: Carrot Edition integration.
 * Uses reflection.
 * @author Yeelp
 *
 */
public final class SOLCarrotHelper
{
	private static Class sol;
	private static Class prog;
	private static boolean enabled;
	private static Queue<SOLCarrotMilestone> milestones;
	private static int rewardMsgs = 8;
	private static Random rand;
	/**
	 * Initialize this module
	 */
	public static void init()
	{
		if(ScalingFeast.hasSolCarrot)
		{
			try 
			{
				rand = new Random();
				parseMilestones();
				sol = Class.forName("com.cazsius.solcarrot.tracking.FoodList");
				prog = Class.forName("com.cazsius.solcarrot.tracking.ProgressInfo");
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
	 * @return true if Scaling Feast found Spice of Life: Carrot Edition.
	 */
	public static boolean isEnabled()
	{
		return enabled;
	}
	/**
	 * Get the length of the FoodList for this player, only counting food items that should count towards milestones.
	 * @param player the player to get the FoodList of.
	 * @return The length of the FoodList for this player.
	 * @throws ModuleNotLoadedException If this module is not loaded.
	 */
	public static int getCountableFoodListLength(EntityPlayer player) throws ModuleNotLoadedException
	{
		if(enabled)
		{
			try 
			{
				Object foodList = sol.cast(sol.getMethod("get", EntityPlayer.class).invoke(null, player));
				Object progList = prog.cast(sol.getMethod("getProgressInfo").invoke(foodList));
				return ((Integer)(prog.getDeclaredField("foodsEaten").get(progList))).intValue();
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
			} catch (NoSuchFieldException e) 
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
	
	/**
	 * Get the reward for this player
	 * @param player the player to get the reward for
	 * @return a short indicating how much max hunger this player gets, in half shanks.
	 */
	public static short getReward(EntityPlayer player)
	{
		short reward = 0;
		try
		{
			int foodsEaten = getCountableFoodListLength(player);
			for(SOLCarrotMilestone milestone : milestones)
			{
				if(foodsEaten >= milestone.getAmountNeeded())
				{
					reward += milestone.getReward();
				}
			}
			return reward;
		} 
		catch (ModuleNotLoadedException e) 
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return 0;
		}
	}
	
	/**
	 * Has this player reached a milestone
	 * @param player player to check
	 * @return true if the player reached a milestone, false otherwise.
	 */
	public static boolean reachedMilestone(EntityPlayer player)
	{
		try
		{
			int foodsEaten = getCountableFoodListLength(player);
			for(SOLCarrotMilestone milestone : milestones)
			{
				if(foodsEaten == milestone.getAmountNeeded())
				{
					return true;
				}
			}
			return false;
		}
		catch(ModuleNotLoadedException e)
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Parse the milestones. If no valid milestones are parsed, SOLCarrotHelper falls back to a default list of
	 * "5:2", "10:2", "15:2", "20:2", "25:2", "30:2", "35:2", "40:2", "45:2", "50:2"
	 */
	public static void parseMilestones()
	{
		milestones = new LinkedList<SOLCarrotMilestone>();
		for(String str : ModConfig.modules.sol.milestones)
		{
			try
			{
				milestones.add(new SOLCarrotMilestone(str));
			}
			catch(IllegalArgumentException e)
			{
				//silently ignore
				continue;
			}
		}
		if(milestones.size() == 0)
		{
			ScalingFeast.warn("No milestones were parsed! Falling back to default...");
			parseMilestones("5:2", "10:2", "15:2", "20:2", "25:2", "30:2", "35:2", "40:2", "45:2", "50:2");
		}
	}
	
	private static void parseMilestones(String...strings)
	{
		for(String str : strings)
		{
			milestones.add(new SOLCarrotMilestone(str));
		}
	}
	
	/**
	 * Get a random reward splash number
	 * @return a random reward splash number from those available.
	 */
	public static int getRewardSplashNumber()
	{
		return rand.nextInt(rewardMsgs);
	}
	
	public static SOLCarrotMilestone getLastMilestoneReached(EntityPlayer player)
	{
		try 
		{
			int foodsEaten = getCountableFoodListLength(player);
			SOLCarrotMilestone lastMilestone = null;
			for(SOLCarrotMilestone milestone : milestones)
			{
				if(foodsEaten >= milestone.getAmountNeeded())
				{
					lastMilestone = milestone;
				}
				else
				{
					return lastMilestone;
				}
			}
			return lastMilestone;
		} 
		catch (ModuleNotLoadedException e) 
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return null;
		}
	}
}
