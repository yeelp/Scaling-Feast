package yeelp.scalingfeast;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.api.impl.ScalingFeastAPIImpl;
import yeelp.scalingfeast.blocks.HeartyFeastBlock;
import yeelp.scalingfeast.handlers.HUDOverlayHandler;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;
import yeelp.scalingfeast.util.XPBonusType;
import yeelp.scalingfeast.util.SaturationScaling;
@Config(modid = ModConsts.MOD_ID)
public class ModConfig
{	
	@Name("Food Cap")
	@Comment("These settings modify the base behaviour of Scaling Feast")
	public static final FoodCapCategory foodCap = new FoodCapCategory();
	
	@Name("Compatibility")
	@Comment("These settings are for ensuring compatibility between other mods")
	public static final CompatCategory compat = new CompatCategory();
	
	@Name("Items")
	@Comment("These settings are for tweaking Scaling Feast's items")
	public static final ItemCategory items = new ItemCategory();
	
	@Name("HUD")
	@Comment("These settings are for tweaking the heads-up display")
	public static final HUDCategory hud = new HUDCategory();
	
	@Name("Modules")
	@Comment("Enable and tweak Scaling Feast's behaviour with other mods")
	public static final ModuleCategory modules = new ModuleCategory();
	
	
	public static class FoodCapCategory
	{	
		public enum RegenCriterion
		{
			VANILLA,
			VANILLA_LIKE,
			DISABLED;
		}
		
		@Name("Global Cap")
		@Comment({"The highest extended hunger the player can have.",
				  "Note that any players with an extended hunger value greater than this will be set to this cap",
			      "This ignores vanilla's hunger level; it ONLY affects the additional amount of hunger you can gain from Scaling Feast.",
			      "If set to -1, this cap is ignored.",
			      "Changes in game will be observed on the next player tick"})
		@RangeInt(min = -1, max = Short.MAX_VALUE)
		public int globalCap = -1;
		
		@Name("Saturation Cap")
		@Comment({"A hard cap on a player's saturation. It can never go above this value.",
			      "Any player's with a saturation above this value will be set to this value.",
			      "If set to -1, this cap is ignored.",
			      "Changes in game will be observed on the next player tick"})
		@RangeDouble(min = -1.0)
		public double satCap = -1;
		
		@Name("Saturation Scaling")
		@Comment({"How a player's max saturation should scale to their max hunger",
			      "If set to MAX_HUNGER, no scaling is done. A player's max saturation is bounded by their max hunger",
			      "If set to HALF_HUNGER, a player's saturation can never be higher than half of their max hunger.",
			      "If set to QUARTER_HUNGER, a player's saturation can never be higher than a quarter of their max hunger",
			      "Changes in game will be observed on the next player tick"})
		public SaturationScaling satScaling = SaturationScaling.MAX_HUNGER;
		
		@Name("Increase Per Hearty Shank Eaten")
		@Comment("The increase in your total max hunger, in half shanks (i.e. 2 = one full hunger shank) per Hearty Shank eaten.")
		@RangeInt(min = 0, max = Short.MAX_VALUE)
		public int inc = 2;
		
		@Name("Hearty Shank Usage Cap")
		@Comment({"The maximum number of times a Hearty Shank can be used to increase max hunger.",
			      "After that, the Hearty Shank can still be consumed, but won't increase max hunger.",
			      "Set to -1 for no limit."})
		@RangeInt(min = -1)
		public int heartyShankCap = -1;
		
		@Name("Starting Hunger")
		@Comment("Players joining worlds for the first time will have their max hunger cap set to this value in half shanks. Vanilla default is 20")
		@RangeInt(min = 1, max = Short.MAX_VALUE)
		public int startingHunger = 20;
		
		@Name("Hunger Damage Multiplier")
		@Comment("When a player is attacked by a non-player entity, they will lose some hunger proportional to the damage dealt. This value determines this proportion (For example, setting to 1.0 means all damage inflicted is deducted from a player's food stats, 0.5 would mean only half that damage will be deducted from a player's food stats. 2.0 would do double damage etc.). If set to 0, this feature is disabled.")
		@RangeDouble(min = 0.0)
		public double hungerDamageMultiplier = 0.0;
		
		@Name("Allow Bloated Overflow Hunger")
		@Comment("If enabled and if a player eats a food item that grants more hunger than they need, Scaling Feast will grant the player the nearest level Bloated effect to match the amount of excess hunger a player ate. Potions must be registered for this to work.")
		public boolean doBloatedOverflow = false;
		
