package yeelp.scalingfeast.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.potion.PotionBloated;
import yeelp.scalingfeast.potion.PotionDeficiency;
import yeelp.scalingfeast.potion.PotionHungerMinus;
import yeelp.scalingfeast.potion.PotionHungerPlus;
import yeelp.scalingfeast.potion.PotionIronStomach;
import yeelp.scalingfeast.potion.PotionMetabolism;
import yeelp.scalingfeast.potion.PotionSoftStomach;

public class SFPotion {
	public static Potion metabolism;
	public static Potion ironstomach;
	public static Potion bloated;
	public static Potion hungerplus;
	public static Potion hungerminus;
	public static Potion softstomach;
	public static Potion deficiency;
	public static PotionType metabolic;
	public static PotionType metabolicStrong;
	public static PotionType metabolicLong;

	public static void init() {
		metabolism = new PotionMetabolism();
		ironstomach = new PotionIronStomach();
		bloated = new PotionBloated();
		hungerplus = new PotionHungerPlus();
		hungerminus = new PotionHungerMinus();
		softstomach = new PotionSoftStomach();
		deficiency = new PotionDeficiency();

		ForgeRegistries.POTIONS.registerAll(metabolism, ironstomach, bloated, hungerplus, hungerminus, softstomach, deficiency);

		if(ModConfig.items.enablePotions) {
			PotionType metabolic = new PotionType(new PotionEffect[] {
					new PotionEffect(metabolism, 120 * 20)});
			PotionType metabolicLong = new PotionType(new PotionEffect[] {
					new PotionEffect(metabolism, 240 * 20)});
			PotionType metabolicStrong = new PotionType(new PotionEffect[] {
					new PotionEffect(metabolism, 60 * 20, 1)});

			PotionType deficiencyPotion = new PotionType(new PotionEffect[] {
					new PotionEffect(deficiency, 1, 3)});
			PotionType deficiencyStrongPotion = new PotionType(new PotionEffect[] {
					new PotionEffect(deficiency, 1, 6)});

			registerPotionType(ironstomach, 150, "ironstomach", 0, 1);
			registerPotionType(bloated, 120, "bloated", 0, 1);
			registerPotionType(hungerplus, 180, "hungerplus", 3, 7);
			registerPotionType(hungerminus, 120, "hungerminus", 3, 7);
			registerPotionType(softstomach, 120, "softstomach", 0, 1);
			deficiencyPotion.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "deficiency"));
			deficiencyStrongPotion.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "deficiency_strong"));
			metabolic.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism"));
			metabolicLong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_extended"));
			metabolicStrong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_strong"));
			ForgeRegistries.POTION_TYPES.registerAll(metabolic, metabolicLong, metabolicStrong, deficiencyPotion, deficiencyStrongPotion);
			if(ModConfig.items.enableMetabolicRecipes) {
				PotionHelper.addMix(PotionTypes.THICK, SFFood.heartyshank, metabolic);
				PotionHelper.addMix(metabolic, Items.REDSTONE, metabolicLong);
				PotionHelper.addMix(metabolic, Items.GLOWSTONE_DUST, metabolicStrong);
			}
		}
	}

	private static void registerPotionType(Potion p, int baseDuration, String rootName, int baseAmplifier, int strongAmplifier) {
		PotionType normal = new PotionType(new PotionEffect[] {
				new PotionEffect(p, baseDuration * 20, baseAmplifier)});
		PotionType extended = new PotionType(new PotionEffect[] {
				new PotionEffect(p, 2 * baseDuration * 20, baseAmplifier)});
		PotionType strong = new PotionType(new PotionEffect[] {
				new PotionEffect(p, baseDuration / 2 * 20, strongAmplifier)});
		normal.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, rootName));
		extended.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, rootName + "_extended"));
		strong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, rootName + "_strong"));
		ForgeRegistries.POTION_TYPES.registerAll(normal, extended, strong);
	}
}
