package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;

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
	 * Cap a player's saturation based on scaling
	 * @param player player to cap
	 */
	@Deprecated
	public void cap(EntityPlayer player)
	{
		float currSat = player.getFoodStats().getSaturationLevel();
		float maxSat = FoodCapProvider.getMaxFoodLevel(player)/this.divisor;
		AppleCoreAPI.mutator.setSaturation(player, currSat <= maxSat ? currSat : maxSat);
	}
	
	/**
	 * Cap a saturation value
	 * @param player player tp target
	 * @return the capped saturation
	 */
	public float getCap(EntityPlayer player)
	{
		return FoodCapProvider.getMaxFoodLevel(player)/this.divisor;
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
