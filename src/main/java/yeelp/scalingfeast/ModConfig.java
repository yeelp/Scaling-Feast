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
import yeelp.scalingfeast.util.FoodStatsMap;

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
		
		@Name("Death Penalty")
		@Comment("Configure what happens to player's extended food stats on death")
		public DeathCategory death = new DeathCategory();
		public static class DeathCategory
		{	
			@Name("Lose Max?")
			@Comment("If true, players will lose their food cap on death, resetting them to vanilla's cap")
			public boolean loseCap = false;
		}
	}
	
	public static class CompatCategory
	{
		@Name("AppleSkin")
		@Comment("Tweak compatibility with AppleSkin. These settings will only have an effect if AppleSkin is installed")
		public AppleSkinCategory appleskin = new AppleSkinCategory();
		
		public static class AppleSkinCategory
		{
			@Name("Should Draw Exhaustion Underlay?")
			@Comment({"If true, Scaling Feast will make sure that AppleSkin's exhaustion underlay is drawn.",
				      "Set to false if AppleSkin isn't drawing the exhaustion underlay.",
				      "This only works if Display is set to OVERLAY"})
			public boolean drawExhaustion = true;
		}
	}
	public static class ItemCategory
	{
		@Name("Hearty Shank Hunger Value")
		@Comment("The Food value of a Hearty Shank, in half shanks (i.e. 2 = one full hunger shank)")
		@RangeInt(min = 0)
		@RequiresMcRestart
		public int heartyShankFoodLevel = 4;
		
		@Name("Hearty Shank Saturation Modifier")
		@Comment("The saturation modifier for the Hearty Shank. This isn't the exact saturation, but a value used to calculate saturation.")
		@RangeDouble(min = 0)
		@RequiresMcRestart
		public double heartyShankSatLevel = 0.8;
	}
	public static class HUDCategory
	{
		public enum DisplayStyle
		{
			OVERLAY,
			NUMERICAL,
			DEFAULT;
		}
		@Name("Display Style")
		@Comment({"The display style in the HUD.",
			      "If set to NUMERICAL, Scaling Feast will display a \'+(x/X, Y)\' next to your hunger bar, when over the default vanilla max hunger, where x is your current extra food level, X is your max food level, and Y is your saturation (Only if Draw Saturation is set to true).",
			      "If set to OVERLAY, Scaling Feast will overlay coloured shanks over your hunger bar to display your extended food stats.",
			      "If set to DEFAULT, Scaling Feast will do nothing. Your default vanilla hunger bar will represent your entire hunger bar."})
		public DisplayStyle style = DisplayStyle.OVERLAY;
		
		@Name("Draw Saturation?")
		@Comment("If set to false, Scaling Feast will make no attempt to provide any information to the player about thier vanilla or extended saturation.")
		public boolean drawSaturation = true;
		
		@Name("Hunger Overlay Colours")
		@Comment({"A List of hex colours for the coloured shanks. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				  "This only has an effect if Display Style is set to OVERLAY.",
				  "If the number of \'rows\' of hunger bars exceed the length of this list, it will wrap around to the beginning.",
				  "If any invalid hex string is entered, it will be ignored."})
		@RequiresMcRestart
		public String[] Hcolours = {"ff9d00", "ffee00", "00ff00", "0000ff", "00ffff", "e100ff", "ffffff"}; 
		
		@Name("Saturation Overlay Colours")
		@Comment({"A List of hex colours for the coloured outline of the shanks for saturation. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				  "This only has an effect if Display Style is set to OVERLAY.",
				  "If the number of \'rows\' of saturation exceed the length of this list, it will wrap around to the beginning.",
				  "If any invalid hex string is entered, it will be ignored."})
		@RequiresMcRestart
		public String[] Scolours = {"d70000", "d700d7", "6400d7", "00d3d7", "64d700", "d7d700", "d7d7d7"}; 
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
				ConfigManager.sync(ModConsts.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
