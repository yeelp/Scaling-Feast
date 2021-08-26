package yeelp.scalingfeast.config.items;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

public final class SFConfigEnchantments {
	@Name("Enable Eternal Feast")
	@Comment("Enables or disables the Eternal Feast enchantment. If disabled the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableEternalFeast = true;

	@Name("Enable Gluttony")
	@Comment("Enables or disables the Gluttony enchantment. If disabled the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableGluttony = true;

	@Name("Enable Famine")
	@Comment("Enables or disables the Famine enchantment. If disabled the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableFamine = true;

	@Name("Enable Fasting")
	@Comment("Enables or disables the Fasting enchantment. If disabled the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableFasting = true;

	@Name("Enable Laziness Curse")
	@Comment("Enables or disables the Curse of Laziness. If disabled, the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableLaziness = true;

	@Name("Enable Deprivation Curse")
	@Comment("Enables or disables the Curse of Deprivation. If disabled, the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableDeprivation = true;

	@Name("Enable Sensitivity Curse")
	@Comment("Enables or disables the Curse of Sensitivity. If disabled, the enchantment won't be registered at all.")
	@RequiresMcRestart
	public boolean enableSensitivity = true;

	@Name("Global Sensitivity")
	@Comment("If true, the Curse of Sensitivity will be disabled, but the effects will apply to all players at all times, regardless if you have the curse or not.")
	@RequiresMcRestart
	public boolean globalSensitvity = false;
}
