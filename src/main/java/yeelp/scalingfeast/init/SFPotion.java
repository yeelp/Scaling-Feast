package yeelp.scalingfeast.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.potion.PotionIronStomach;
import yeelp.scalingfeast.potion.PotionMetabolism;
import yeelp.scalingfeast.potion.PotionBase;

public class SFPotion 
{
	public static Potion metabolism;
	public static Potion ironstomach;
	public static PotionType metabolic;
	public static PotionType metabolicStrong;
	public static PotionType metabolicLong;
	
	public static void init() 
	{
		metabolism = new PotionMetabolism();
		ironstomach = new PotionIronStomach();
		ForgeRegistries.POTIONS.registerAll(metabolism, ironstomach);
		
		if(ModConfig.items.enableMetabolicPotion)
		{
			((PotionBase) metabolism).setupRecipe(PotionTypes.THICK, SFFood.heartyshank, new PotionEffect[] {new PotionEffect(metabolism, 120*20)}, new PotionEffect[] {new PotionEffect(metabolism, 240*20)}, new PotionEffect[] {new PotionEffect(metabolism, 60*20, 1)}, "metabolism");
			((PotionBase) metabolism).registerPotionType();
		}
	}
	
	public static void addBrewingRecipes()
	{
		((PotionBase) metabolism).registerRecipe();
	}

	public static void createJEIRecipes() 
	{
		((PotionBase) metabolism).createJEIRecipes();
	}
}