		@Name("Bloated Overflow Duration")
		@Comment("If Bloated Overflow is enabled, this dictates how long the potion should last in ticks. Note, there are 20 ticks per second.")
		@RangeInt(min = 1)
		public int bloatedOverflowDuration = 1800;
		
		@Name("Hunger Regen Type")
		@Comment({"Alters how natural regeneration works. Note all options except DISABLED still respect the naturalRegeneration gamerule.",
				  "VANILLA - Scaling Feast leaves regen behaviour as is.",
				  "VANILLA_LIKE - Scaling Feast extends Minecraft's conditions for natural regeneration. That is, natural regeneration will occur provided your hunger is at least <max hunger> - 2",
				  "DISABLED - Scaling Feast will always prevent natural regeneration from hunger."})
		public RegenCriterion hungerRegen = RegenCriterion.VANILLA;
		
		
		@Name("Saturated Regen Type")
		@Comment({"Alters how saturated natural regeneration works. Note all options still respect the naturalRegeneration gamerule.",
			      "VANILLA - Scaling Feast leaves regen behaviour as is.",
			      "VANILLA_LIKE - Scaling Feast extends Minecraft's conditions for saturated regeneration. That is, saturated regen occurs if a player has non zero saturation and full hunger.",
			      "DISABLED - Scaling Feast will always prevent saturated regeneration."})
		public RegenCriterion satRegen = RegenCriterion.VANILLA;
		
		@Name("Food Efficiency Bonus")
		@Comment({"Enable and set Food Efficiency Bonus.",
			      "When set, a player's Food Efficiency attribute will change depending on XP. The rewards they get can be configured in Food Efficiency XP Rewards.",
			      "NONE - Food Efficiency Bonus will not be given to any players, effectively disabling this feature.",
			      "LEVEL - Scaling Feast will compare a player's level (so a value of 2 in the rewards would correspond to level 2) against the entries in Food Efficiency XP Rewards when calculating the attribute value.",
			      "AMOUNT - Scaling Feast will compare a player's XP total (so a value of 34 corresponds to level 2) against the entries in Food Efficiency XP Rewards when calculating the attribute value."})
		public XPBonusType efficiencyXPBonus = XPBonusType.NONE;
		
		@Name("Max Hunger Bonus")
		@Comment({"Enable and set Max Hunger Bonus.",
			      "When set, a player's max hunger attribute will change depending on XP. The rewards they get can be configured in Max Hunger XP Rewards.",
			      "NONE - Max Hunger Bonus will not be given to any players, effectively disabling this feature.",
			      "LEVEL - Scaling Feast will compare a player's level (so a value of 2 in the rewards would correspond to level 2) against the entries in Max Hunger XP Rewards when calculating the attribute value.",
			      "AMOUNT - Scaling Feast will compare a player's XP total (so a value of 34 corresponds to level 2) against the entries in Max Hunger XP Rewards when calculating the attribute value."})
		public XPBonusType maxHungerXPBonus = XPBonusType.NONE;
		
		@Name("Food Efficiency XP Rewards")
		@Comment({"A list of values x:b, where x is the player's XP, and b is the Food Efficiency Bonus (or penalty, can set negative values) this player receives.",
				  "A player will get ALL bonus that their XP threshold surpasses. The way the XP argument is interpreted is set in the Food Efficiency Bonus option."})
		public String[] efficiencyRewards = {};
		
		@Name("Max Hunger XP Rewards")
		@Comment({"A list of values x:b, where x is the player's XP, and b is the Max Hunger Bonus (or penalty, can set negative values) this player receives.",
		          "A player will get ALL bonus that their XP threshold surpasses. The way the XP argument is interpreted is set in the Max Hunger Bonus option."})
		public String[] maxHungerRewards = {};
		
		@Name("Death Penalty")
		@Comment("Configure what happens to player's extended food stats on death")
		public DeathCategory death = new DeathCategory();
		public static class DeathCategory
		{	
			@Name("Max Lost on Death")
			@Comment("If not set to zero, this field indicates how much of your maximum hunger you lose upon death. Can not go below maxLossLowerBound.")
			@RangeInt(min = 0, max = Short.MAX_VALUE)
			public int maxLossAmount = 0;
			
			@Name("Max Loss Lower Bound")
			@Comment("A player's max hunger will never go below this value via death penalties.")
			@RangeInt(min = 1)
			public int maxLossLowerBound = 1;
			
