package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.scalingfeast.lib.worldgen.xpcalculators.XPBonusType;

public final class SFConfigXPBonuses {
	@Name("Food Efficiency Bonus")
	@Comment({
			"Enable and set Food Efficiency Bonus.",
			"When set, a player's Food Efficiency attribute will change depending on XP. The rewards they get can be configured in Food Efficiency XP Rewards.",
			"NONE - Food Efficiency Bonus will not be given to any players, effectively disabling this feature.",
			"LEVEL - Scaling Feast will compare a player's level (so a value of 2 in the rewards would correspond to level 2) against the entries in Food Efficiency XP Rewards when calculating the attribute value.",
			"AMOUNT - Scaling Feast will compare a player's XP total (so a value of 34 corresponds to level 2) against the entries in Food Efficiency XP Rewards when calculating the attribute value."})
	public XPBonusType efficiencyXPBonus = XPBonusType.NONE;

	@Name("Max Hunger Bonus")
	@Comment({
			"Enable and set Max Hunger Bonus.",
			"When set, a player's max hunger attribute will change depending on XP. The rewards they get can be configured in Max Hunger XP Rewards.",
			"NONE - Max Hunger Bonus will not be given to any players, effectively disabling this feature.",
			"LEVEL - Scaling Feast will compare a player's level (so a value of 2 in the rewards would correspond to level 2) against the entries in Max Hunger XP Rewards when calculating the attribute value.",
			"AMOUNT - Scaling Feast will compare a player's XP total (so a value of 34 corresponds to level 2) against the entries in Max Hunger XP Rewards when calculating the attribute value."})
	public XPBonusType maxHungerXPBonus = XPBonusType.NONE;

	@Name("Food Efficiency XP Rewards")
	@Comment({
			"A list of values x:b, where x is the player's XP, and b is the Food Efficiency Bonus (or penalty, can set negative values) this player receives.",
			"A player will get ALL bonus that their XP threshold surpasses. The way the XP argument is interpreted is set in the Food Efficiency Bonus option."})
	public String[] efficiencyRewards = {};

	@Name("Max Hunger XP Rewards")
	@Comment({
			"A list of values x:b, where x is the player's XP, and b is the Max Hunger Bonus (or penalty, can set negative values) this player receives.",
			"A player will get ALL bonus that their XP threshold surpasses. The way the XP argument is interpreted is set in the Max Hunger Bonus option."})
	public String[] maxHungerRewards = {};
}
