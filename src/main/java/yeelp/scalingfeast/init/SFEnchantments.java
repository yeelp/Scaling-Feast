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
	public static SFEnchantmentBase fasting;
	public static SFEnchantmentBase gluttony;
	public static SFEnchantmentBase famine;
	public static SFEnchantmentBase eternalfeast;
	public static SFEnchantmentBase sensitivityCurse;
	public static SFEnchantmentBase lazinessCurse;
	public static SFEnchantmentBase deprivationCurse;

	public static void init() {
		fasting = new EnchantmentFasting();
		gluttony = new EnchantmentGluttony();
		famine = new EnchantmentFamine();
		eternalfeast = new EnchantmentEternalFeast();
		sensitivityCurse = new CurseSensitivity();
		lazinessCurse = new CurseLaziness();
		deprivationCurse = new CurseDeprivation();
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