			@Name("Hunger Lost on Death")
			@Comment("If not set to zero, this field indicates how much hunger you lose on death. Will not bring your respawning hunger value below vanilla's default maximum")
			@RangeInt(min = 0, max = Short.MAX_VALUE)
			public int hungerLossOnDeath = 0;
		}
		
		@Name("Starvation Penalty")
		@Comment("Configure penalties for starving")
		public StarvationCategory starve = new StarvationCategory();
		public static class StarvationCategory
		{
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
			@Comment({"Should the frequency counter reset upon gaining hunger?",
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
			@Comment("If true, Scaling Feast will remeber how much exhaustion a player has received since going to zero hunger. Then, Scaling Feast will increase starvation damage proprtional to the amount of food points a player would have lost if they weren't starving. In addition, a player's starvation tracker, as described in the other settings here, will be increased multiple times in accordance to the amount of extra starvation damage received.")
			public boolean doDynamicStarvation = false;
			
			@Name("Bonus Starvation Damage Multiplier")
			@Comment("When starving with dynamic starvation enabled, this is the amount of bonus damage to do, in half hearts, per food point lost via exhaustion")
			@RangeInt(min = 1)
			public int bonusStarveDamageMult = 1;
		}
	}
	
	public static class CompatCategory
	{	
		@Name("Enable Compatibility Settings")
		@Comment({"If true, Scaling Feast will try to fire a RenderGameOverlay.Post event with ElementType.FOOD for mods that may use that event.", 
				  "Try this if other mods have their HUD components disappear when display style is set to OVERLAY"})
		public boolean shouldFirePost = true;
	}
	public static class ItemCategory
	{
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
		
		@Name("Hearty Feast Restoration Cap")
		@Comment("This is the maximum value the Hearty Feast will restore. If set to -1, there is no limit. If set to 0, the Hearty Feast won't restore anything.")
		@RangeInt(min=-1)
		public int heartyFeastCap = -1;
		
		@Name("Enable Potions")
		@Comment("If false, Scaling Feast will not register potions for all of its potion effects. This doesn't remove the potion effects from the game, just the potions. Note only the Metabolic Potion has brewing recipes added by Scaling Feast.")
		@RequiresMcRestart
		public boolean enablePotions = true;
		
		@Name("Enable Brewing Recipes")
		@Comment("If false, Scaling Feast will not create brewing recipes for Metabolic Potions. The potions will still be registered. However, if Metabolic Potions are disabled, recipes will of course not be added, and this config option will do nothing.")
		@RequiresMcRestart
		public boolean enableMetabolicRecipes = true;
		
		@Name("Enchantments")
		@Comment("Configure enchantments added by Scaling Feast")
		public EnchantmentCategory enchants = new EnchantmentCategory();
		public static class EnchantmentCategory
		{
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
	}
	public static class HUDCategory
	{
		public enum DisplayStyle
		{
			OVERLAY,
			DEFAULT;
		}
		public enum InfoStyle
		{
			SIMPLE,
			ADVANCED;
		}
		public enum OverlayStyle
		{
			DEFAULT,
			REVERSED;
		}
		public enum MaxColourStyle
		{
			DEFAULT,
			CUSTOM;
		}
		public enum TrackerStyle
		{
			MAX_COLOUR,
			SATURATION;
		}
		@Name("Display Style")
		@Comment({"The display style in the HUD.",
			      "If set to OVERLAY, Scaling Feast will overlay coloured shanks over your hunger bar to display your extended food stats.",
			      "If set to DEFAULT, Scaling Feast will do nothing. Your default vanilla hunger bar will represent your entire hunger bar."})
		public DisplayStyle style = DisplayStyle.OVERLAY;
		
		@Name("Info Style")
		@Comment({"The text to display to the right of the hunger bar",
				  "If set to SIMPLE, the text \'xb/B\' will be shown, where b is the number of hunger bars you currently have and B is the number of hunger bars you will have when at your max",
				  "If set to ADVANCED the texts \'x/X\' and \'Y\' will be shown, stacked on top of on another, where x is your current food level, X is your max food level, and Y is your saturation (Only if Draw Saturation is set to true)."})
		public InfoStyle infoStyle = InfoStyle.SIMPLE;
		
		@Name("Overlay Style")
		@Comment("If set to REVERSED, the icon styles used for saturation and max hunger will be swapped.")
		public OverlayStyle overlayStyle = OverlayStyle.DEFAULT;
		
