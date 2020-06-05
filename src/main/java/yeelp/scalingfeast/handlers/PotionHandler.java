package yeelp.scalingfeast.handlers;

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import yeelp.scalingfeast.init.SFPotion;

public class PotionHandler extends Handler
{
	@SubscribeEvent
	public void onPlayerExhaustion(ExhaustionEvent.ExhaustionAddition evt)
	{
		PotionEffect ironstomach = evt.player.getActivePotionEffect(SFPotion.ironstomach);
		PotionEffect softstomach = evt.player.getActivePotionEffect(SFPotion.softstomach);
		int netEffect = 0;
		if(ironstomach != null)
		{
			netEffect += (ironstomach.getAmplifier() + 1);
		}
		if(softstomach != null)
		{
			netEffect -= (softstomach.getAmplifier() + 1);
		}
		if(netEffect != 0)
		{
			if(netEffect >= 5)
			{
				evt.deltaExhaustion = 0;
			}
			else
			{
				double reduction = netEffect*0.2;
				evt.deltaExhaustion *= (1-reduction);
			}
		}
	}
}
