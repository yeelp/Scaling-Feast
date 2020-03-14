package yeelp.scalingfeast.init;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.potion.PotionIronStomach;
import yeelp.scalingfeast.potion.PotionMetabolism;

public class SFPotion 
{
	public static Potion metabolism;
	public static Potion ironstomach;
	
	@SubscribeEvent
	public void registerPotion(RegistryEvent.Register<Potion> evt)
	{
		metabolism = new PotionMetabolism();
		ironstomach = new PotionIronStomach();
		evt.getRegistry().register(metabolism);
		evt.getRegistry().register(ironstomach);
	}
}
