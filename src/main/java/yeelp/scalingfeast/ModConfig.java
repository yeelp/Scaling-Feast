package yeelp.scalingfeast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.handlers.HUDOverlayHandler;
import yeelp.scalingfeast.helpers.SOLCarrotHelper;
import yeelp.scalingfeast.util.ConfigVersion;
import yeelp.scalingfeast.util.ConfigVersionChecker;

@Config(modid = ModConsts.MOD_ID)
public class ModConfig extends Configuration
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
		@Name("Global Cap")
		@Comment({"The highest extended hunger the player can have.",
				  "Note that any players with an extended hunger value greater than this will be set to this cap",
			      "This ignores vanilla's hunger level; it ONLY affects the additional amount of hunger you can gain from Scaling Feast.",
			      "If set to -1, this cap is ignored."})
		@RangeInt(min = -1, max = Short.MAX_VALUE)
		public int globalCap = -1;
		
		@Name("Increase Per Hearty Shank Eaten")
		@Comment("The increase in your total max hunger, in half shanks (i.e. 2 = one full hunger shank) per Hearty Shank eaten.")
		@RangeInt(min = 0, max = Short.MAX_VALUE)
		public int inc = 2;
		
		@Name("Starting Hunger")
		@Comment("Players joining worlds for the first time will have thier max hunger cap set to this value in half shanks. Vanilla default is 20")
		@RangeInt(min = 1, max = Short.MAX_VALUE)
		public int startingHunger = 20;
		
		@Name("Death Penalty")
		@Comment("Configure what happens to player's extended food stats on death")
		public DeathCategory death = new DeathCategory();
		public static class DeathCategory
		{	
			@Name("Max Lost on Death")
			@Comment("If not set to zero, this field indicates how much of your maximum hunger you lose upon death. Can not go below the default vanilla maximum.")
			@RangeInt(min = 0, max = Short.MAX_VALUE)
			public int maxLossAmount = 0;
			
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
		
		@Name("Enable Metabolic Potions")
		@Comment("If false, Scaling Feast will not register Metabolic Potions and its variants. This does not remove the Metabolism Potion Effect from the game, just the potions.")
		@RequiresMcRestart
		public boolean enableMetabolicPotion = true;
		
		@Name("Enable Brewing Recipes")
		@Comment("If false, Scaling Feast will not create brewing recipes for Metabolic Potions. The potions will still be registered. However, if Metabolic Potions are disabled, recipes will of course not be added, and this config option will do nothing.")
		@RequiresMcRestart
		public boolean enableMetabolicRecipes = true;
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
		@Name("Display Style")
		@Comment({"The display style in the HUD.",
			      "If set to NUMERICAL, Scaling Feast will display a \'+(x/X, Y)\' next to your hunger bar, when over the default vanilla max hunger, where x is your current extra food level, X is your max food level, and Y is your saturation (Only if Draw Saturation is set to true).",
			      "If set to OVERLAY, Scaling Feast will overlay coloured shanks over your hunger bar to display your extended food stats.",
			      "If set to DEFAULT, Scaling Feast will do nothing. Your default vanilla hunger bar will represent your entire hunger bar."})
		public DisplayStyle style = DisplayStyle.OVERLAY;
		
		@Name("Info Style")
		@Comment({"The text to display to the right of the hunger bar",
				  "If set to SIMPLE, the text \'xb/B\' will be shown, where b is the number of hunger bars you currently have and B is the number of hunger bars you will have when at your max",
				  "If set to ADVANCED the text \'(x/X, Y)\' will be shown, where x is your current food level, X is your max food level, and Y is your saturation (Only if Draw Saturation is set to true)."})
		public InfoStyle infoStyle = InfoStyle.SIMPLE;
		
		@Name("Overlay Style")
		@Comment("If set to REVERSED, the icon styles used for saturation and max hunger will be swapped.")
		public OverlayStyle overlayStyle = OverlayStyle.DEFAULT;
		
		@Name("Max Outline Colour Style")
		@Comment({"The Colour style to use when drawing the max outline.",
			      "If set to DEFAULT, the default colour style will be used.",
			      "If set to CUSTOM, Scaling Feast will take the colour value specified in Max Custom Colour Start and transition to Max Custom Colour End when taking starvation damage."})
		public MaxColourStyle maxColourStyle = MaxColourStyle.DEFAULT;
		
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
			@RequiresMcRestart
			public boolean useFoodGroups = false;
			
			@Name("Required Amount")
			@Comment("How many unique entries must be found in a player's food history to prevent punishing them. Should be less than or equal to Spice of Life's food history length")
			@RangeInt(min = 1)
			@RequiresMcRestart
			public int uniqueRequired = 5;
			
			@Name("Penalty")
			@Comment({"If the number of unique entires in a player's food history is less than Required Amount, that player will lose this much max hunger for every unique entry missing.",
					  "For example, if a player has 3 unique entires and the required amount is 5, they will lose (5-3)*(penalty) max hunger"})
			@RangeInt(min = 1, max = Short.MAX_VALUE)
			@RequiresMcRestart
			public int penalty = 2;
		}
		
		public static class SOLCarrotCategory
		{
			@Name("Enabled")
			@Comment("Set to true to enable the Spice Of Life: Carrot Edition module")
			@RequiresMcRestart
			public boolean enabled = false;
			
			@Name("Milestones")
			@Comment({"A list of pairs delimited by a colon, m:r, of milestones and milestone rewards.",
					  "When a player eats m unique food items, they will gain r max hunger, in half shanks. m must be a positive integer and r must be a positive integer less than 32767.",
					  "Values for r > 32767 will be brought inside these bounds modulo 32767. list entires that aren't of this form, or pairs containing negative values for either m or r will be silently ignored."})
			@RequiresMcRestart
			public String[] milestones = {"5:2", "10:2", "15:2", "20:2", "25:2", "30:2", "35:2", "40:2", "45:2", "50:2"};
		
			@Name("Reward Messages Above Hotbar?")
			@Comment("If true, Scaling Feast will display its reward messages above a player's hotbar. Else, it will display it in chat.")
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
			if (event.getModID().equals(ModConsts.MOD_ID)) 
			{
				boolean addConfigVersion;
				ConfigVersion ver = null;
				try
				{
					ver = ConfigVersionChecker.getConfigVersion(ScalingFeast.config);
					if(ver.compareTo(ConfigVersionChecker.getCurrentConfigVersion()) < 0)
					{
						//config version outdated - update.
						addConfigVersion = true;
					}
					else
					{
						//config version matches, ignore.
						addConfigVersion = false;
					}
				} 
				catch (IOException e)
				{
					ScalingFeast.err("Something went wrong parsing the config version.");
					addConfigVersion = false;
					e.printStackTrace();
				}
				ConfigManager.sync(ModConsts.MOD_ID, Config.Type.INSTANCE);
				if(addConfigVersion)
				{
					Queue<String> lines = new LinkedList<String>();;
					try(BufferedReader br = new BufferedReader(new FileReader(ScalingFeast.config)))
					{
						Iterator<String> it = br.lines().iterator(); 
						lines.add(it.next());
						if(ver != null && !ver.isUnversioned())
						{
							//pass over config version we don't care about, then add the right config version
							it.next();
							lines.add("~CONFIG VERSION: "+ModConsts.CONFIG_VERSION);
						}
						while(it.hasNext())
						{
							lines.add(it.next());
						}
					} 
					catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!lines.isEmpty())
					{
						try(PrintWriter writer = new PrintWriter(ScalingFeast.config))
						{
							for(String line : lines)
							{
								writer.println(line);
							}
						} 
						catch (FileNotFoundException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				HUDOverlayHandler.loadColours();
				HUDOverlayHandler.setIcons();
				HUDOverlayHandler.loadTextColours();
				SOLCarrotHelper.parseMilestones();
			}
		}
	}
}
