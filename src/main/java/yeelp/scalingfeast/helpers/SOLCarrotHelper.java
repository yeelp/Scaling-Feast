package yeelp.scalingfeast.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.util.FoodEfficiencyMilestone;
import yeelp.scalingfeast.util.Milestone;
import yeelp.scalingfeast.util.SOLCarrotMilestone;

/**
 * Helper class for Spice of Life: Carrot Edition integration.
 * Uses reflection.
 * @author Yeelp
 *
 */
public final class SOLCarrotHelper
{
	private static Class<?> sol;
	private static Class<?> prog;
	private static boolean enabled;
	private static List<SOLCarrotMilestone> milestones;
	private static List<FoodEfficiencyMilestone> efficiencyMilestones;
	private static final int rewardMsgs = 8;
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
		return enabled && ModConfig.modules.sol.enabled;
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
			} 
			catch (NoSuchFieldException e) 
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
		return getReward(player, milestones, (n, r) -> (short) (r.shortValue() + n.shortValue()), (short) 0).shortValue();
	}
	
	/**
	 * Get the food efficiency modifier for this player.
	 * @param player
	 * @return a float for the new foodEfficiency modifier.
	 */
	public static float getEfficiencyModifier(EntityPlayer player)
	{
		return getReward(player, efficiencyMilestones, (n, r) -> r.floatValue() + n.floatValue(), 0.0f).floatValue();
	}
	
	/**
	 * Has this player reached a milestone
	 * @param player player to check
	 * @return true if the player just reached a milestone, false otherwise.
	 */
	public static boolean reachedMilestone(EntityPlayer player)
	{
		try
		{
			int foodsEaten = getCountableFoodListLength(player);
			return getMilestoneIndicesSatisfying(m -> foodsEaten == m.getTarget(), milestones).size() > 0;
		}
		catch(ModuleNotLoadedException e)
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Has this player reached all milestones?
	 * @param player
	 * @return True if they have, false otherwise.
	 */
	public static boolean reachedAllMilestones(EntityPlayer player)
	{
		try
		{
			int foodsEaten = getCountableFoodListLength(player);
			boolean m = ModConfig.modules.sol.useMilestones;
			boolean e = ModConfig.modules.sol.useFoodEfficiencyMilestones;
			boolean rm = getMilestoneIndicesSatisfying(milestone -> foodsEaten >= milestone.getTarget(), milestones).size() == milestones.size();
			boolean re = getMilestoneIndicesSatisfying(milestone -> foodsEaten >= milestone.getTarget(), efficiencyMilestones).size() == efficiencyMilestones.size();
			return (m || e) && (!e || re) && (!m || rm);
		}
		catch (ModuleNotLoadedException e)
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Has this player reached a milestone
	 * @param player player to check
	 * @return true if the player just reached a milestone, false otherwise
	 */
	public static boolean reachedFoodEfficiencyMilestone(EntityPlayer player)
	{
		try
		{
			int foodsEaten = getCountableFoodListLength(player);
			return getMilestoneIndicesSatisfying(m -> foodsEaten == m.getTarget(), efficiencyMilestones).size() > 0;
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
		milestones = new ArrayList<SOLCarrotMilestone>();
		efficiencyMilestones = new ArrayList<FoodEfficiencyMilestone>();
		if(ModConfig.modules.sol.useMilestones)
		{
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
				parseMilestones("regular", "5:2", "10:2", "15:2", "20:2", "25:2", "30:2", "35:2", "40:2", "45:2", "50:2");
			}
		}
		if(ModConfig.modules.sol.useFoodEfficiencyMilestones)
		{
			for(String str : ModConfig.modules.sol.foodEfficiencyMilstones)
			{
				try
				{
					efficiencyMilestones.add(new FoodEfficiencyMilestone(str));
				}
				catch(IllegalArgumentException e)
				{
					//silently ignore
					continue;
				}
			}
			if(efficiencyMilestones.size() == 0)
			{
				ScalingFeast.warn("No efficiency milestones were parsed! Falling back to default...");
				parseMilestones("foodEfficiency", "20:0.05", "40:0.05", "60:0.05");
			}
		}
		
	}
	
	private static void parseMilestones(String type, String...strings)
	{
		switch(type)
		{
			case "regular":
				for(String str : strings)
				{
					milestones.add(new SOLCarrotMilestone(str));
				}
				return;
			case "foodEfficiency":
				for(String str : strings)
				{
					efficiencyMilestones.add(new FoodEfficiencyMilestone(str));
				}
				return;
			default:
				ScalingFeast.debug("Invalid type of milestone to parse: "+type);
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
	
	/**
	 * Get the last milestone reached by this player
	 * @param player
	 * @return the last SolCarrotMilestone reached by this player, or null if none was reached yet.
	 */
	@Nullable
	public static SOLCarrotMilestone getLastRegularMilestoneReached(EntityPlayer player)
	{
		try 
		{
			int foodsEaten = getCountableFoodListLength(player);
			return milestones.get(getMilestoneIndicesSatisfying(m -> foodsEaten >= m.getTarget(), milestones).getLast());
		} 
		catch (ModuleNotLoadedException e) 
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Get the last efficiency milestone reached by this player
	 * @param player
	 * @return the last FoodEfficiencyMilestone reached by this player, or null if none was reached yet.
	 */
	@Nullable
	public static FoodEfficiencyMilestone getLastEfficiencyMilestoneReached(EntityPlayer player)
	{
		try
		{
			int foodsEaten = getCountableFoodListLength(player);
			return efficiencyMilestones.get(getMilestoneIndicesSatisfying (m -> foodsEaten >= m.getTarget(), efficiencyMilestones).getLast());
		}
		catch (ModuleNotLoadedException e) 
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return null;
		}
	}
	
	private static <T extends Milestone> Deque<Integer> getMilestoneIndicesSatisfying(Predicate<T> p, List<T> lst)
	{
		ArrayDeque<Integer> deque = new ArrayDeque<Integer>();
		for(int i = 0; i < lst.size(); i++)
		{
			if(p.test(lst.get(i)))
			{
				deque.addFirst(i);
			}
		}
		return deque;
	}
	
	private static <T extends Milestone, N extends Number> Number getReward(EntityPlayer player, List<T> lst, BinaryOperator<Number> combiningFunc, Number accumulator)
	{
		try
		{
			List<N> reward = new ArrayList<N>();
			int foodsEaten = getCountableFoodListLength(player);
			for(int i : getMilestoneIndicesSatisfying(m -> foodsEaten >= m.getTarget(), lst))
			{
				accumulator = combiningFunc.apply(lst.get(i).getReward(), accumulator);
			}
			return accumulator;
		}
		catch (ModuleNotLoadedException e) 
		{
			ScalingFeast.err("Scaling Feast expected Spice of Life: Carrot Edition to be loaded, but it wasn't! This doesn't make any sense!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return 0;
		}
	}
}
