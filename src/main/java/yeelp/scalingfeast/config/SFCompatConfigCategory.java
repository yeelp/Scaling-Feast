package yeelp.scalingfeast.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public class SFCompatConfigCategory {
	@Name("Enable Compatibility Settings")
	@Comment({
			"If true, Scaling Feast will try to fire a RenderGameOverlay.Post event with ElementType.FOOD for mods that may use that event.",
			"Try this if other mods have their HUD components disappear when display style is set to OVERLAY"})
	public boolean shouldFirePost = true;
}
