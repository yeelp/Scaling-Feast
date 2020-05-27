package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;

public enum SaturationScaling
{
	MAX_HUNGER(1.0f),
	HALF_HUNGER(2.0f),
	QUARTER_HUNGER(4.0f);
	private float divisor;
	private SaturationScaling(float divisor)
	{
		this.divisor = divisor;
	}
	
	/**
	 * Cap a saturation value
	 * @param player player to target
	 * @return the capped saturation
	 */
	public float getCap(EntityPlayer player)
	{
		return ScalingFeastAPI.accessor.getModifiedFoodCap(player)/this.divisor;
	}
	
	/**
	 * Cap a specific saturation value
	 * @param sat saturation value to cap
	 * @return the result of capping this saturation value with the current scaling.
	 */
	public float clampSaturation(float sat)
	{
		return sat/this.divisor;
	}
}
