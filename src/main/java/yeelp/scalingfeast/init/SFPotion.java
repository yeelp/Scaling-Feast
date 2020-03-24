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
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.potion.PotionIronStomach;
import yeelp.scalingfeast.potion.PotionMetabolism;

public class SFPotion 
{
	public static Potion metabolism;
	public static Potion ironstomach;
	public static PotionType metabolic;
	public static PotionType metabolicStrong;
	public static PotionType metabolicLong;
	
	@SubscribeEvent
	public void registerPotion(RegistryEvent.Register<Potion> evt)
	{
		metabolism = new PotionMetabolism();
		ironstomach = new PotionIronStomach();
		evt.getRegistry().register(metabolism);
		evt.getRegistry().register(ironstomach);
	}
	
	@SubscribeEvent
	public void registerPotionTypes(RegistryEvent.Register<PotionType> evt)
	{
		metabolic = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 60*20)});
		metabolicStrong = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 30*20, 1)});
		metabolicLong = new PotionType(new PotionEffect[] {new PotionEffect(metabolism, 120*20)});
		metabolic.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism"));
		metabolicStrong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_strong"));
		metabolicLong.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, "metabolism_extended"));
		
		evt.getRegistry().register(metabolic);
		evt.getRegistry().register(metabolicStrong);
		evt.getRegistry().register(metabolicLong);
	}
	
	public static void addBrewingRecipes()
	{
		addBrewingRecipe(PotionTypes.THICK, new ItemStack(SFFood.heartyshank), metabolic);
		addBrewingRecipe(metabolic, new ItemStack(Items.REDSTONE), metabolicLong);
		addBrewingRecipe(metabolic, new ItemStack(Items.GLOWSTONE_DUST), metabolicStrong);
		addBrewingRecipe(metabolicLong, new ItemStack(Items.GLOWSTONE_DUST), metabolicStrong);
		addBrewingRecipe(metabolicStrong, new ItemStack(Items.REDSTONE), metabolicLong);
		
		addBrewingTransformations(metabolic);
	}
	
	private static void addBrewingRecipe(PotionType input, ItemStack ingredient, PotionType output)
	{
		addBrewingRecipe(new ItemStack(Items.POTIONITEM), input, ingredient, new ItemStack(Items.POTIONITEM), output);
		addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), input, ingredient, new ItemStack(Items.SPLASH_POTION), output);
		addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), input, ingredient, new ItemStack(Items.LINGERING_POTION), output);
	}
	
	private static void addBrewingTransformations(PotionType potion)
	{
		addBrewingRecipe(new ItemStack(Items.POTIONITEM), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
		addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
		
		addBrewingRecipe(new ItemStack(Items.POTIONITEM), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
		addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
	}

	
	private static void addBrewingRecipe(ItemStack inputBottle, PotionType input, ItemStack ingredient, ItemStack outputBottle, PotionType output)
	{
		BrewingRecipeRegistry.addRecipe(new BrewingRecipe(PotionUtils.addPotionToItemStack(inputBottle, input), ingredient, PotionUtils.addPotionToItemStack(outputBottle, output)));
	}
}
