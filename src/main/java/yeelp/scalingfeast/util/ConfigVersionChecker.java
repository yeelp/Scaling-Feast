package yeelp.scalingfeast.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import scala.actors.threadpool.Arrays;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;

/**
 * A simple config version checker.
 * @author Yeelp
 *
 */
public final class ConfigVersionChecker
{
	private final Class<? extends Configuration> modConfig;
	private final File configFile;
	private ConfigVersion encounteredVersion;
	private static final ConfigVersion currentVersion = new ConfigVersion(ModConsts.CONFIG_VERSION);
	/**
	 * Set up a new ConfigVersionChecker
	 * @param modConfig The mod config class.
	 * @param configFile the mod's config file.
	 */
	public ConfigVersionChecker(Class<? extends Configuration> modConfig, File configFile)
	{
		this.modConfig = modConfig;
		this.configFile = configFile;
		try
		{
			this.encounteredVersion = getConfigVersion(this.configFile);
		}
		catch(IOException e)
		{
			ScalingFeast.err("Something went wrong getting the config version! Setting to UNVERSIONED");
			this.encounteredVersion = ConfigVersion.UNVERSIONED;
		}
	}
	
	/**
	 * is the config outdated?
	 * @return true if the config file found has a version before this one.
	 * @throws RuntimeException if the config file found has a future version.
	 */
	public boolean isConfigOutdated() throws RuntimeException
	{
		int comp = this.encounteredVersion.compareTo(ConfigVersionChecker.currentVersion);
		if(comp > 0)
		{
			throw new RuntimeException("Scaling Feast encountered a future config version. Please back it up and remove it.");
		}
		else
		{
			return comp == 0 ? false : true;
		}
	}

