package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Something that will be drawn to the screen.
 * 
 * @author Yeelp
 *
 */
public interface IDrawable {

	/**
	 * Get the section name used in Minecraft's Profiler.
	 * 
	 * @return The section name.
	 */
	String getSectionName();

	/**
	 * Should this Drawable enable GL blend before drawing? If true, it will also
	 * turn off blend after drawing.
	 * 
	 * @return true if blend is needed, false if not.
	 */
	boolean requiresBlend();

	/**
	 * Should this Drawable enable GL alpha before drawing? If true, it will also
	 * turn off alpha after drawing.
	 * 
	 * @return true if alpha is needed, false if not.
	 */
	boolean requiresAlpha();

	/**
	 * Should this Drawable be drawn based on current player state?
	 * 
	 * @param player the player whose client is drawing
	 * @return true if this drawable is to be drawn.
	 */
	boolean shouldDraw(EntityPlayer player);

	/**
	 * Actually draw
	 * @param mc Minecraft client
	 * @param player player of the client
	 * @param left left part of hunger bar 
	 * @param top top part of hunger bar
	 */
	void draw(Minecraft mc, EntityPlayer player, int left, int top);
}
