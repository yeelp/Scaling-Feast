package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import yeelp.scalingfeast.lib.StarvationScaling;

public final class SFConfigStarvation {

	@Name("Tracker")
	@Comment({
			"Configure settings for Scaling Feast's Starvation Tracker.",
			"The Starvation Tracker tracks how many times a player starves.",
			"Starve too many times consecutively, and your max hunger will drop!"})
	public TrackerCategory tracker = new TrackerCategory();

	@Name("Counter")
	@Comment({
			"Configure settings for Scaling Feast's Starvation Counter.",
			"This counts how many times the player starves in a row, with the ability to inflict more and more damage every time they starve."})
	public CounterCategory counter = new CounterCategory();

	@Name("Dynamic Starvation")
	public DynamicCategory dynamic = new DynamicCategory();

	public final class TrackerCategory {
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

		@Name("Tracker Reset")
		@Comment({
				"Should the tracker count reset upon gaining hunger?",
				"If true, the frequency counter resets, and if false, it will not.",
				"For example, if the frequency count is set to 3 and this field is set to true,",
				"then whenever a player takes starvation damage 3 times, no matter how infrequent or spread apart, or if they've eaten any food in between, they still lose max hunger"})
		public boolean doesFreqReset = true;

		@Name("Reset Tracker For Hearty Shank?")
		@Comment("If true, the tracker will reset whenever a player eats a Hearty Shank. If Tracker Reset is true, this does nothing, as eating any food already resets the counter.")
		public boolean shankResetsCounter = true;

		@Name("Tracker Reset on Penalty")
		@Comment("Should the tracker for a player be reset when they lose max hunger?")
		public boolean doesFreqResetOnStarve = true;

	}

	public final class DynamicCategory {
		@Name("Dynamic Starvation")
		@Comment("If true, Scaling Feast will remember how much exhaustion a player has received since going to zero hunger. Then, Scaling Feast will increase starvation damage proprtional to the amount of food points a player would have lost if they weren't starving. In addition, a player's Starvation Tracker, as described in the Tracker settings, will be increased multiple times in accordance to the amount of extra starvation damage received.")
		public boolean doDynamicStarvation = false;

		@Name("Dynamic Starvation is Unblockable")
		@Comment({
				"If true, bonus damage caused by dynamic starvation is completely unblockable. Mods that prevent starvation damage will not be able to prevent this bonus damage.",
				"Scaling Feast uses a different death message to prevent confusion"})
		public boolean dynamicStarvationUnblockable = false;

		@Name("Bonus Starvation Damage Multiplier")
		@Comment("When starving with dynamic starvation enabled, this is the amount of bonus damage to do, in half hearts, per food point lost via exhaustion")
		@RangeInt(min = 1)
		public int bonusStarveDamageMult = 1;
	}

	public final class CounterCategory {
		@Name("Starvation Scaling")
		@Comment({
				"How Scaling Feast computes additional starvation damage per starve hit. Some functions may require additional arguments. Every value computed by this feature is added to the original damage, and the bonus damage is bounded below at 0.",
				"Here, x is the current number of times a player has taken starvation damage in a row without gaining food stats. a, b, c, and d are additional constants defined in this category.",
				"If at any time these functions are undefined, Scaling Feast just ignores it and deals 0 additional damage.",
				"NONE - No scaling is applied; this feature is effectively disabled.",
				"CONSTANT - Inflicts additional damage with the function: f(x) = d",
				"POLYNOMIAL - Inflicts additional damage with the function: f(x) = a(x + b)^c + d",
				"LOGARITHMIC - Inflicts additional damage with the function: f(x) = alog_b(x + c) + d. log_b is logarithm with base b.",
				"EXPONENTIONAL - Inflicts additional damage with the function: f(x) = ab^(cx+d)."})
		public StarvationScaling starveScaling = StarvationScaling.NONE;
		
		@Name("Starvation Scaling is unblockable")
		@Comment("If true, bonus damage caused by starvation scaling is unblockable. Mods that prevent starvation damage will not be able to prevent this bonus damage.")
		public boolean starvationScalingUnblockable = false;

		@Name("a")
		@Comment("The constant a for Starvation Scaling")
		public float a = 0;

		@Name("b")
		@Comment("The constant b for Starvation Scaling")
		public float b = 0;

		@Name("c")
		@Comment("The constant c for Starvation Scaling")
		public float c = 0;

		@Name("d")
		@Comment("The constant d for Starvation Scaling")
		public float d = 0;
	}

}
