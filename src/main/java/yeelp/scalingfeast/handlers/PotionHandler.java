package yeelp.scalingfeast.handlers;

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import yeelp.scalingfeast.init.SFPotion;

public class PotionHandler extends Handler
{
	/*@SubscribeEvent
	public void onAddExhaution(ExhaustionEvent.ExhaustionAddition evt)
	{
		PotionEffect ironstomachEffect = evt.player.getActivePotionEffect(SFPotion.ironstomach);
		if(ironstomachEffect == null)
		{
			return;
		}
		else
		{
			double reduction = (ironstomachEffect.getAmplifier() + 1)*0.2;
			evt.deltaExhaustion *= (1-reduction > 0 ? 1-reduction : 0);
		}
	}*/
}
