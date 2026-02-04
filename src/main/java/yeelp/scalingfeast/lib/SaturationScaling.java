package yeelp.scalingfeast.lib;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;

public enum SaturationScaling {
	MAX_HUNGER(1.0f),
	HALF_HUNGER(2.0f),
	QUARTER_HUNGER(4.0f);

	private final float divisor;

	SaturationScaling(float divisor) {
		this.divisor = divisor;
	}

	/**
	 * Cap a saturation value
	 * 
	 * @param player player to target
	 * @return the capped saturation
	 */
	public float getCap(EntityPlayer player) {
		return AppleCoreAPI.accessor.getMaxHunger(player) / this.divisor;
	}

	/**
	 * Cap a specific saturation value
	 * 
	 * @param sat saturation value to cap
	 * @return the result of capping this saturation value with the current scaling.
	 */
	public float clampSaturation(float sat) {
		return sat / this.divisor;
	}
}