		@Name("Max Outline Colour Style")
		@Comment({"The Colour style to use when drawing the max outline.",
			      "If set to DEFAULT, the default colour style will be used.",
			      "If set to CUSTOM, Scaling Feast will take the colour value specified in Max Custom Colour Start and transition to Max Custom Colour End when taking starvation damage."})
		public MaxColourStyle maxColourStyle = MaxColourStyle.DEFAULT;
		
		@Name("Starvation Tracker Style")
		@Comment({"The style for tracking starvation.",
			      "If set to MAX_COLOUR, the max outline colour will change depending on how many times you've taken starvation damage. The colours used depend on Max Outline Colour Style.",
			      "If set to SATURATION, then only when the hunger bar is empty, each starvation damage will cause a \'saturation bar\' to fill up over your hunger bar more and more. once full, taking starvation damage will decrease your max hunger."})
		public TrackerStyle trackerStyle = TrackerStyle.MAX_COLOUR;
		
		@Name("Max Outline Transparency")
		@Comment("How transparent should the max outline be when a player's hunger is not on the same \'layer\' as it, or not starving. 1.0 if completely solid, 0.0 if completely transparent")
		@RangeDouble(min = 0.0, max = 1.0)
		public double maxOutlineTransparency = 0.5;
		
		@Name("Saturation Text Colour")
		@Comment("The colour of the text used when drawing saturation info. Only affects the ADVANCED info style. Must be a valid hexadecimal number.")
		public String satTextColour = "ffff55";
		
		@Name("Saturation Text Colour Empty")
		@Comment("The colour of the saturation text when a player has no saturation. Only affects the ADVANCED info style. Must be a valid hexadecimal number")
		public String satTextColourEmpty = "555555";
		
		@Name("Max Custom Colour Start")
		@Comment("When Max Outline Colour Style is CUSTOM and when the player hasn't taken starvation damage, this is the hex colour of the starvation tracker.")
		public String maxColourStart = "ffffff";
		
		@Name("Max Custom Colour End")
		@Comment("When Max Outline Colour Style is CUSTOM and when the player is about to lose max hunger, this is the hex colour of the starvation tracker.")
		public String maxColourEnd = "aa0000";
		
		@Name("ADVANCED info text x offset")
		@Comment("Shift the ADVANCED info text in the x direction. Can be positive or negative.")
		public int infoXOffset = 0;
		
		@Name("ADVANCED info text y offset")
		@Comment("Shift the ADVANCED info text in the y direction. Can be positive of negative.")
		public int infoYOffset = 0;
		
		@Name("Draw Saturation?")
		@Comment("If set to false, Scaling Feast will make no attempt to provide any information to the player about thier vanilla or extended saturation.")
		public boolean drawSaturation = true;
		
		@Name("Replace Vanilla Hunger")
		@Comment("If true, Scaling Feast will replace the vanilla hunger shanks with coloured shanks if the display style is set to OVERLAY")
		public boolean replaceVanilla = false;
		
		@Name("Hunger Overlay Colours")
		@Comment({"A List of hex colours for the coloured shanks. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				  "This only has an effect if Display Style is set to OVERLAY.",
				  "If the number of \'rows\' of hunger bars exceed the length of this list, it will wrap around to the beginning.",
				  "If any invalid hex string is entered, it will be ignored."})
		public String[] Hcolours = {"ff9d00", "ffee00", "00ff00", "0000ff", "00ffff", "e100ff", "ffffff"}; 
		
		@Name("Saturation Overlay Colours")
		@Comment({"A List of hex colours for the coloured outline of the shanks for saturation. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				  "This only has an effect if Display Style is set to OVERLAY.",
				  "If the number of \'rows\' of saturation exceed the length of this list, it will wrap around to the beginning.",
				  "If any invalid hex string is entered, it will be ignored."})
		public String[] Scolours = {"d70000", "d700d7", "6400d7", "00d3d7", "64d700", "d7d700", "d7d7d7"}; 
		
		@Name("Bloated Overlay Colours")
		@Comment({"A List of hex colours for coloured shanks a player receives while under the Bloated effect. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				  "If the number of \'rows\' of bloated shanks exceed the length of this list, it will wrap around to the beginning.",
				  "If any invalid hex string is entered, it will be ignored."})
		public String[] Bcolours = {"ffff6e", "ff6e6e", "6eff6e", "6effff", "6e6eff", "ff6eff", "e6e6e6"};
	}
	
