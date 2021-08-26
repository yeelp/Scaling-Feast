package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public final class SFConfigStarvation {
	@Name("Decrease Amount on Starvation")
	@Comment("The amount of max hunger to lose when starving, in half shanks. If set to 0, max hunger will never drop when starving")
	@RangeInt(min = 0, max = Short.MAX_VALUE)
	public int starveLoss = 2;

	@Name("Starvation Loss Lower Bound")
	@Comment("When losing hunger due to starvation, a player's max hunger will never get set below this value. If a player's max hunger is already below this value, starving will not punish the player")
	@RangeInt(min = 1, max = Short.MAX_VALUE)
	public int starveLowerCap = 1;

	@Name("Starvation Loss Frequency")
	@Comment("After getting damaged by starvation this many times, a player's max hunger will drop")
	@RangeInt(min = 1, max = Short.MAX_VALUE)
	public int lossFreq = 3;

	@Name("Frequency Reset")
	@Comment({
			"Should the frequency counter reset upon gaining hunger?",
			"If true, the frequency counter resets, and if false, it will not.",
			"For example, if the frequency count is set to 3 and this field is set to true,",
			"then whenever a player takes starvation damage 3 times, no matter how infrequent or spread apart, or if they've eaten any food in between, they still lose max hunger"})
	public boolean doesFreqReset = true;

	@Name("Reset Counter For Hearty Shank?")
	@Comment("If true, the frequency counter will reset whenever a player eats a Hearty Shank. If Frequency Reset is true, this does nothing, as eating any food already resets the counter.")
	public boolean shankResetsCounter = true;

	@Name("Frequency Reset on Penalty")
	@Comment("Should the frequency counter for a player be reset when they lose max hunger?")
	public boolean doesFreqResetOnStarve = true;

	@Name("Dynamic Starvation")
	@Comment("If true, Scaling Feast will remember how much exhaustion a player has received since going to zero hunger. Then, Scaling Feast will increase starvation damage proprtional to the amount of food points a player would have lost if they weren't starving. In addition, a player's starvation tracker, as described in the other settings here, will be increased multiple times in accordance to the amount of extra starvation damage received.")
	public boolean doDynamicStarvation = false;
	
	@Name("Dynamic Starvation is Unblockable")
	@Comment({"If true, bonus damage caused by dynamic starvation is completely unblockable. Mods that prevent starvation damage will not be able to prevent this bonus damage.",
			  "Scaling Feast uses a different death message to prevent confusion"})
	public boolean dynamicStarvationUnblockable = false;

	@Name("Bonus Starvation Damage Multiplier")
	@Comment("When starving with dynamic starvation enabled, this is the amount of bonus damage to do, in half hearts, per food point lost via exhaustion")
	@RangeInt(min = 1)
	public int bonusStarveDamageMult = 1;
}
