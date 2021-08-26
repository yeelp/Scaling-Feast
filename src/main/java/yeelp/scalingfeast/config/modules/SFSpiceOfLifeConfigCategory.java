package yeelp.scalingfeast.config.modules;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public class SFSpiceOfLifeConfigCategory extends SFAbstractModuleConfig {

	@Name("Enabled")
	@Comment("Set to true to enable the Spice Of Life module. This module will only work when NOT using time based queues or hunger based queues in Spice of Life")
	public boolean enabled = false;

	@Name("Use Food Groups")
	@Comment("Should Scaling Feast check food groups in a player's food history instead of individual food items? Must have food groups defined in Spice Of Life")
	public boolean useFoodGroups = false;

	@Name("Required Amount")
	@Comment("How many unique entries must be found in a player's food history to prevent punishing them. Should be less than or equal to Spice of Life's food history length")
	@RangeInt(min = 1)
	public int uniqueRequired = 5;

	@Name("Penalty")
	@Comment({
			"If the number of unique entires in a player's food history is less than Required Amount, that player will lose this much max hunger for every unique entry missing.",
			"For example, if a player has 3 unique entires and the required amount is 5, they will lose (5-3)*(penalty) max hunger"})
	@RangeInt(min = 1, max = Short.MAX_VALUE)
	public int penalty = 2;
	
	@Override
	public boolean getEnabledEntry() {
		return this.enabled;
	}
}
