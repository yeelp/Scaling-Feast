package yeelp.scalingfeast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.util.Colour;

public class ModConfig 
{
	public static Configuration config;
	public static final String CATEGORY_EXTENDED_FOOD_STATS = "Extended Food Stats";
	public static final String CATEGORY_ITEMS = "Items";
	public static final String CATEGORY_HUD = "HUD";
	public static final String CATEGORY_COMPAT = "Compatibility";
	public static final String CATEGORY_COMPAT_APPLESKIN = "AppleSkin";
	
	private static final String CATEGORY_EXTENDED_FOOD_STATS_COMMENT = "These config settings are for tweaking your extended food stats added by Scaling Feast";
	private static final String CATEGORY_ITEMS_COMMENT = "These config settings are for tweaking the items added by Scaling Feast";
	private static final String CATEGORY_HUD_COMMENT = "These client side only config settings are for tweaking the display in the HUD for Scaling Feast";
	private static final String CATEGORY_COMPAT_COMMENT = "These config settings are for compatibility with other mods";
	private static final String CATEGORY_COMPAT_APPLESKIN_COMMENT = "These config settings are for compatibility with AppleSkin";
	
	private static final String GLOBAL_CAP_COMMENT = "If set to a potitive integer, no player can have an extended hunger value greater than this (and likewise, their saturation will also be capped). If set to a negative number, this cap is ignored, and there is no limit.";
	private static final String INCREASE_COMMENT = "The increase in max hunger per Hearty Shank eaten.";
	private static final String HEARTY_SHANK_HUNGER_COMMENT = "The amount of hunger a Hearty Shank heals. Note that this heal is done BEFORE a player's cap is increased.";
	private static final String HEARTY_SHANK_SAT_COMMENT = "The saturation modifier for a Hearty Shank. This affects the actual saturation restored by a Hearty Shank and will be dependant on the food level a Hearty Shank restores";
	private static final String DISPLAY_COMMENT = "The display style in the HUD. if set to NUMERICAL, Scaling Feast will display a \'(x/X, Y)\' next to your hunger bar, where x is your current extended food level, X is your max food level, and Y is your saturation. If set to OVERLAY, Scaling Feast will overlay coloured shanks over your hunger bar to display your extended food stats.";
	private static final String HUNGER_COLOURS_COMMENT = "A List of hex colours for the coloured shanks. This only has an effect if Display Style is set to OVERLAY. If the number of \'rows\' of hunger bars exceed the length of this list, it will wrap around to the beginning.";
	private static final String SAT_COLOURS_COMMENT = "A list of the hex colours for the coloured saturation outline. This only has an effect if Display Style is set to OVERLAY. If the number od \'rows\' of saturation exceed the length of this list, it will wrap around  to the beginning.";
	private static final String EXHAUSTION_COMMENT = "If true, Scaling Feast will make sure that AppleSkin's exhaustion underlay is drawn. Set to false if AppleSkin is not installed, or AppleSkin isn't drawing the exhaustion underlay.";
	private static final String VANILLA_SATURATION_OVERLAY_COMMENT = "If true, Scaling Feast won't draw coloured hunger shank outlines for vanilla saturation, only extended saturation. Set this to true if AppleSkin is drawing the saturation overlay already. IF set to false, Scaling Feast will draw saturation outlines for all forms of saturation (vanilla and extended).";
	
	private static final String GLOBAL_CAP = "Global Cap";
	private static final String INCREASE = "Increase Amount";
	private static final String SHANK_HUNGER = "Hearty Shank Hunger Value";
	private static final String SHANK_SAT = "Hearty Shank Sautration Modifier";
	private static final String DISPLAY = "Display Style";
	private static final String HCOLOURS = "Hunger Overlay Colours";
	private static final String SCOLOURS = "Saturation Overlay Colours";
	private static final String EXHAUST = "Draw Exhaustion?";
	private static final String VANILLA_SAT = "Overlay Vanilla Saturation?";
	
