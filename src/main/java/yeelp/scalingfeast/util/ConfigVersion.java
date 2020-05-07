package yeelp.scalingfeast.util;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
/**
 * Simple Config Versions. This class expects versions to follow the standard <a href=https://semver.org/>semantic versioning</a> and currently follows Semantic Versioning 2.0.0
 * This class is immutable.
 * @author Yeelp
 *
 */
public final class ConfigVersion implements Comparable<ConfigVersion>
{
	private static final Pattern reg = Pattern.compile("^(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)\\.(?<patch>0|[1-9]\\d*)(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$");
	private final short major;
	private final short minor;
	private final short patch;
	private final String prerelease;
	private final String buildMeta;
	public static final ConfigVersion UNVERSIONED = new ConfigVersion();
	
	private ConfigVersion()
	{
		this.major = -1;
		this.minor = -1;
		this.patch = -1;
		this.prerelease = null;
		this.buildMeta = null;
	}
	/**
	 * Construct a new Config Version. This constructor does strictly enforce Semantic Versioning. 
	 * @param ver a String to build a config version from. Must be of the form: x.y.z-pre+build where the -pre+build portion is optional. Either or both may be excluded.
	 * @throws IllegalArgumentException if the String passed isn't a valid Semver string.
	 */
	public ConfigVersion(String ver)
	{
		final Matcher m = reg.matcher(ver);
		if(m.matches())
		{
			major = Short.parseShort(m.group("major"));
			minor = Short.parseShort(m.group("minor"));
			patch = Short.parseShort(m.group("patch"));
			prerelease = m.group("prerelease");
			buildMeta = m.group("buildmetadata");
		}
		else
		{
			throw new IllegalArgumentException("Not a valid Semver!");
		}
	}
	
	@Nonnull
	public final short getMajorVersion()
	{
		return this.major;
	}
	
	@Nonnull
	public final short getMinorVersion()
	{
		return this.minor;
	}
	
	@Nonnull
	public final short getPatchVersion()
	{
		return this.patch;
	}
	
	@Nullable
	public final String getPrereleaseVersion()
	{
		return this.prerelease;
	}
	
	@Nullable
	public final String getBuildMetadata()
	{
		return this.buildMeta;
	}
	
	@Override
	public int compareTo(ConfigVersion o)
	{
		int maj = Short.valueOf(this.major).compareTo(o.major);
		if(maj == 0)
		{
			int min = Short.valueOf(minor).compareTo(o.minor);
			if(min == 0)
			{
				return Short.valueOf(patch).compareTo(o.patch);
			}
			else
			{
				return min;
			}
		}
		else
		{
			return maj;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ConfigVersion)
		{
			final ConfigVersion that = (ConfigVersion) o;
			return this.major == that.major && this.minor == that.minor && this.patch == that.patch && this.prerelease.equals(that.prerelease) && this.buildMeta.equals(that.buildMeta);
		}
		else
		{
			return false;
		}
	}
	/**
	 * Is this config version unversioned?
	 * @return true if unversioned, false otherwise.
	 */
	public boolean isUnversioned()
	{
		return this.equals(UNVERSIONED);
	}
}
