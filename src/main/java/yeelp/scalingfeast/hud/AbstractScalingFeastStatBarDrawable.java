package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.GUIIcons;

public abstract class AbstractScalingFeastStatBarDrawable extends AbstractStatBarDrawable {

	private final int u, v;
	
	public AbstractScalingFeastStatBarDrawable(String name, int u, int v, boolean hasHungerOverlay) {
		super(name, hasHungerOverlay);
		this.u = u;
		this.v = v;
	}
	
	public AbstractScalingFeastStatBarDrawable(String name, int u, int v) {
		this(name, u, v, false);
	}

	@Override
	protected final void bindTexture(Minecraft mc) {
		GUIIcons.bindSFGUIIcons(mc);
	}

	@Override
	protected final int getBaseUCoord(EntityPlayer player) {
		return this.u;
	}
	
	@Override
	protected final int getBaseVCoord(EntityPlayer player) {
		return this.v;
	}

	@Override
	protected final int jitterIndexOffset(EntityPlayer player) {
		return 0;
	}
	
	protected static final float getIconCountForTopmostBar(float amount) {
		return Math.min(ModConsts.VANILLA_MAX_SAT, (((amount - 1) % ModConsts.VANILLA_MAX_HUNGER) + 1))/2.0f;
	}

}
