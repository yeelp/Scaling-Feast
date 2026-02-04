package yeelp.scalingfeast.config.modules;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.FoodEfficiencyModifiers.FoodEfficiencyOperations;

public final class SFSOLCarrotConfigCategory {

	@Name("Enabled")
	@Comment("Set to true to enable the Spice Of Life: Carrot Edition module")
	public boolean enabled = false;

	@Name("Use Milestones")
	@Comment("Set to true to use regular milestones that increase max hunger as a reward.")
	public boolean useMilestones = true;

	@Name("Use Food Efficiency Milestones")
	@Comment("Set to true to use food efficiency milestones that alter a player's exhaustion increase rate")
	public boolean useFoodEfficiencyMilestones = true;

	@Name("Milestones")
	@Comment({
			"A list of pairs delimited by a colon, m:r, of milestones and milestone rewards.",
			"When a player eats m unique food items, they will gain r max hunger, in half shanks. m must be a positive integer and r must be a positive integer less than 32767.",
			"Values for r > 32767 will be brought inside these bounds modulo 32767. List entries that aren't of this form, or pairs containing negative values for either m or r will be silently ignored."})
	public String[] milestones = {
			"5:2",
			"10:2",
			"15:2",
			"20:2",
			"25:2",
			"30:2",
			"35:2",
			"40:2",
			"45:2",
			"50:2"};

	@Name("Food Efficiency Milestones")
	@Comment({
			"A list of pairs delimited by a colon, m:r, of milestones and milestone rewards.",
			"Identical to regular milestones, however instead of granting the player bonus hunger, these food efficiency milestones increase a player's food efficiency attribute by r when they eat m unique food items, which changes a player's exhaustion rate.",
			"Use positive values to DECREASE the rate of exhaustion, and use negative values to INCREASE the rate of exhaustion."})
	public String[] foodEfficiencyMilestones = {
			"20:0.05",
			"40:0.05",
			"60:0.05"};

	@Name("Reward Messages Above Hotbar?")
	@Comment("If true, Scaling Feast will display its reward messages above a player's hotbar. Else, it will display it in chat. If multiple reward messages are sent, only the last one is displayed in the hotbar if this is true.")
	public boolean rewardMsgAboveHotbar = false;
	
	@Name("Food Efficiency Milestone Reward Type")
	@Comment({"Set the type of reward for Food Efficiency Milestones",
		"STACK_ADDITIVELY - Multiplies the base Food Efficiency by the sum of this milestone reward plus any other Food Efficiency attribute modifiers. So, it stacks with other modifiers additively",
		"STACK_MULTIPLICATIVELY - Multiplies the base Food Efficiency by this milestone reward independently of other modifiers. So, it stacks with other modifiers multiplicatively."})
	public FoodEfficiencyOperations foodEfficiencyMilestoneType = FoodEfficiencyOperations.STACK_MULTIPLICATIVELY;
}
