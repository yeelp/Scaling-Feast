package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.scalingfeast.lib.RegenCriterion.Hunger;
import yeelp.scalingfeast.lib.RegenCriterion.Saturation;

public final class SFConfigHealthRegen {

	@Name("Hunger Regen Type")
	@Comment({
			"Alters how natural regeneration works. Note all options except DISABLED still respect the naturalRegeneration gamerule.",
			"VANILLA - Scaling Feast leaves regen behaviour as is.",
			"MAX_MINUS_TWO - Scaling Feast does one possible extension of Minecraft's conditions for natural regeneration: natural regeneration will occur provided your hunger is at least <max hunger> - 2",
			"NINETY_PERCENT - Scaling Feast does one possible extension of Minecraft's conditions for natural regeneration: natural regeneration will only occur if you have at least 90% of your max hunger, rounded down.",
			"DISABLED - Scaling Feast will always prevent natural regeneration from hunger. Saturated regen can still occur if allowed."})
	public Hunger hungerRegen = Hunger.VANILLA;

	@Name("Saturated Regen Type")
	@Comment({
			"Alters how saturated natural regeneration works. Note all options still respect the naturalRegeneration gamerule.",
			"VANILLA - Scaling Feast leaves regen behaviour as is.",
			"          NOTE: Since saturated regen takes precedence over natural regen, a player will always get saturated regen with this option if they have at least 20 hunger and some saturation, no matter what the hungerRegen is.",
			"FULL - Scaling Feast extends Minecraft's conditions for saturated regeneration. That is, saturated regen only occurs if a player has non zero saturation and full hunger.",
			"DISABLED - Scaling Feast will always prevent saturated regeneration. Regular hunger regen will attempt to occur in its stead."})
	public Saturation satRegen = Saturation.VANILLA;
}
