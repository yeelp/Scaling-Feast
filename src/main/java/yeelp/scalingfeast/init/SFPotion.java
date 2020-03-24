package yeelp.scalingfeast.init;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.potion.PotionIronStomach;
import yeelp.scalingfeast.potion.PotionMetabolism;

public class SFPotion 
{
	public static Potion metabolism;
	public static Potion ironstomach;
	public static PotionType metabolic;
	public static PotionType metabolicStrong;
	public static PotionType metabolicLong;
	
	@SubscribeEvent
	public void registerPotion(RegistryEvent.Register<Potion> evt)
	{
		metabolism = new PotionMetabolism();
		ironstomach = new PotionIronStomach();
		evt.getRegistry().register(metabolism);
		evt.getRegistry().register(ironstomach);
	}
	
	@SubscribeEvent
	public void registerPotionTypes(RegistryEvent.Register<PotionType> evt)
	{
		metabolic = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 60*20)});
		metabolicStrong = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 30*20, 1)});
		metabolicLong = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 120*20)});
		metabolic.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism"));
		metabolicStrong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_strong"));
		metabolicLong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_extended"));
		
		evt.getRegistry().register(metabolic);
		evt.getRegistry().register(metabolicStrong);
		evt.getRegistry().register(metabolicLong);
	}
}
