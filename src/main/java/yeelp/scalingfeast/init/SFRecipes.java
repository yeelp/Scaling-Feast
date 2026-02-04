package yeelp.scalingfeast.init;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.blocks.ExhaustingOreBlock;
import yeelp.scalingfeast.config.ModConfig;

public final class SFRecipes {
	
	private static final Map<Potion, Potion> INVERSIONS = Maps.newHashMap();
	private static final Set<Item> MUNDANE_BREWERS = Sets.newHashSet();
	private static final Set<Item> AWKWARD_BREWERS = Sets.newHashSet();
	private static final Map<PotionType, Set<Item>> BASE_BREWING = Maps.newHashMap();
	
	static {
		INVERSIONS.put(SFPotion.hungerminus, SFPotion.hungerplus);
		INVERSIONS.put(SFPotion.ironstomach, SFPotion.softstomach);
		MUNDANE_BREWERS.add(SFItems.heartyshank);
		MUNDANE_BREWERS.add(SFItems.exhaustingNugget);
		AWKWARD_BREWERS.add(SFItems.exhaustingpotato);
		BASE_BREWING.put(PotionTypes.MUNDANE, MUNDANE_BREWERS);
		BASE_BREWING.put(PotionTypes.AWKWARD, AWKWARD_BREWERS);
	}
	
	@SuppressWarnings("deprecation")
	public static void init() {
		Comparator<PotionType> sortByAmplifier = Comparator.comparingInt((p) -> p.getEffects().get(0).getAmplifier());
		final ItemStack output = new ItemStack(SFItems.exhaustingIngot, 1);
		for(int i = 0; i < ExhaustingOreBlock.RockType.values().length; FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(SFItems.exhaustingOre, 1, i++), output, 0.8f));
		if(ModConfig.items.enablePotions && ModConfig.items.enableBrewingRecipes) {
			addBrewingRecipes(PotionTypes.THICK, SFPotion.metabolism, SFItems.heartyshank);
			addBrewingRecipes(PotionTypes.THICK, SFPotion.hungerminus, SFItems.exhaustingNugget);
			addBrewingRecipes(PotionTypes.THICK, SFPotion.deficiency, SFItems.exhaustingpotato);
			INVERSIONS.forEach(SFRecipes::addBrewingInversions);
			//create brewing inversions for deficiency potion, but only if we can find a registered saturation potion type.
			Deque<PotionType> saturationPotions = ForgeRegistries.POTION_TYPES.getValues().stream().filter((p) -> {
				List<PotionEffect> effects = p.getEffects();
				if(effects.size() == 1) {
					return effects.get(0).getPotion().equals(MobEffects.SATURATION);
				}
				return false;
			}).sorted(sortByAmplifier).collect(Collectors.collectingAndThen(Collectors.toList(), Lists::newLinkedList));
			//collect to deque so we can sort by amplifier and have the weakest and strongest saturation potions to invert out two deficiency potions to.
			//we technically don't know if there will be more than 2 registered saturation potions.
			//this ensures the weakest and strongest potions get inverted.
			if(!saturationPotions.isEmpty()) {
				Iterator<PotionType> deficiencyPotions = SFPotion.POTION_TYPES.get(SFPotion.deficiency).values().stream().sorted(sortByAmplifier).iterator();
				//if there is only 1 saturation potion, our first and last calls will retrieve the same object and be limited down to 1 with the distinct call.
				Stream.of(saturationPotions.getFirst(), saturationPotions.getLast()).distinct().forEach((pt) -> {
					PotionType deficiency = deficiencyPotions.next();
					PotionHelper.addMix(deficiency, Items.FERMENTED_SPIDER_EYE, pt);
					PotionHelper.addMix(pt, Items.FERMENTED_SPIDER_EYE, deficiency);
				});
			}
			BASE_BREWING.forEach((pt, items) -> items.forEach((i) -> PotionHelper.addMix(PotionTypes.WATER, i, pt)));
		}
	}

	@SuppressWarnings("SameParameterValue")
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
