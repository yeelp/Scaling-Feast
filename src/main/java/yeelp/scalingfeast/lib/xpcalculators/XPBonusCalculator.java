package yeelp.scalingfeast.lib.xpcalculators;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ScalingFeast;

public interface XPBonusCalculator {

	final class XPMilestone implements Comparable<XPMilestone> {
		private final int target;
		private final double reward;

		XPMilestone(String milestone) {
			String[] args = milestone.split(":");
			this.target = Integer.parseInt(args[0]);
			this.reward = Double.parseDouble(args[1]);
		}
		
		/**
		 * Has the passed value exceeded the target threshold
		 * @param val value to check
		 * @return true if yes, false if no.
		 */
		boolean reachedTarget(int val) {
			return val >= this.target;
		}

		/**
		 * @return the target
		 */
		int getTarget() {
			return this.target;
		}

		/**
		 * @return the reward
		 */
		double getReward() {
			return this.reward;
		}

		@Override
		public int compareTo(XPMilestone o) {
			return Integer.compare(this.target, o.getTarget());
		}
	}
	
	static List<XPMilestone> createNewListOfMilestones(String[] config) {
		if(config.length == 0) {
			return ImmutableList.of();
		}
		Predicate<String> p = Pattern.compile("^\\d+:\\d+(\\.\\d+)?$").asPredicate();
		List<XPMilestone> lst = Arrays.stream(config).filter(p).map(XPMilestone::new).sorted().collect(Collectors.toList());
		Arrays.stream(config).filter(p.negate()).forEach((s) -> ScalingFeast.warn(s + " isn't a valid xp milestone, or is incorrectly formatted!"));
		return lst;
	}

	/**
	 * Apply a new XP bonus to the player
	 * @param player the player
	 */
	void applyXPBonus(EntityPlayer player);

	/**
	 * Set a new list of milestones
	 * @param lst list of milestones
	 */
	void setMilestoneList(List<XPMilestone> lst);
}
