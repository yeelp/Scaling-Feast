package yeelp.scalingfeast.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class HeartyShankUsageTickerProvider
{
	@CapabilityInject(IHeartyShankUsageTicker.class)
	public static Capability<IHeartyShankUsageTicker> ticker = null;
	
	private IHeartyShankUsageTicker instance = ticker.getDefaultInstance();
	
	public static IHeartyShankUsageTicker getTicker(EntityPlayer player)
	{
		return player.getCapability(ticker, null);
	}
}
