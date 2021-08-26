package yeelp.scalingfeast.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import yeelp.scalingfeast.util.SaturationScaling;

public class SFGeneralConfigCategory {
	@Name("Global Cap")
	@Comment({
			"The highest extended hunger the player can have.",
			"Note that any players with an extended hunger value greater than this will be set to this cap",
			"This ignores vanilla's hunger level; it ONLY affects the additional amount of hunger you can gain from Scaling Feast.",
			"If set to -1, this cap is ignored.",
			"Changes in game will be observed on the next player tick"})
	@RangeInt(min = -1, max = Short.MAX_VALUE)
	public int globalCap = -1;

	@Name("Saturation Cap")
	@Comment({
			"A hard cap on a player's saturation. It can never go above this value.",
			"Any player's with a saturation above this value will be set to this value.",
			"If set to -1, this cap is ignored.",
			"Changes in game will be observed on the next player tick"})
	@RangeDouble(min = -1.0)
	public double satCap = -1;

	@Name("Saturation Scaling")
	@Comment({
			"How a player's max saturation should scale to their max hunger",
			"If set to MAX_HUNGER, no scaling is done. A player's max saturation is bounded by their max hunger",
			"If set to HALF_HUNGER, a player's saturation can never be higher than half of their max hunger.",
			"If set to QUARTER_HUNGER, a player's saturation can never be higher than a quarter of their max hunger",
			"Changes in game will be observed on the next player tick"})
	public SaturationScaling satScaling = SaturationScaling.MAX_HUNGER;

	@Name("Starting Hunger")
	@Comment("Players joining worlds for the first time will have their max hunger cap set to this value in half shanks. Vanilla default is 20")
	@RangeInt(min = 1, max = Short.MAX_VALUE)
	public int startingHunger = 20;
}
