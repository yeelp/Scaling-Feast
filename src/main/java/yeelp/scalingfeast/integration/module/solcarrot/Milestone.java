package yeelp.scalingfeast.integration.module.solcarrot;

/**
 * Base class for milestones. Used for SOL:Carrot integration
 * 
 * @author Yeelp
 * @param <N> The numerical rewards type
 *
 */
public abstract class Milestone<N extends Number> {
	private int target;

	/**
	 * Build a new milestone
	 * 
	 * @param milestone a string in the form {@code "m:r"} denoting the milestone
	 *                  target, {@code m} and the milestone reward {@code r}.
	 * @throws NumberFormatException    if the reward or target can't be parsed
	 *                                  properly
	 * @throws IllegalArgumentException if the reward or target isn't a valid amount
	 *                                  for this milestone.
	 */
	Milestone(String milestone) throws NumberFormatException, IllegalArgumentException {
		String[] arr = milestone.split(":");
		if(arr.length != 2) {
			throw new IllegalArgumentException(milestone + " isn't a valid milestone!");
		}
		try {
			this.target = Integer.parseInt(arr[0]);
			this.setReward(arr[1]);
			if(this.target <= 0) {
				throw new IllegalArgumentException(this.target + " isn't a valid amount for a milestone!");
			}
		}
		catch(NumberFormatException e) {
			throw e;
		}
	}

	/**
	 * Get the amount needed for this milestone
	 * 
	 * @return the amount needed for this milestone
	 */
	public final int getTarget() {
		return this.target;
	}

	abstract void setReward(String str);

	/**
	 * Get the reward amount for reaching this milestone
	 * 
	 * @return the reward amount
	 */
	public abstract N getReward();
}
