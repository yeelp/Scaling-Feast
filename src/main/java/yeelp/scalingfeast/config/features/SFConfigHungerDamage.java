package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;

public final class SFConfigHungerDamage {
	@Name("Hunger Damage Multiplier")
	@Comment("When a player is attacked by a non-player entity, they will lose some hunger proportional to the damage dealt. This value determines this proportion (For example, setting to 1.0 means all damage inflicted is deducted from a player's food stats, 0.5 would mean only half that damage will be deducted from a player's food stats. 2.0 would do double damage etc.). If set to 0, this feature is disabled.")
	@RangeDouble(min = 0.0)
	public double hungerDamageMultiplier = 0.0;
}
