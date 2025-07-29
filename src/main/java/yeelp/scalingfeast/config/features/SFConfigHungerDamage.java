package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import yeelp.scalingfeast.features.FilterListType;

public final class SFConfigHungerDamage {
	@Name("Hunger Damage Multiplier")
	@Comment("When a player is attacked by a non-player entity, they will lose some hunger proportional to the damage dealt. This value determines this proportion (For example, setting to 1.0 means all damage inflicted is deducted from a player's food stats, 0.5 would mean only half that damage will be deducted from a player's food stats. 2.0 would do double damage etc.). If set to 0, this feature is disabled.")
	@RangeDouble(min = 0.0)
	public double hungerDamageMultiplier = 0.0;
	
	@Name("Dimension List")
	@Comment("The list of dimensions this feature should or should not apply in")
	public String[] dimList = {};
	
	@Name("Dimension List Type")
	@Comment({"The type of list for filtering the dimensions this feature applies in",
		"BLACKLIST - Features doesn't apply in these dimensions",
		"WHITELIST - Features apply in these dimensions"
	})
	public FilterListType listType = FilterListType.BLACKLIST;
}
