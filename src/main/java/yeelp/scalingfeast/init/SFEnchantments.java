package yeelp.scalingfeast.init;

import net.minecraft.enchantment.Enchantment;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.enchantments.*;

/**
 * Initialize all the enchantments
 * @author Yeelp
 *
 */
public class SFEnchantments 
{
	public static Enchantment fasting;
	public static Enchantment gluttony;
	
	@SubscribeEvent
	public void registerEnchantments(RegistryEvent.Register<Enchantment> event)
	{
		fasting = new EnchantmentFasting();
		gluttony = new EnchantmentGluttony();
		event.getRegistry().register(fasting);
		event.getRegistry().register(gluttony);
	}
}
