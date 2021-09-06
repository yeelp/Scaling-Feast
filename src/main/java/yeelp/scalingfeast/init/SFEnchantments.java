package yeelp.scalingfeast.init;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.enchantments.CurseDeprivation;
import yeelp.scalingfeast.enchantments.CurseLaziness;
import yeelp.scalingfeast.enchantments.CurseSensitivity;
import yeelp.scalingfeast.enchantments.EnchantmentEternalFeast;
import yeelp.scalingfeast.enchantments.EnchantmentFamine;
import yeelp.scalingfeast.enchantments.EnchantmentFasting;
import yeelp.scalingfeast.enchantments.EnchantmentGluttony;
import yeelp.scalingfeast.enchantments.SFEnchantmentBase;

/**
 * Initialize all the enchantments
 * 
 * @author Yeelp
 *
 */
public class SFEnchantments {
	public static SFEnchantmentBase fasting = new EnchantmentFasting();
	public static SFEnchantmentBase gluttony = new EnchantmentGluttony();
	public static SFEnchantmentBase famine = new EnchantmentFamine();
	public static SFEnchantmentBase eternalfeast = new EnchantmentEternalFeast();
	public static SFEnchantmentBase sensitivityCurse = new CurseSensitivity();
	public static SFEnchantmentBase lazinessCurse = new CurseLaziness();
	public static SFEnchantmentBase deprivationCurse = new CurseDeprivation();

	public static void init() {
		ImmutableList.of(fasting, gluttony, famine, eternalfeast, sensitivityCurse, lazinessCurse, deprivationCurse).forEach(SFEnchantments::tryRegisterEnchantment);
	}
	
	private static void tryRegisterEnchantment(SFEnchantmentBase enchantment) {
		if(enchantment.enabled()) {
			ForgeRegistries.ENCHANTMENTS.register(enchantment);
			enchantment.onRegister();
		}
		else if (enchantment.shouldRegisterHandlerAnyway()) {
			enchantment.onRegister();
		}
	}
}
