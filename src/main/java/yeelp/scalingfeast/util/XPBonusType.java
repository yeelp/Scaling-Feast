package yeelp.scalingfeast.util;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ScalingFeast;

public enum XPBonusType implements FoodEfficiencyXPBonus, MaxHungerXPBonus {
	NONE(null),
	LEVEL((player) -> player.experienceLevel),
	AMOUNT((player) -> player.experienceTotal);

	private Function<EntityPlayer, Integer> f;
	private static final ArrayList<XPMilestone> FOOD_EFFICIENCY_BONUS = new ArrayList<XPMilestone>(20);
	private static final ArrayList<XPMilestone> MAX_HUNGER_BONUS = new ArrayList<XPMilestone>(20);
	private static final UUID FOOD_EFFICIENCY_BONUS_UUID = UUID.fromString("24bd97f0-392d-4c7f-8738-bfbaf34340f4");
	private static final UUID MAX_HUNGER_BONUS_UUID = UUID.fromString("157f01fc-05b7-4443-8654-1cbc1251f7ed");

	private static class XPMilestone implements Comparable<XPMilestone> {
		private final int target;
		private final double reward;

		XPMilestone(int target, double reward) {
			this.target = target;
			this.reward = reward;
		}

		boolean isValPastTarget(int val) {
			return val >= this.target;
		}

		double getReward() {
			return this.reward;
		}

		@Override
		public int compareTo(XPMilestone o) {
			return Integer.compare(this.target, o.target);
		}
	}

	private XPBonusType(Function<EntityPlayer, Integer> f) {
		this.f = f;
	}

	/**
	 * Update reward values from the config
	 */
	public static final void updateRewards() {
		updateRewards(ModConfig.features.xpBonuses.efficiencyRewards, FOOD_EFFICIENCY_BONUS);
		updateRewards(ModConfig.features.xpBonuses.maxHungerRewards, MAX_HUNGER_BONUS);
	}

	private static final void updateRewards(String[] config, ArrayList<XPMilestone> rewards) {
		rewards.clear();
		for(String s : config) {
			try {
				String[] args = s.split(":");
				rewards.add(new XPMilestone(Integer.parseInt(args[0]), Double.parseDouble(args[1])));
			}
			catch(NumberFormatException e) {
				ScalingFeast.err("Could not parse XP rewards! invalid entry: " + s);
			}
		}
		rewards.sort(null);
	}

	public static UUID getFoodEfficiencyBonusUUID() {
		return FOOD_EFFICIENCY_BONUS_UUID;
	}

	public static UUID getMaxHungerBonusUUID() {
		return MAX_HUNGER_BONUS_UUID;
	}

	@Override
	public double getFoodEfficiencyBonus(EntityPlayer player) {
		return getBonus(player, FOOD_EFFICIENCY_BONUS);
	}

	@Override
	public double getMaxHungerBonus(EntityPlayer player) {
		return getBonus(player, MAX_HUNGER_BONUS);
	}

	private double getBonus(EntityPlayer player, ArrayList<XPMilestone> lst) {
		if(this.f == null) {
			return 0.0;
		}
		int xp = this.f.apply(player);
		double reward = 0.0;
		for(XPMilestone m : lst) {
			if(m.isValPastTarget(xp)) {
				reward += m.getReward();
			}
			else {
				break;
			}
		}
		return reward;
	}
}
