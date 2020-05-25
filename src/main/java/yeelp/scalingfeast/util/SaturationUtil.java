package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;

public final class SaturationUtil
{
	/**
	 * cap a player's saturation, first based on scaling, then off the hard cap.
	 * @param player player to cap
	 */
	public static final void capSaturation(EntityPlayer player)
	{
		ModConfig.foodCap.satScaling.cap(player);
		if(player.getFoodStats().getSaturationLevel() > (float) ModConfig.foodCap.satCap)
		{
			AppleCoreAPI.mutator.setSaturation(player, (float) ModConfig.foodCap.satCap);
		}
	}

	/**
	 * Get the saturation cap for a player. This is the same value {@link #capSaturation(EntityPlayer)} will cap a player's saturation to.
	 * @param player player to get saturation cap for.
	 * @return this player's saturation cap.
	 */
	public static final float getSaturationCapForPlayer(EntityPlayer player)
	{
		float maxSatByScaling = ModConfig.foodCap.satScaling.getCap(player);
		return maxSatByScaling <= ModConfig.foodCap.satCap ? maxSatByScaling : (float) ModConfig.foodCap.satCap;
	}
	
	public static final float capSaturationValue(float sat)
	{
		float maxSatByScaling = ModConfig.foodCap.satScaling.clampSaturation(sat);
		return maxSatByScaling <= ModConfig.foodCap.satCap ? maxSatByScaling : (float) ModConfig.foodCap.satCap; 
	}
}
