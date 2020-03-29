package yeelp.scalingfeast.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
	public static Enchantment famine;
	public static Enchantment eternalfeast;
	
	public static void init()
	{
		fasting = new EnchantmentFasting();
		gluttony = new EnchantmentGluttony();
		famine = new EnchantmentFamine();
		eternalfeast = new EnchantmentEternalFeast();
		
		ForgeRegistries.ENCHANTMENTS.registerAll(fasting, gluttony, famine, eternalfeast);
	}
}
