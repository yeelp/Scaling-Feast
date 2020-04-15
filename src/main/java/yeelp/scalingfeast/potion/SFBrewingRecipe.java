package yeelp.scalingfeast.potion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ModConsts;


/**
 * Class to deal with all the brewing recipe nonsense
 * @author Yeelp
 *
 */
public final class SFBrewingRecipe implements IBrewingRecipe 
{
	
	private final HashSet<ItemStack> validInputs = new HashSet<ItemStack>();
	private final HashSet<Item> validIngredients = new HashSet<Item>();
	private final HashSet<PotionType> types = new HashSet<PotionType>();
	private final HashMap<Item, HashMap<ItemStack, ItemStack>> recipeMap = new HashMap<Item, HashMap<ItemStack, ItemStack>>();
	private final Item ingredient;
	private final PotionType inputBasePotion;
	private final PotionType outputNormalPotion;
	private final PotionType outputExtendedPotion;
	private final PotionType outputStrongPotion;
	private final boolean isInstant;
	private final String rootName;

	/**
	 * Build a new Brewing Recipe
	 * @param input the input PotionType, what all of these potions must be derived from.
	 * @param ingredient The Item that brews these potions from the base potion
	 * @param isInstant True if the potion should be instant, false otherwise.
	 * @param basicPotion The effects on the basic potion.
	 * @param extendedPotion The effects on the extended potion. If isInstant is true, this argument is ignored.
	 * @param strongPotion The effects on the strong potion
	 * @param rootName The root localization key of the potion. Extended variants get "_extended" appended and strong variants get "_strong" appended.
	 */
	protected SFBrewingRecipe(@Nonnull PotionType input, @Nonnull Item ingredient, boolean isInstant, @Nonnull PotionEffect[] basicPotion, PotionEffect[] extendedPotion, PotionEffect[] strongPotion, String rootName)
	{
		this.rootName = rootName;
		this.ingredient = ingredient;
		this.inputBasePotion = input;
		this.isInstant = isInstant;
		this.outputNormalPotion = new PotionType(basicPotion);
		this.outputExtendedPotion = isInstant || extendedPotion == null ? null : new PotionType(extendedPotion);
		this.outputStrongPotion = strongPotion == null ? null : new PotionType(strongPotion);
		
		//Get the set of valid ingredients.
		validIngredients.addAll(Arrays.asList(ingredient, Items.GUNPOWDER, Items.DRAGON_BREATH));
		if(this.outputExtendedPotion != null)
		{
			validIngredients.add(Items.REDSTONE);
		}
		if(this.outputStrongPotion != null)
		{
			validIngredients.add(Items.GLOWSTONE_DUST);
		}
		
		types.add(inputBasePotion);
		types.add(outputNormalPotion);
		if(outputExtendedPotion != null)
		{
			types.add(outputExtendedPotion);
		}
		if(outputStrongPotion != null)
		{
			types.add(outputStrongPotion);
		}
		
		//now build the set of valid inputs.
		for(PotionType p : types)
		{
			validInputs.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), p));
			validInputs.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), p));
			validInputs.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), p));
		}
		
		//now we build the recipe map.
		ItemStack baseNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), inputBasePotion);
		ItemStack baseSplash = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), inputBasePotion);
		ItemStack baseLing = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), inputBasePotion);
		
		ItemStack regNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), outputNormalPotion);
		ItemStack splashNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), outputNormalPotion);
		ItemStack lingNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), outputNormalPotion);
		
		HashMap<ItemStack, ItemStack> splashTransformations = new HashMap<ItemStack, ItemStack>();
		HashMap<ItemStack, ItemStack> lingTransformations = new HashMap<ItemStack, ItemStack>();
		HashMap<ItemStack, ItemStack> basic = new HashMap<ItemStack, ItemStack>();
		basic.put(baseNormal, regNormal);
		basic.put(baseLing, lingNormal);
		basic.put(baseSplash, splashNormal);
		
		splashTransformations.put(regNormal, splashNormal);
		splashTransformations.put(lingNormal, splashNormal);
		
		lingTransformations.put(regNormal, lingNormal);
		lingTransformations.put(splashNormal, lingNormal);
		this.recipeMap.put(ingredient, basic);
		
		if(this.outputExtendedPotion != null)
		{
			ItemStack regExt = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), outputExtendedPotion);
			ItemStack splashExt = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), outputExtendedPotion);
			ItemStack lingExt = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), outputExtendedPotion);
			
			HashMap<ItemStack, ItemStack> extended = new HashMap<ItemStack, ItemStack>();
			extended.put(regNormal, regExt);
			extended.put(splashNormal, splashExt);
			extended.put(lingNormal, lingExt);
			
			splashTransformations.put(regExt, splashExt);
			splashTransformations.put(lingExt, splashExt);
			
			lingTransformations.put(regExt, lingExt);
			lingTransformations.put(splashExt, lingExt);
			this.recipeMap.put(Items.REDSTONE, extended);
		}
		
		if(this.outputStrongPotion != null)
		{
			ItemStack regStrong = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), outputStrongPotion);
			ItemStack splashStrong = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), outputStrongPotion);
			ItemStack lingStrong = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), outputStrongPotion);
		
			HashMap<ItemStack, ItemStack> strong = new HashMap<ItemStack, ItemStack>();
			
			strong.put(regNormal, regStrong);
			strong.put(splashNormal, splashStrong);
			strong.put(lingNormal, lingStrong);
			
			splashTransformations.put(regStrong, splashStrong);
			splashTransformations.put(lingStrong, splashStrong);
			
			lingTransformations.put(regStrong, lingStrong);
			lingTransformations.put(splashStrong, lingStrong);
			this.recipeMap.put(Items.GLOWSTONE_DUST, strong);
		}
		this.recipeMap.put(Items.GUNPOWDER, splashTransformations);
		this.recipeMap.put(Items.DRAGON_BREATH, lingTransformations);
		
	}
	@Override
	public boolean isInput(ItemStack input) 
	{
		for(ItemStack stack : this.validInputs)
		{
			if(ItemStack.areItemStacksEqual(stack, input))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) 
	{
		for(Item i : this.validIngredients)
		{
			if(ItemStack.areItemsEqual(new ItemStack(i), ingredient))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) 
	{
		if(isIngredient(ingredient) && isInput(input))
		{
			if (recipeMap.get(ingredient.getItem()).get(input) != null)
			{
				return recipeMap.get(ingredient.getItem()).get(input);
			}
			else
			{
				return ItemStack.EMPTY;
			}
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}
	
	/**
	 * Register the brewing recipes
	 */
	public void registerBrewingRecipes()
	{
		BrewingRecipeRegistry.addRecipe(this);
	}
	
	/**
	 * Register the potion types
	 */
	public void registerPotionTypes()
	{
		this.outputNormalPotion.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, this.rootName));
		ForgeRegistries.POTION_TYPES.register(this.outputNormalPotion);
		if(this.outputExtendedPotion != null)
		{
			this.outputExtendedPotion.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, this.rootName + "_extended"));
			ForgeRegistries.POTION_TYPES.register(this.outputExtendedPotion);
		}
		if(this.outputStrongPotion != null)
		{
			this.outputStrongPotion.setRegistryName(new ResourceLocation(ModConsts.MOD_ID, this.rootName + "_strong"));
			ForgeRegistries.POTION_TYPES.register(this.outputStrongPotion);
		}
	}
}
