package yeelp.scalingfeast.config.items;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

public final class SFItemConfigCategory {
	@Name("Enable Potions")
	@Comment("If false, Scaling Feast will not register potions for all of its potion effects. This doesn't remove the potion effects from the game, just the potions. Note only the Metabolic Potion has brewing recipes added by Scaling Feast.")
	@RequiresMcRestart
	public boolean enablePotions = true;

	@Name("Enable Brewing Recipes")
	@Comment("If false, Scaling Feast will not create brewing recipes for Metabolic Potions. The potions will still be registered. However, if Metabolic Potions are disabled, recipes will of course not be added, and this config option will do nothing.")
	@RequiresMcRestart
	public boolean enableMetabolicRecipes = true;
	
	@Name("Hearty Shank")
	@Comment("Change properties about Scaling Feast's Hearty Shank item.")
	public final SFConfigHeartyShank shank = new SFConfigHeartyShank();
	
	@Name("Hearty Feast")
	@Comment("Change properties about Scaling Feast's Hearty Feast block.")
	public final SFConfigHeartyFeast feast = new SFConfigHeartyFeast();
	
	@Name("Enchantments")
	@Comment("Configure enchantments added by Scaling Feast")
	public final SFConfigEnchantments enchants = new SFConfigEnchantments();
}
