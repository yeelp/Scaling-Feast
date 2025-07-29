package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import yeelp.scalingfeast.lib.RespawningStats;

public final class SFConfigDeath {
	@Name("Max Hunger Cap Lost on Death")
	@Comment("If not set to zero, this field indicates how much of your maximum hunger you lose upon death. This can not take a player's max hunger below maxLossLowerBound.")
	@RangeInt(min = 0, max = Short.MAX_VALUE)
	public int maxLossAmount = 0;

	@Name("Max Hunger Cap Loss Lower Bound")
	@Comment("A player's max hunger will never go below this value via death penalties.")
	@RangeInt(min = 1)
	public int maxLossLowerBound = 1;
	
	@Name("Respawning Food Stats")
	@Comment({"Determines what food stats a player respawns with on death.",
			  "MAX_BOTH - The player respawns with their stats maxed out.",
			  "MAX_HUNGER - The player respawns with max hunger, but the default vanilla saturation.",
			  "PERSIST - The player respawns with the same stats they had when they died.",
			  "VANILLA - The player respawns with the vanilla defaults: 20 hunger, 5 saturation.",
			  "STARTING_AMOUNT - The player respawns with the amount of hunger and saturation they first spawned with."})
	public RespawningStats respawningStats = RespawningStats.MAX_HUNGER;

	@Name("Food Stats Penalty on Death")
	@Comment({"If not set to zero, this field indicates how much food values you lose on death. This deducts from saturation first, then hunger.",
		      "While not required, using this while respawningStats is set to PERSIST yields the best results."})
	@RangeInt(min = 0, max = Short.MAX_VALUE)
	public float hungerLossOnDeath = 0;
}
