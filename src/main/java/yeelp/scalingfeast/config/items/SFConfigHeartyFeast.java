package yeelp.scalingfeast.config.items;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public final class SFConfigHeartyFeast {
	@Name("Hearty Feast Restoration Cap")
	@Comment("This is the maximum value the Hearty Feast will restore. If set to -1, there is no limit. If set to 0, the Hearty Feast won't restore anything.")
	@RangeInt(min = -1)
	public int heartyFeastCap = -1;
	
	@Name("Hearty Feast Effect Duration")
	@Comment("Hearty Feast will grant an Iron Stomach I effect for the specified duration (in ticks; 20 ticks per second) when a slice is eaten. Set to 0 to have no effect be applied.")
	public int heartyFeastEffectDuration = 1200;
}
