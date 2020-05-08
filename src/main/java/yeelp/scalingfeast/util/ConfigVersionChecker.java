package yeelp.scalingfeast.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import net.minecraftforge.common.config.Configuration;
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
		ScalingFeast.info(ConfigVersionChecker.currentVersion.toString()+", " + this.encounteredVersion.toString());
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
			it.next(); //# Configuration file
			String ver = it.next(); //~CONFIG VERSION:...
			if(ver.startsWith("~CONFIG VERSION:"))
			{
				String verNum = ver.substring(ver.indexOf(":")+1);
				ScalingFeast.info(verNum);
				return new ConfigVersion(verNum.trim());
			}
			else
			{
				return ConfigVersion.UNVERSIONED;
			}
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
}
