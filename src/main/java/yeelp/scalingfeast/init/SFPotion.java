package yeelp.scalingfeast.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
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
import yeelp.scalingfeast.potion.PotionSoftStomach;
import yeelp.scalingfeast.potion.PotionBase;
import yeelp.scalingfeast.potion.PotionBloated;
import yeelp.scalingfeast.potion.PotionHungerMinus;
import yeelp.scalingfeast.potion.PotionHungerPlus;

public class SFPotion 
{
	public static Potion metabolism;
	public static Potion ironstomach;
	public static Potion bloated;
	public static Potion hungerplus;
	public static Potion hungerminus;
	public static Potion softstomach;
	public static PotionType metabolic;
	public static PotionType metabolicStrong;
	public static PotionType metabolicLong;
	
	public static void init() 
	{
		metabolism = new PotionMetabolism();
		ironstomach = new PotionIronStomach();
		bloated = new PotionBloated();
		hungerplus = new PotionHungerPlus();
		hungerminus = new PotionHungerMinus();
		softstomach = new PotionSoftStomach();
		
		ForgeRegistries.POTIONS.registerAll(metabolism, ironstomach, bloated, hungerplus, hungerminus, softstomach);
		
		if(ModConfig.items.enableMetabolicPotion)
		{
			PotionType metabolic = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 120*20)});
			PotionType metabolicLong = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 240*20)});
			PotionType metabolicStrong = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 60*20, 1)});
			metabolic.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism"));
			metabolicLong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_extended"));
			metabolicStrong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_strong"));
			ForgeRegistries.POTION_TYPES.registerAll(metabolic, metabolicLong, metabolicStrong);
			if(ModConfig.items.enableMetabolicRecipes)
			{
				PotionHelper.addMix(PotionTypes.THICK, SFFood.heartyshank, metabolic);
				PotionHelper.addMix(metabolic, Items.REDSTONE, metabolicLong);
				PotionHelper.addMix(metabolic, Items.GLOWSTONE_DUST, metabolicStrong);
			}
		}
	}
}