	public static class ModuleCategory
	{
		@Name("Disable Hearty Shank Effects")
		@Comment("If true, the Hearty Shank will no longer increase max hunger")
		public boolean isShankDisabled = false;
		
		@Name("Spice Of Life")
		@Comment("Tweak Spice Of Life integration")
		public SpiceOfLifeCategory spiceoflife = new SpiceOfLifeCategory();
		
		@Name("Spice Of Life: Carrot Edition")
		@Comment("Tweak Spice Of Life: Carrot Edition integration")
		public SOLCarrotCategory sol = new SOLCarrotCategory();
		
		public static class SpiceOfLifeCategory
		{
			@Name("Enabled")
			@Comment("Set to true to enable the Spice Of Life module")
			@RequiresMcRestart
			public boolean enabled = false;
			
			@Name("Use Food Groups")
			@Comment("Should Scaling Feast check food groups in a player's food history instead of individual food items? Must have food groups defined in Spice Of Life")
			public boolean useFoodGroups = false;
			
			@Name("Required Amount")
			@Comment("How many unique entries must be found in a player's food history to prevent punishing them. Should be less than or equal to Spice of Life's food history length")
			@RangeInt(min = 1)
			public int uniqueRequired = 5;
			
			@Name("Penalty")
			@Comment({"If the number of unique entires in a player's food history is less than Required Amount, that player will lose this much max hunger for every unique entry missing.",
					  "For example, if a player has 3 unique entires and the required amount is 5, they will lose (5-3)*(penalty) max hunger"})
			@RangeInt(min = 1, max = Short.MAX_VALUE)
			public int penalty = 2;
		}
		
		public static class SOLCarrotCategory
		{
			@Name("Enabled")
			@Comment("Set to true to enable the Spice Of Life: Carrot Edition module")
			@RequiresMcRestart
			public boolean enabled = false;
			
			@Name("Use Milestones")
			@Comment("Set to true to use regular milestones that increase max hunger as a reward.")
			public boolean useMilestones = true;
			
			@Name("Use Food Efficiency Milestones")
			@Comment("Set to true to use food efficiency milestones that alter a player's exhaustion increase rate")
			public boolean useFoodEfficiencyMilestones = true;
			
			@Name("Milestones")
			@Comment({"A list of pairs delimited by a colon, m:r, of milestones and milestone rewards.",
					  "When a player eats m unique food items, they will gain r max hunger, in half shanks. m must be a positive integer and r must be a positive integer less than 32767.",
					  "Values for r > 32767 will be brought inside these bounds modulo 32767. list entires that aren't of this form, or pairs containing negative values for either m or r will be silently ignored."})
			public String[] milestones = {"5:2", "10:2", "15:2", "20:2", "25:2", "30:2", "35:2", "40:2", "45:2", "50:2"};
			
			@Name("Food Efficiency Milestones")
			@Comment({"A list of pairs delimited by a colon, m:r, of milestones and milestone rewards.",
					  "Identical to regular milestones, however instead of granting the player bonus hunger, these food efficiency milstones increase a player's food efficiency attribute by r when they eat m unique food items, which changes a player's exhaustion rate.", 
					  "Use positive values to DECREASE the rate of exhaustion, and use negative values to INCREASE the rate of exhaustion."})
			public String[] foodEfficiencyMilstones = {"20:0.05", "40:0.05", "60:0.05"};
		
			@Name("Reward Messages Above Hotbar?")
			@Comment("If true, Scaling Feast will display its reward messages above a player's hotbar. Else, it will display it in chat. If multiple reward messages are sent, only the last one is displayed in the hotbar if this is true.")
			public boolean rewardMsgAboveHotbar = false;
		}
	}

	@Mod.EventBusSubscriber(modid = ModConsts.MOD_ID)
	private static class EventHandler 
	{

		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) 
		{
			if(event.getModID().equals(ModConsts.MOD_ID)) 
			{
				ConfigManager.sync(ModConsts.MOD_ID, Config.Type.INSTANCE);
				HUDOverlayHandler.loadColours();
				HUDOverlayHandler.setIcons();
				HUDOverlayHandler.loadTextColours();
				HeartyFeastBlock.updateCap();
				SOLCarrotHelper.parseMilestones();
				SpiceOfLifeHelper.update();
				ScalingFeastAPIImpl.INSTANCE.updateValues();
			}
		}
	}
}
