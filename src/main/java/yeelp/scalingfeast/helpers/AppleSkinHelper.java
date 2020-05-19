package yeelp.scalingfeast.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ScalingFeast;

/**
 * Helper class for AppleSkin. Uses reflection
 * @author Yeelp
 *
 */
public class AppleSkinHelper
{
	private static Class overlay;
	private static Class config;
	private static boolean loaded = false;
	
	/**
	 * Initialize this helper class.
	 * @throws ClassNotFoundException if the classes used for reflection from AppleSkin can't be found.
	 */
	public static void init() throws ClassNotFoundException
	{
		overlay = Class.forName("squeek.appleskin.client.HUDOverlayHandler");
		config = Class.forName("squeek.appleskin.ModConfig");
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
