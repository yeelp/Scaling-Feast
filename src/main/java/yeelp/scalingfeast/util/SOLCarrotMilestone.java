package yeelp.scalingfeast.util;

/**
 * A simple container for storing Spice of Life: Carrot Edition Milestones
 * @author Yeelp
 *
 */
public class SOLCarrotMilestone extends Milestone
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
		super(milestone);
	}
	
	@Override
	void setReward(String str)
	{
		int temp = Integer.parseInt(str);
		if(temp <= 0)
		{
			throw new IllegalArgumentException(temp +" isn't a valid milestone reward amount!");
		}
		else
		{
			this.milestoneReward = (short)(Math.abs(temp) % 32768);
		}
	}
	
	
	@Override
	public Short getReward()
	{
		return this.milestoneReward;
	}
}
