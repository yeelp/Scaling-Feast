package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import yeelp.scalingfeast.features.FilterListType;

public final class SFConfigBloatedOverflow {
	@Name("Allow Bloated Overflow Hunger")
	@Comment("If enabled and if a player eats a food item that grants more hunger than they need, Scaling Feast will grant the player the nearest level Bloated effect to match the amount of excess hunger a player ate. Potions must be registered for this to work.")
	public boolean doBloatedOverflow = false;

	@Name("Bloated Overflow Duration")
	@Comment("If Bloated Overflow is enabled, this dictates how long the potion should last in ticks. Note, there are 20 ticks per second.")
	@RangeInt(min = 1)
	public int bloatedOverflowDuration = 1800;
	
	@Name("Bloated Overflow Level Cap")
	@Comment("The highest level of Bloated the player can receive through bloated overflow. 0 corresponds to level 1 Bloated. Anything less than 0 will prevent the level from being capped at all.")
	public int bloatedLevelCap = -1;
	
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
