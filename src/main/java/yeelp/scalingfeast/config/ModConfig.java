package yeelp.scalingfeast.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.features.SFFeatureConfigCategory;
import yeelp.scalingfeast.config.items.SFItemConfigCategory;
import yeelp.scalingfeast.config.modules.SFModuleConfigCategory;
import yeelp.scalingfeast.hud.DrawUtils;

@Config(modid = ModConsts.MOD_ID)
public class ModConfig {
	@Name("General")
	@Comment("These settings modify the base behaviour of Scaling Feast")
	public static final SFGeneralConfigCategory general = new SFGeneralConfigCategory();

	@Name("Compatibility")
	@Comment("These settings are for ensuring compatibility between other mods")
	public static final SFCompatConfigCategory compat = new SFCompatConfigCategory();

	@Name("Items")
	@Comment("These settings are for tweaking Scaling Feast's items")
	public static final SFItemConfigCategory items = new SFItemConfigCategory();

	@Name("HUD")
	@Comment("These settings are for tweaking the heads-up display")
	public static final HUDCategory hud = new HUDCategory();

	@Name("Modules")
	@Comment("Enable and tweak Scaling Feast's behaviour with other mods")
	public static final SFModuleConfigCategory modules = new SFModuleConfigCategory();
	
	@Name("Features")
	@Comment("Configure various features provided by Scaling Feast.")
	public static final SFFeatureConfigCategory features = new SFFeatureConfigCategory();
	
	@Name("Debug")
	@Comment("Set to true to enable debug mode. Console log may be filled with debug messages if Scaling Feast sends any debug messages.")
	public static boolean debug = false;

	public static class HUDCategory {
		public enum DisplayStyle {
			OVERLAY,
			DEFAULT;
		}

		public enum InfoStyle {
			SIMPLE,
			ADVANCED;
		}

		public enum OverlayStyle {
			DEFAULT,
			REVERSED;
		}

		public enum MaxColourStyle {
			DEFAULT,
			CUSTOM;
		}

		public enum TrackerStyle {
			MAX_COLOUR,
			SATURATION;
		}

		@Name("Display Style")
		@Comment({
				"The display style in the HUD.",
				"If set to OVERLAY, Scaling Feast will overlay coloured shanks over your hunger bar to display your extended food stats.",
				"If set to DEFAULT, Scaling Feast will do nothing. Your default vanilla hunger bar will represent your entire hunger bar."})
		public DisplayStyle style = DisplayStyle.OVERLAY;

		@Name("Info Style")
		@Comment({
				"The text to display to the right of the hunger bar",
				"If set to SIMPLE, the text \'xb/B\' will be shown, where b is the number of hunger bars you currently have and B is the number of hunger bars you will have when at your max",
				"If set to ADVANCED the texts \'x/X\' and \'Y\' will be shown, stacked on top of on another, where x is your current food level, X is your max food level, and Y is your saturation (Only if Draw Saturation is set to true)."})
		public InfoStyle infoStyle = InfoStyle.SIMPLE;

		@Name("Overlay Style")
		@Comment("If set to REVERSED, the icon styles used for saturation and max hunger will be swapped.")
		public OverlayStyle overlayStyle = OverlayStyle.DEFAULT;

		@Name("Max Outline Colour Style")
		@Comment({
				"The Colour style to use when drawing the max outline.",
				"If set to DEFAULT, the default colour style will be used.",
				"If set to CUSTOM, Scaling Feast will take the colour value specified in Max Custom Colour Start and transition to Max Custom Colour End when taking starvation damage."})
		public MaxColourStyle maxColourStyle = MaxColourStyle.DEFAULT;

		@Name("Starvation Tracker Style")
		@Comment({
				"The style for tracking starvation.",
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

		@Name("Info text x offset")
		@Comment("Shift the info text in the x direction. Can be positive or negative.")
		public int infoXOffset = 0;

		@Name("Info text y offset")
		@Comment("Shift the info text in the y direction. Can be positive or negative.")
		public int infoYOffset = 0;

		@Name("Draw Saturation?")
		@Comment("If set to false, Scaling Feast will make no attempt to provide any information to the player about thier vanilla or extended saturation.")
		public boolean drawSaturation = true;

		@Name("Replace Vanilla Hunger")
		@Comment("If true, Scaling Feast will replace the vanilla hunger shanks with coloured shanks if the display style is set to OVERLAY")
		public boolean replaceVanilla = false;

		@Name("Hunger Overlay Colours")
		@Comment({
				"A List of hex colours for the coloured shanks. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				"This only has an effect if Display Style is set to OVERLAY.",
				"If the number of \'rows\' of hunger bars exceed the length of this list, it will wrap around to the beginning.",
				"If any invalid hex string is entered, it will be ignored."})
		public String[] Hcolours = {
				"ff9d00",
				"ffee00",
				"00ff00",
				"0000ff",
				"00ffff",
				"e100ff",
				"ffffff"};

		@Name("Saturation Overlay Colours")
		@Comment({
				"A List of hex colours for the coloured outline of the shanks for saturation. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				"This only has an effect if Display Style is set to OVERLAY.",
				"If the number of \'rows\' of saturation exceed the length of this list, it will wrap around to the beginning.",
				"If any invalid hex string is entered, it will be ignored."})
		public String[] Scolours = {
				"d70000",
				"d700d7",
				"6400d7",
				"00d3d7",
				"64d700",
				"d7d700",
				"d7d7d7"};

		@Name("Bloated Overlay Colours")
		@Comment({
				"A List of hex colours for coloured shanks a player receives while under the Bloated effect. Each entry is of the form XXXXXX, where X is a hexadecimal digit",
				"If the number of \'rows\' of bloated shanks exceed the length of this list, it will wrap around to the beginning.",
				"If any invalid hex string is entered, it will be ignored."})
		public String[] Bcolours = {
				"ffff6e",
				"ff6e6e",
				"6eff6e",
				"6effff",
				"6e6eff",
				"ff6eff",
				"e6e6e6"};
	}

	@Mod.EventBusSubscriber(modid = ModConsts.MOD_ID)
	private static class EventHandler {

		/**
		 * Inject the new values and save to the config file when the config has been
		 * changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(ModConsts.MOD_ID)) {
				ConfigManager.sync(ModConsts.MOD_ID, Config.Type.INSTANCE);
				DrawUtils.updateColours();
				DrawUtils.updateTextColours();
			}
		}
	}
}
