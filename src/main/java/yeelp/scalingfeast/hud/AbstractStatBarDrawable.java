package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import yeelp.scalingfeast.util.Colour;

import javax.annotation.Nullable;

/**
 * A drawable stat bar component
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractStatBarDrawable extends AbstractDrawable {

	private final boolean hasHungerOverlay;
	private static final int HUNGER_U_OFFSET = 27;

	/**
	 * Make a drawable stat bar component.
	 * 
	 * @param name          the profiler name.
	 * @param hungerOverlay If this drawable has an overlay to draw if the player
	 *                      has the hunger status effect.
	 */
	public AbstractStatBarDrawable(String name, boolean hungerOverlay) {
		super(name, true, false);
		this.hasHungerOverlay = hungerOverlay;
	}

	/**
	 * Make a drawable stat bar component.
	 * 
	 * @param name the profiler name
	 */
	public AbstractStatBarDrawable(String name) {
		this(name, false);
	}

	/**
	 * Bind the correct texture map.
	 * 
	 * @param mc the Minecraft client.
	 */
	protected abstract void bindTexture(Minecraft mc);

	/**
	 * Get the amount of icons to draw. This returns a float specifically to
	 * indicate the presence of half shanks or incomplete saturation.
	 * 
	 * @param player player.
	 * @param stats  the player's stats.
	 * @return the amount of icons to draw.
	 */
	protected abstract float getAmountToDraw(EntityPlayer player, FoodStats stats);

	/**
	 * The base u coord on the texture map that will be drawn. May depend on player
	 * state.
	 * 
	 * @param player player.
	 * @return the u coord on the texture map to start from.
	 */
	protected abstract int getBaseUCoord(EntityPlayer player);

	/**
	 * The base v coord on the texture map that will be drawn. May depend on player
	 * state.
	 * 
	 * @param player player.
	 * @return the v coord on the texture map to start from.
	 */
	protected abstract int getBaseVCoord(EntityPlayer player);

	/**
	 * The Colour to colour the textures. If no colour is needed, null can be
	 * returned. The colour may depend on player state.
	 * 
	 * @param player player
	 * @return The colour to colour textures, or null if no colouring is needed.
	 */
	@Nullable
	protected abstract Colour getColour(EntityPlayer player);

	/**
	 * Shift the u coord on the texture map for drawing a half shank.
	 * 
	 * @param excess The excess amount of shanks left over.
	 * @return the u coord shift amount.
	 */
	protected abstract int uCoordShift(float excess);

	/**
	 * Get the offset into the jitter array for this drawable.
	 * 
	 * @param player the player
	 * @return the index to start at for the jitter amount for this drawable.
	 */
	protected abstract int jitterIndexOffset(EntityPlayer player);

	@Override
	protected void draw(Minecraft mc, EntityPlayer player, FoodStats stats, int left, int top) {
		this.bindTexture(mc);
		int x, y, u = this.getBaseUCoord(player), v = this.getBaseVCoord(player),
				jitterOffset = this.jitterIndexOffset(player);
		Colour colour = this.getColour(player);
		float amount = this.getAmountToDraw(player, stats);
		boolean hasHungerEffect = player.isPotionActive(MobEffects.HUNGER);
		for(int i = 0; i < amount; i++) {
			x = left - i * 8 - 9;
			y = top + DrawUtils.getJitterAmount()[i + jitterOffset];
			float excess = amount - i;
			boolean isDrawingHalfShank = excess < 1;
			int newU = u + (isDrawingHalfShank ? this.uCoordShift(excess) : 0);
			AbstractDrawable.drawIcon(mc, x, y, newU, v, colour);
			if(this.hasHungerOverlay && hasHungerEffect) {
				AbstractDrawable.drawIcon(mc, x, y, newU + HUNGER_U_OFFSET, v, null);
			}
		}
		GUIIcons.unbindSFGUIIcons(mc);
	}

}
