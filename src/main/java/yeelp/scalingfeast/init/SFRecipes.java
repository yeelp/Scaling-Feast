package yeelp.scalingfeast.init;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import yeelp.scalingfeast.config.ModConfig;

public final class SFRecipes {
	
	public static void init() {
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(SFItems.exhaustingOre, 1, 0), new ItemStack(SFItems.exhaustingIngot, 1), 0.8f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(SFItems.exhaustingOre, 1, 1), new ItemStack(SFItems.exhaustingIngot, 1), 0.8f);
		if(ModConfig.items.enablePotions && ModConfig.items.enableBrewingRecipes) {
			addBrewingRecipes(PotionTypes.THICK, SFPotion.metabolism, SFItems.heartyshank);
			addBrewingRecipes(PotionTypes.THICK, SFPotion.hungerminus, SFItems.exhaustingNugget);
			addBrewingInversions(SFPotion.hungerminus, SFPotion.hungerplus);
			addBrewingInversions(SFPotion.ironstomach, SFPotion.softstomach);
		}
	}

	private static void addBrewingRecipes(PotionType base, Potion result, Item baseIng) {
		Map<String, PotionType> types = SFPotion.POTION_TYPES.get(result);
		SFPotion.POTION_TYPES.get(result).forEach((s, p) -> {
			Item ing = baseIng;
			PotionType pot = types.get("base");
			switch(s) {
				case "extended":
					ing = Items.REDSTONE;
					break;
				case "strong":
					ing = Items.GLOWSTONE_DUST;
					break;
				default:
					pot = base;
					break;
			}
			PotionHelper.addMix(pot, ing, p);
		});
	}
	
	private static void addBrewingInversions(Potion input, Potion output) {
		Map<String, PotionType> inputs = SFPotion.POTION_TYPES.get(input);
		Map<String, PotionType> outputs = SFPotion.POTION_TYPES.get(output);
		inputs.forEach((s, p) -> PotionHelper.addMix(p, Items.FERMENTED_SPIDER_EYE, outputs.get(s)));
		outputs.forEach((s, p) -> PotionHelper.addMix(p, Items.FERMENTED_SPIDER_EYE, inputs.get(s)));
	}
}
