package yeelp.scalingfeast.init;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.potion.PotionMetabolism;

public class SFPotion 
{
	public static Potion metabolism;
	
	@SubscribeEvent
	public void registerPotion(RegistryEvent.Register<Potion> evt)
	{
		metabolism = new PotionMetabolism(false, 0xF0B78C);
		evt.getRegistry().register(metabolism);
	}
}
