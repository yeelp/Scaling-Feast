package yeelp.scalingfeast.config.features;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public final class SFFeatureConfigCategory {

	@Name("Bloated Overflow")
	@Comment({
			"Configure behaviour for bloated overflow",
			"Bloated overflow allows players to gain a Bloated status effect when they overeat."})
	public final SFConfigBloatedOverflow bloatedOverflow = new SFConfigBloatedOverflow();

	@Name("Death Penalty")
	@Comment("Configure what happens to player's extended food stats on death")
	public final SFConfigDeath death = new SFConfigDeath();

	@Name("Health Regen")
	@Comment("Configure how health regenerates naturally from hunger")
	public final SFConfigHealthRegen regen = new SFConfigHealthRegen();

	@Name("Hunger Damage")
	@Comment({
			"Configure behaviour for hunger damage",
			"Hunger damage deducts saturation/hunger from a player when they get hit."})
	public final SFConfigHungerDamage hungerDamage = new SFConfigHungerDamage();
	
	@Name("XP Bonuses")
	@Comment("Configure bonuses to max hunger and food efficiency granted by XP thresholds")
	public final SFConfigXPBonuses xpBonuses = new SFConfigXPBonuses();
	
	@Name("Starvation Penalties")
	@Comment("Configure penalties for starving")
	public final SFConfigStarvation starve = new SFConfigStarvation();
	
	@Name("Exhaustion Scaling")
	@Comment("Configure exhaustion scaling when breaking blocks")
	public final SFConfigExhaustionScaling exhaustionScaling = new SFConfigExhaustionScaling();
}
