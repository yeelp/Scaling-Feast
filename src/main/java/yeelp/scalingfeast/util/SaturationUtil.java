package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;

public final class SaturationUtil
{
	/**
	 * cap a player's saturation, first based on scaling, then off the hard cap.
	 * @param player
	 */
	public static final void capSaturation(EntityPlayer player)
	{
		ModConfig.foodCap.satScaling.cap(player);
		if(player.getFoodStats().getSaturationLevel() > (float) ModConfig.foodCap.satCap)
		{
			AppleCoreAPI.mutator.setSaturation(player, (float) ModConfig.foodCap.satCap);
		}
	}
}
