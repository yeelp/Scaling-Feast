package yeelp.scalingfeast.util;

import squeek.applecore.api.AppleCoreAPI;

/**
 * A simple container for storing Spice of Life: Carrot Edition Milestones
 * @author Yeelp
 *
 */
public class SOLCarrotMilestone 
{
	private int milestoneTarget;
	private short milestoneReward; 
	/**
	 * Create a new SOLCarrotMilestone
	 * @param milestone A String of the form "m:r", where:
	 * 
	 * <p>- m represents an integer satisfying 0 < m and denotes the amount of food
	 * eaten for this milestone
	 * 
	 * <p>- r represents an integer satisfying 0 < r < 32767 and denotes 
	 * the amount of max hunger awarded at this milestone. In other words, r must be a valid positive short
	 *
	 * @throws IllegalArgumentException if milestone isn't a valid string that can be parsed, or if either m or r are non-positive
	 * @throws NumberFormatException if the strings m and r can't be parsed as numbers.
	 */
	public SOLCarrotMilestone(String milestone) throws NumberFormatException, IllegalArgumentException
	{
		String[] arr = milestone.split(":");
		if(arr.length != 2)
		{
			throw new IllegalArgumentException(milestone + " isn't a valid milestone!");
		}
		try
		{
			this.milestoneTarget = Integer.parseInt(arr[0]);
			int temp = Integer.parseInt(arr[1]);
			if(milestoneTarget <= 0)
			{
				throw new IllegalArgumentException(milestoneTarget+ " isn't a valid amount for a milestone!");
			}
			else if(temp <= 0)
			{
				throw new IllegalArgumentException(temp +" isn't a valid milestone reward amount!");
			}
			else
			{
				this.milestoneReward = (short)(Math.abs(temp) % 32768);
			}
		}
		catch(NumberFormatException e)
		{
			throw e;
		}
	}
	
	/**
	 * Get the amount needed for this milestone
	 * @return the amount needed for this milestone
	 */
	public int getAmountNeeded()
	{
		return this.milestoneTarget;
	}
	
	/**
	 * Get the reward amount in half shanks for a player's max hunger due to reaching this milestone
	 * @return the reward amount
	 */
	public short getReward()
	{
		return this.milestoneReward;
	}
}