	public static int globalCap;
	public static int inc;
	public static int heartyShankHunger;
	public static float heartyShankSat;
	public static String style;
	public static ArrayList<Colour> hungerColours;
	public static ArrayList<Colour> satColours;
	public static boolean shouldDrawExhaustion;
	public static boolean shouldDrawVanillaSat;
	
	private static ArrayList<String> configContents;
	private static String[] validDisplay = {"OVERLAY", "NUMERICAL"};
	private static String[] defaultHColours = {"ff9d00", "ffee00", "00ff00", "0000ff", "00ffff", "e100ff", "ffffff"};
	private static String[] defaultSColours = {"d70000", "d700d7", "6400d7", "00d3d7", "64d700", "d7d700", "d7d7d7"};
	
	public static void init(File file)
	{
		config = new Configuration(file);
		configContents = new ArrayList<String>();
		hungerColours = new ArrayList<Colour>();
		satColours = new ArrayList<Colour>();
		load();
		sync();
	}
	
	public static Configuration getConfig()
	{
		return config;
	}
	
	@SubscribeEvent 
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent evt)
	{
		if(evt.getModID().equals(ModConsts.MOD_ID))
		{
			sync();
		}
	}
	
	private static void save()
	{
		config.save();
	}
	
	private static void load()
	{
		config.load();
	}
	
	private static void sync() 
	{
		config.getCategory(CATEGORY_EXTENDED_FOOD_STATS).setComment(CATEGORY_EXTENDED_FOOD_STATS_COMMENT);
		config.getCategory(CATEGORY_HUD).setComment(CATEGORY_HUD_COMMENT);
		config.getCategory(CATEGORY_COMPAT).setComment(CATEGORY_COMPAT_COMMENT);
		config.getCategory(CATEGORY_COMPAT_APPLESKIN).setComment(CATEGORY_COMPAT_APPLESKIN_COMMENT);
		config.getCategory(CATEGORY_ITEMS).setComment(CATEGORY_ITEMS_COMMENT);
		
		globalCap = config.get(CATEGORY_EXTENDED_FOOD_STATS, GLOBAL_CAP, -1, GLOBAL_CAP_COMMENT).getInt(-1);
		inc = config.get(CATEGORY_EXTENDED_FOOD_STATS, INCREASE, 2, INCREASE_COMMENT).getInt(2);
		heartyShankHunger = config.get(CATEGORY_ITEMS, SHANK_HUNGER, 4, HEARTY_SHANK_HUNGER_COMMENT).getInt(4);
		heartyShankSat = (float)config.get(CATEGORY_ITEMS, SHANK_SAT, 0.8, HEARTY_SHANK_SAT_COMMENT).getDouble(0.8);
		style = config.get(CATEGORY_HUD, DISPLAY, "OVERLAY", DISPLAY_COMMENT, validDisplay).getString();
		Collections.addAll(configContents, config.get(CATEGORY_HUD, HCOLOURS, defaultHColours, HUNGER_COLOURS_COMMENT).getStringList());
		convertToColoursAndEmpty(hungerColours, configContents);
		Collections.addAll(configContents, config.get(CATEGORY_HUD, SCOLOURS, defaultSColours, SAT_COLOURS_COMMENT).getStringList());
		convertToColoursAndEmpty(satColours, configContents);
		shouldDrawExhaustion = config.get(CATEGORY_COMPAT_APPLESKIN, EXHAUST, true, EXHAUSTION_COMMENT).getBoolean(true);
		shouldDrawVanillaSat = config.get(CATEGORY_COMPAT_APPLESKIN, VANILLA_SAT, true, VANILLA_SATURATION_OVERLAY_COMMENT).getBoolean(true);
		
		if(config.hasChanged())
		{
			save();
		}
	}
	
	private static void convertToColoursAndEmpty(ArrayList<Colour> lst, ArrayList<String> contents)
	{
		for(String str : contents)
		{
			lst.add(new Colour(str));
		}
		contents.clear();
	}
}
