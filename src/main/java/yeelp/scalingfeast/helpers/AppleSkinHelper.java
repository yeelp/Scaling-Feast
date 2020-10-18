package yeelp.scalingfeast.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;

/**
 * Helper class for AppleSkin. Uses reflection
 * @author Yeelp
 *
 */
public class AppleSkinHelper
{
	private static Class<?> overlay;
	private static Class<?> config;
	private static Class<?> appleSkinBase;
	private static boolean loaded = false;
	
	/**
	 * Initialize this helper class.
	 * @throws ClassNotFoundException if the classes used for reflection from AppleSkin can't be found.
	 */
	public static void init() throws ClassNotFoundException
	{
		overlay = Class.forName("squeek.appleskin.client.HUDOverlayHandler");
		config = Class.forName("squeek.appleskin.ModConfig");
		appleSkinBase = Class.forName("squeek.appleskin.AppleSkin"); 
		loaded = true;
	}
	
	/**
	 * Has this class loaded?
	 * @return true if AppleSkinHelper.init() has returned successfully, i.e. the class has loaded.
	 */
	public static boolean isLoaded()
	{
		return loaded;
	}
	
	/**
	 * Enable AppleCore recognition
	 */
	public static void enableAppleCoreRecognition()
	{
		try
		{
			appleSkinBase.getDeclaredField("hasAppleCore").setBoolean(null, true);
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			ScalingFeast.err("Couldn't get AppleCore recognition for AppleSkin.");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
		}
	}
	
	/**
	 * Disable AppleCore recognition
	 */
	public static void disableAppleCoreRecognition()
	{
		try
		{
			appleSkinBase.getDeclaredField("hasAppleCore").setBoolean(null, false);
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			ScalingFeast.err("Couldn't get AppleCore recognition for AppleSkin.");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
		}
	}
	
	/**
	 * Get AppleSkin to draw exhaustion
	 * @param exhaustion player's exhaustion
	 * @param mc Minecraft instance
	 * @param left x coord to start to draw
	 * @param top y coord to start to draw
	 * @param alpha transparency
	 * @return true if the operation was successful, false if not. If false, chances are problems will persist, so it's best to not call this method after it returns false as it will be a waste of processing.
	 */
	public static boolean drawExhaustion(float exhaustion, Minecraft mc, int left, int top, float alpha)
	{
		try
		{
			enableAppleCoreRecognition();
			if(config.getDeclaredField("SHOW_FOOD_EXHAUSTION_UNDERLAY").getBoolean(null))
			{
				overlay.getDeclaredMethod("drawExhaustionOverlay", float.class, Minecraft.class, int.class, int.class, float.class).invoke(null, exhaustion, mc, left, top, alpha); 
			}
			return true;
		}
		catch(NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e)
		{
			ScalingFeast.err("Scaling Feast couldn't use AppleSkin to draw exhaustion! Something went wrong here, here's a stack trace.");
			ScalingFeast.err(Arrays.toString(e.getStackTrace()));
			return false;
		}
	}
}