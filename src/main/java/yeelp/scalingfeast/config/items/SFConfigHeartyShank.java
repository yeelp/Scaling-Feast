package yeelp.scalingfeast.config.items;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

public final class SFConfigHeartyShank {
	@Name("Hearty Shank Hunger Value")
	@Comment("The Food value of a Hearty Shank, in half shanks (i.e. 2 = one full hunger shank)")
	@RangeInt(min = 0)
	@RequiresMcRestart
	public int heartyShankFoodLevel = 4;

	@Name("Hearty Shank Saturation Modifier")
	@Comment("The saturation modifier for the Hearty Shank. This item's actual saturation level will be 2*this*<Hearty Shank Hunger Value>")
	@RangeDouble(min = 0)
	@RequiresMcRestart
	public double heartyShankSatLevel = 0.8;
	
	@Name("Increase Per Hearty Shank Eaten")
	@Comment("The increase in your total max hunger, in half shanks (i.e. 2 = one full hunger shank) per Hearty Shank eaten.")
	@RangeInt(min = 0, max = Short.MAX_VALUE)
	public int inc = 2;

	@Name("Hearty Shank Increase Cap")
	@Comment({
			"The maximum amount of bonus max hunger Hearty Shanks can provide.",
			"All net bonuses Hearty Shanks provide will be capped to this amount, and any 'excess' bonus max hunger granted is revoked.",
			"The Hearty Shank can still be consumed at this point, but won't increase max hunger.",
			"Set to -1 for no limit."})
	@RangeInt(min = -1)
	public int heartyShankCap = -1;
}
