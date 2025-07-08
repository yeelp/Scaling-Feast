package yeelp.scalingfeast.hud;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.util.Colour;

/**
 * Skeletal drawable. Also contains a couple useful static methods for children.
 * 
 * @author Yeelp
 *
 */
@SideOnly(Side.CLIENT)
abstract class AbstractDrawable implements IDrawable {
	private final String name;
	private final boolean blend, alpha;

	/**
	 * Make a new Drawable component.
	 * 
	 * @param name  The section name used in Minecraft's Profiler.
	 * @param blend if this component needs blend.
	 * @param alpha if this component needs alpha.
	 */
	public AbstractDrawable(String name, boolean blend, boolean alpha) {
		this.name = name;
		this.blend = blend;
		this.alpha = alpha;
	}

	@Override
	public String getSectionName() {
		return this.name;
	}

	@Override
	public boolean requiresBlend() {
		return this.blend;
	}

	@Override
	public boolean requiresAlpha() {
		return this.alpha;
	}

	@Override
	public void draw(Minecraft mc, EntityPlayer player, int left, int top) {
		mc.profiler.startSection(this.getSectionName());
		if(this.requiresBlend()) {
			GlStateManager.enableBlend();
		}
		if(this.requiresAlpha()) {
			GlStateManager.enableAlpha();
		}
		this.draw(mc, player, player.getFoodStats(), left, top);
		if(this.requiresBlend()) {
			GlStateManager.disableBlend();
		}
		if(this.requiresAlpha()) {
			GlStateManager.disableAlpha();
		}
		mc.profiler.endSection();
	}

	/**
	 * actually draw.
	 * 
	 * @param mc     The Minecraft client.
	 * @param player the client player.
	 * @param stats  The player's food stats.
	 * @param left   left bound of drawing area.
	 * @param top    right bound of drawing area.
	 */
	abstract protected void draw(Minecraft mc, EntityPlayer player, FoodStats stats, int left, int top);

	/**
	 * Draw a single icon. Will call {@code glColor3f(1.0f, 1.0f, 1.0f)} after.
	 * 
	 * @param mc     the Minecraft client.
	 * @param x      x pos of the texture.
	 * @param y      y pos of the texture.
	 * @param u      u coord on texture map.
	 * @param v      v coord on texture map.
	 * @param colour colour, if null, will not change colour with glColor.
	 */
	protected static void drawIcon(Minecraft mc, int x, int y, int u, int v, @Nullable Colour colour) {
		if(colour != null) {
			GL11.glColor3ub(colour.getR(), colour.getG(), colour.getB());
		}
		mc.ingameGUI.drawTexturedModalRect(x, y, u, v, 9, 9);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

	/**
	 * Draw a single icon with alpha. Will call
	 * {@code glColor4f(1.0f, 1.0f, 1.0f, 1.0f)} after.
	 * 
	 * @param mc     the Minecraft client
	 * @param x      x pos of the texture
	 * @param y      y pos of the texture
	 * @param u      u coord on texture map
	 * @param v      v coord on texture map
	 * @param colour colour
	 * @param alpha  alpha
	 */
	protected static void drawIcon(Minecraft mc, int x, int y, int u, int v, @Nonnull Colour colour, short alpha) {
		GL11.glColor4ub(colour.getR(), colour.getG(), colour.getB(), (byte) alpha);
		mc.ingameGUI.drawTexturedModalRect(x, y, u, v, 9, 9);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

}
