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
	public static Enchantment sensitivityCurse;
	public static Enchantment lazinessCurse;
	public static Enchantment deprivationCurse;
	
	public static void init()
	{
		fasting = new EnchantmentFasting();
		gluttony = new EnchantmentGluttony();
		famine = new EnchantmentFamine();
		eternalfeast = new EnchantmentEternalFeast();
		sensitivityCurse = new CurseSensitivity();
		lazinessCurse = new CurseLaziness();
		deprivationCurse = new CurseDeprivation();
		
		ForgeRegistries.ENCHANTMENTS.registerAll(fasting, gluttony, famine, eternalfeast, sensitivityCurse, lazinessCurse, deprivationCurse);
	}
}