	/**
	 * Get the config version from the file.
	 * @param config config file to get version from.
	 * @return The ConfigVersion object that represents the ConfigVersion, ConfigVersion.UNVERSIONED, if it wasn't possible to find the config version, or null, if an error occured.
	 * @throws IOException If this method's BufferedReader throws an IOException when trying to invoke close().
	 */
	public static ConfigVersion getConfigVersion(File config) throws IOException
	{
		try(BufferedReader br = new BufferedReader(new FileReader(config)))
		{
			Iterator<String> it = br.lines().iterator();
			while(it.hasNext())
			{
				String s = it.next().trim();
				if(s.startsWith("S:\"Config Version\""))
				{
					String verNum = s.substring(s.indexOf("=")+1);
					return new ConfigVersion(verNum.trim());
				}
			}
			return ConfigVersion.UNVERSIONED;
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
	}
	
	/**
	 * Get the current config version. The one this version of Scaling Feast uses.
	 * @return the current config version.
	 */
	public static ConfigVersion getCurrentConfigVersion()
	{
		return ConfigVersionChecker.currentVersion;
	}

	/**
	 * Update the config file to match the current version.
	 */
	public void updateConfig()
	{
		boolean spiceoflife = false;
		boolean solcarrot = false;
		try(BufferedReader reader = new BufferedReader(new FileReader(this.configFile)))
		{
			if(this.encounteredVersion.equals(ConfigVersion.UNVERSIONED))
			{
				Iterator<String> it = reader.lines().iterator();
				while(it.hasNext())
				{
					String s = it.next().trim();
					if(s.equals("\"spice of life\" {"))
					{
						solcarrot = false;
						spiceoflife = true;
					}
					else if(s.equals("\"spice of life: carrot edition\" {"))
					{
						solcarrot = true;
						spiceoflife = false;
					}
					//Single value config options (ones with =)
					if(s.contains("="))
					{
						String[] option = s.split("=");
						switch(option[0])
						{
							//##### FOOD CAP #####//
							case "I:\"Global Cap\"":
								ModConfig.foodCap.globalCap = Short.parseShort(option[1]);
								break;
							case "I:\"Increase Per Hearty Shank Eaten\"":
								ModConfig.foodCap.inc = Short.parseShort(option[1]);
								break;
							case "I:\"Starting Hunger\"":
								ModConfig.foodCap.startingHunger = Short.parseShort(option[1]);
								break;
							case "I:\"Hunger Lost on Death\"":
								ModConfig.foodCap.death.hungerLossOnDeath = Short.parseShort(option[1]);
								break;
							case "I:\"Max Lost on Death\"":
								ModConfig.foodCap.death.maxLossAmount = Short.parseShort(option[1]);
								break;
							case "I:\"Decrease Amount on Starvation\"":
								ModConfig.foodCap.starve.starveLoss = Short.parseShort(option[1]);
								break;
							case "B:\"Frequency Reset\"":
								ModConfig.foodCap.starve.doesFreqReset = Boolean.parseBoolean(option[1]);
								break;
							case "B:\"Frequency Reset on Penalty\"":
								ModConfig.foodCap.starve.doesFreqResetOnStarve = Boolean.parseBoolean(option[1]);
								break;
							case "B:\"Reset Counter For Hearty Shank?\"":
								ModConfig.foodCap.starve.shankResetsCounter = Boolean.parseBoolean(option[1]);
								break;
							case "I:\"Starvation Loss Frequency\"":
								ModConfig.foodCap.starve.lossFreq = Short.parseShort(option[1]);
								break;
							case "I:\"Starvation Loss Lower Bound\"":
								ModConfig.foodCap.starve.starveLowerCap = Short.parseShort(option[1]);
								break;
							//##### COMPAT #####//
							case "B:\"Enable Compatibility Settings\"":
								ModConfig.compat.shouldFirePost = Boolean.parseBoolean(option[1]);
								break;
							//##### ITEMS #####//
							case "B:\"Enable Brewing Recipes\"":
								ModConfig.items.enableMetabolicRecipes = Boolean.parseBoolean(option[1]);
								break;
							case "B:\"Enable Metabolic Potions\"":
								ModConfig.items.enableMetabolicPotion = Boolean.parseBoolean(option[1]);
								break;
							case "I:\"Hearty Shank Hunger Value\"":
								ModConfig.items.heartyShankFoodLevel = Integer.parseInt(option[1]);
								break;
							case "D:\"Hearty Shank Saturation Modifier\"":
								ModConfig.items.heartyShankSatLevel = Double.parseDouble(option[1]);
								break;
							//##### HUD #####//
							case "I:\"ADVANCED info text x offset\"":
								ModConfig.hud.infoXOffset = Integer.parseInt(option[1]);
								break;
							case "I:\"ADVANCED info text y offset\"":
								ModConfig.hud.infoYOffset = Integer.parseInt(option[1]);
								break;
							case "S:\"Display Style\"":
								ModConfig.hud.style = ModConfig.HUDCategory.DisplayStyle.valueOf(option[1]);
								break;
							case "B:\"Draw Saturation?\"":
								ModConfig.hud.drawSaturation = Boolean.parseBoolean(option[1]);
								break;
							case "S:\"Info Style\"":
								ModConfig.hud.infoStyle = ModConfig.HUDCategory.InfoStyle.valueOf(option[1]);
								break;
							case "S:\"Max Custom Colour End\"":
								ModConfig.hud.maxColourEnd = option[1];
								break;
							case "S:\"Max Custom Colour Start\"":
								ModConfig.hud.maxColourStart = option[1];
								break;
							case "S:\"Max Outline Colour Style\"":
								ModConfig.hud.maxColourStyle = ModConfig.HUDCategory.MaxColourStyle.valueOf(option[1]);
								break;
							case "D:\"Max Outline Transparency\"":
								ModConfig.hud.maxOutlineTransparency = Double.parseDouble(option[1]);
								if(ModConfig.hud.maxOutlineTransparency < 0 || 1 < ModConfig.hud.maxOutlineTransparency)
								{
									ScalingFeast.warn("Max Outline Transparency not in the range [0, 1]! Setting = 0.5...");
									ModConfig.hud.maxOutlineTransparency = 0.5;
								}
								break;
							case "S:\"Overlay Style\"":
								ModConfig.hud.overlayStyle = ModConfig.HUDCategory.OverlayStyle.valueOf(option[1]);
								break;
							case "S:\"Replace Vanilla Hunger\"":
								ModConfig.hud.replaceVanilla = Boolean.parseBoolean(option[1]);
								break;
							case "S:\"Saturation Text Colour\"":
								ModConfig.hud.satTextColour = option[1];
								break;
							case "S:\"Saturation Text Colour Empty\"":
								ModConfig.hud.satTextColourEmpty = option[1];
								break;
							//##### MODULES #####//
							case "B:\"Disable Hearty Shank Effects\"":
								ModConfig.modules.isShankDisabled = Boolean.parseBoolean(option[1]);
								break;
							case "B:Enabled":
								if(spiceoflife)
								{
									ModConfig.modules.spiceoflife.enabled = Boolean.parseBoolean(option[1]);
								}
								else if (solcarrot)
								{
									ModConfig.modules.sol.enabled = Boolean.parseBoolean(option[1]);
								}
								else
								{
									ScalingFeast.err("Scaling Feast ran into problems parsing the Enabled field of the config. Please report this to the mod author on GitHub!");
								}
								break;
							case "I:Penalty":
								ModConfig.modules.spiceoflife.penalty = Short.parseShort(option[1]);
								break;
							case "I:\"Required Amount\"":
								ModConfig.modules.spiceoflife.uniqueRequired = Integer.parseInt(option[1]);
								break;
							case "B:\"Use Food Groups\"":
								ModConfig.modules.spiceoflife.useFoodGroups = Boolean.parseBoolean(option[1]);
								break;
							case "B:\"Reward Messages Above Hotbar?\"":
								ModConfig.modules.sol.rewardMsgAboveHotbar = Boolean.parseBoolean(option[1]);
								break;
						}
					}
					//array config options (lists)
					else if(s.startsWith("S:") && s.endsWith(" <"))
					{
						ArrayList<String> lst = new ArrayList<String>();
						for(String t = it.next().trim(); !t.equals(">"); t = it.next().trim())
						{
							lst.add(t);
						}
						String[] arr = new String[lst.size()];
						for(int i = 0; i < lst.size(); i++)
						{
							arr[i] = lst.get(i);
						}
						//##### HUD #####//
						if(s.contains("Hunger Overlay Colours"))
						{
							ModConfig.hud.Hcolours = arr;
						}
						else if(s.contains("Saturation Overlay Colours"))
						{
							ModConfig.hud.Scolours = arr;
						}
						//##### MODULES #####//
						else if(s.contains("Milestones"))
						{
							ModConfig.modules.sol.milestones = arr;
						}
					}
					else
					{
						continue;
					}		
				}
				ConfigManager.sync(ModConsts.MOD_ID, Config.Type.INSTANCE);
				ModConfig.scrubConfig();
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
		catch (NumberFormatException e)
		{
			ScalingFeast.err("Scaling Feast encountered a problem trying to parse your config options! You should make sure to transfer the config options yourself!");
			return;
		}
		catch(NoSuchElementException e)
		{
			ScalingFeast.err("Scaling Feast ran into problems parsing the config file! Report the following stack trace to the mod author!");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
		}
	}
}
