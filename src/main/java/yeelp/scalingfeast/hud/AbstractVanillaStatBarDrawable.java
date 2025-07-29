package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import yeelp.scalingfeast.handlers.GUIIcons;
import yeelp.scalingfeast.util.Colour;

/**
 * A drawable stat bar that uses the Vanilla icons
 * @author Yeelp
 *
 */
public abstract class AbstractVanillaStatBarDrawable extends AbstractStatBarDrawable {

	static final int V_COORD_BASE = 27;
	
	private final int uCoordBase, hungerUOffset;
	
	/**
	 * Create a new vanilla drawable
	 * @param name The profiler name
	 */
	public AbstractVanillaStatBarDrawable(String name, int uCoordBase, int hungerUOffset) {
		super(name);
		this.uCoordBase = uCoordBase;
		this.hungerUOffset = hungerUOffset;
	}

	@Override
	public final boolean shouldDraw(EntityPlayer player) {
		return true;
	}
	
	@Override
	protected final void bindTexture(Minecraft mc) {
		GUIIcons.unbindSFGUIIcons(mc);
	}
	
	@Override
	protected final int getBaseUCoord(EntityPlayer player) {
		return this.uCoordBase + (player.isPotionActive(MobEffects.HUNGER) ? this.hungerUOffset : 0); 
	}

	@Override
	protected final int getBaseVCoord(EntityPlayer player) {
		return V_COORD_BASE;
	}

	@Override
	protected final Colour getColour(EntityPlayer player) {
		return null;
	}

	@Override
	protected final int jitterIndexOffset(EntityPlayer player) {
		return 0;
	}

}
