package yeelp.scalingfeast.init;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.enchantments.*;

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
