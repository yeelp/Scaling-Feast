package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public class SFConfigDeath {
	@Name("Max Hunger Cap Lost on Death")
	@Comment("If not set to zero, this field indicates how much of your maximum hunger you lose upon death. Can not go below maxLossLowerBound.")
	@RangeInt(min = 0, max = Short.MAX_VALUE)
	public int maxLossAmount = 0;

	@Name("Max Hunger Cap Loss Lower Bound")
	@Comment("A player's max hunger will never go below this value via death penalties.")
	@RangeInt(min = 1)
	public int maxLossLowerBound = 1;
	
	@Name("Should Extended Food Stats Persist on Death?")
	@Comment("If true, a player's extended food stats will be preserved on death. If false, a player's hunger is set to the maximum and saturation set to 5, or max hunger, whichever is less")
	public boolean persistStats = false;

	@Name("Food Stats Penalty on Death")
	@Comment({"If not set to zero, this field indicates how much food values you lose on death. This deducts from saturation first, then hunger.",
		      "While not required, using this while persistStats is true yields the best results."})
	@RangeInt(min = 0, max = Short.MAX_VALUE)
	public float hungerLossOnDeath = 0;
}
