package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public class SFConfigHealthRegen {
	public enum RegenCriterion {
		VANILLA,
		VANILLA_LIKE,
		DISABLED;
	}
	
	@Name("Hunger Regen Type")
	@Comment({
			"Alters how natural regeneration works. Note all options except DISABLED still respect the naturalRegeneration gamerule.",
			"VANILLA - Scaling Feast leaves regen behaviour as is.",
			"VANILLA_LIKE - Scaling Feast extends Minecraft's conditions for natural regeneration. That is, natural regeneration will occur provided your hunger is at least <max hunger> - 2",
			"DISABLED - Scaling Feast will always prevent natural regeneration from hunger."})
	public RegenCriterion hungerRegen = RegenCriterion.VANILLA;

	@Name("Saturated Regen Type")
	@Comment({
			"Alters how saturated natural regeneration works. Note all options still respect the naturalRegeneration gamerule.",
			"VANILLA - Scaling Feast leaves regen behaviour as is.",
			"VANILLA_LIKE - Scaling Feast extends Minecraft's conditions for saturated regeneration. That is, saturated regen occurs if a player has non zero saturation and full hunger.",
			"DISABLED - Scaling Feast will always prevent saturated regeneration."})
	public RegenCriterion satRegen = RegenCriterion.VANILLA;
}
