package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;

public final class SFConfigExhaustionScaling {

	@Name("Allow Exhaustion Scaling")
	@Comment("If enabled, when a player breaks a block, the amount of exhaustion they receive is now dependent on how fast they broke the block. Breaking a block faster results in less exhaustion.")
	public boolean doExhaustionScaling = false;
	
	@Name("Base Exhaustion per Second")
	@Comment({"The amount of exhaustion gained after breaking a block is this value times the amount of seconds it took to break the block.",
		"Instaminning results in only receiving 1/20th of this value in exhaustion.",
		"The default value results in receiving the same amount of exhaustion in vanilla when breaking a block, but ONLY when instaminned."})
	@RangeDouble(min = 0.00001)
	public float baseExhaustionRate = 0.1f;
}
