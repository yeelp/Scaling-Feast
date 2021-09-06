package yeelp.scalingfeast.config.modules;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public final class SFModuleConfigCategory {
	@Name("Spice Of Life: Carrot Edition")
	@Comment("Tweak Spice Of Life: Carrot Edition integration")
	public SFSOLCarrotConfigCategory sol = new SFSOLCarrotConfigCategory();
	
	@Name("Spice Of Life")
	@Comment("Tweak Spice Of Life integration")
	public SFSpiceOfLifeConfigCategory spiceoflife = new SFSpiceOfLifeConfigCategory();
}
