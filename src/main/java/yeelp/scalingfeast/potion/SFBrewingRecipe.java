package yeelp.scalingfeast.potion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;


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
	private final HashMap<Item, HashMap<InputPotion, ItemStack>> recipeMap = new HashMap<Item, HashMap<InputPotion, ItemStack>>();
	private final Item ingredient;
	private final PotionType inputBasePotion;
	private final PotionType outputNormalPotion;
	private final PotionType outputExtendedPotion;
	private final PotionType outputStrongPotion;
	private final boolean isInstant;
	private final String rootName;
	
	private ItemStack baseNormal = null;
	private ItemStack baseSplash = null;
	private ItemStack baseLing = null;
	
	private ItemStack regNormal = null;
	private ItemStack splashNormal = null;
	private ItemStack lingNormal = null;
	
	private ItemStack regExt = null;
	private ItemStack splashExt = null;
	private ItemStack lingExt = null;
	
	private ItemStack regStrong = null;
	private ItemStack splashStrong = null;
	private ItemStack lingStrong = null;
	
	/**
	 * Classify input potions.
	 * @author Yeelp
	 *
	 */
	private static enum InputPotion
	{
		INVALID,
		REGULAR_BASE,
		REGULAR_NORMAL,
		REGULAR_EXTENDED,
		REGULAR_STRONG,
		SPLASH_BASE,
		SPLASH_NORMAL,
		SPLASH_EXTENDED,
		SPLASH_STRONG,
		LINGER_BASE,
		LINGER_NORMAL,
		LINGER_EXTENDED,
		LINGER_STRONG;
		protected static final boolean isType(@Nonnull InputPotion potion, @Nonnull ItemStack input, @Nonnull SFBrewingRecipe recipe)
		{
			ItemStack potionToCheck = getPotion(potion, recipe);
			return potionToCheck != null && recipe.verifyType(potionToCheck.copy(), input) && ItemStack.areItemStackTagsEqual(potionToCheck.copy(), input) && ItemStack.areItemStacksEqual(potionToCheck.copy(), input);
		}
		@Nullable
		private static final ItemStack getPotion(@Nonnull InputPotion potion, @Nonnull SFBrewingRecipe recipe)
		{
			switch(potion)
			{
				case INVALID:
					return ItemStack.EMPTY;
				case REGULAR_BASE:
					return recipe.baseNormal;
				case REGULAR_NORMAL:
					return recipe.regNormal;
				case REGULAR_EXTENDED:
					return recipe.regExt;
				case REGULAR_STRONG:
					return recipe.regStrong;
				case SPLASH_BASE:
					return recipe.baseSplash;
				case SPLASH_NORMAL:
					return recipe.splashNormal;
				case SPLASH_EXTENDED:
					return recipe.splashExt;
				case SPLASH_STRONG:
					return recipe.splashStrong;
				case LINGER_BASE:
					return recipe.baseLing;
				case LINGER_NORMAL:
					return recipe.lingNormal;
				case LINGER_EXTENDED:
					return recipe.lingExt;
				case LINGER_STRONG:
					return recipe.lingStrong;
				//needed for JVM
				default:
					return ItemStack.EMPTY;	
			}
		}
	}

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
	protected SFBrewingRecipe(@Nonnull PotionType input, @Nonnull Item ingredient, boolean isInstant, @Nonnull PotionEffect[] basicPotion, PotionEffect[] extendedPotion, PotionEffect[] strongPotion, @Nonnull String rootName)
	{
		this.rootName = rootName;
		this.ingredient = ingredient;
		this.inputBasePotion = input;
		this.isInstant = isInstant;
		this.outputNormalPotion = new PotionType(basicPotion);
		this.outputExtendedPotion = isInstant || extendedPotion == null ? null : new PotionType(extendedPotion);
		this.outputStrongPotion = strongPotion == null ? null : new PotionType(strongPotion);
	}
	private void setupRecipe()
	{
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
		
		this.regNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), outputNormalPotion);
		this.splashNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), outputNormalPotion);
		this.lingNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), outputNormalPotion);
		
		this.baseNormal = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), inputBasePotion);
		this.baseSplash = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), inputBasePotion);
		this.baseLing = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), inputBasePotion);
		
		HashMap<InputPotion, ItemStack> splashTransformations = new HashMap<InputPotion, ItemStack>();
		HashMap<InputPotion, ItemStack> lingTransformations = new HashMap<InputPotion, ItemStack>();
		HashMap<InputPotion, ItemStack> basic = new HashMap<InputPotion, ItemStack>();
		basic.put(InputPotion.REGULAR_BASE, this.regNormal);
		basic.put(InputPotion.LINGER_BASE, this.lingNormal);
		basic.put(InputPotion.SPLASH_BASE, this.splashNormal);
		basic.put(InputPotion.INVALID, ItemStack.EMPTY);
		
		splashTransformations.put(InputPotion.REGULAR_NORMAL, this.splashNormal);
		splashTransformations.put(InputPotion.LINGER_NORMAL, this.splashNormal);
		splashTransformations.put(InputPotion.INVALID, ItemStack.EMPTY);
		
		lingTransformations.put(InputPotion.REGULAR_NORMAL, this.lingNormal);
		lingTransformations.put(InputPotion.SPLASH_NORMAL, this.lingNormal);
		lingTransformations.put(InputPotion.INVALID, ItemStack.EMPTY);
		this.recipeMap.put(ingredient, basic);
		
		if(this.outputExtendedPotion != null)
		{
			this.regExt = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), outputExtendedPotion);
			this.splashExt = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), outputExtendedPotion);
			this.lingExt = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), outputExtendedPotion);
			
			HashMap<InputPotion, ItemStack> extended = new HashMap<InputPotion, ItemStack>();
			extended.put(InputPotion.REGULAR_NORMAL, this.regExt);
			extended.put(InputPotion.SPLASH_NORMAL, this.splashExt);
			extended.put(InputPotion.LINGER_NORMAL, this.lingExt);
			extended.put(InputPotion.INVALID, ItemStack.EMPTY);
			
			splashTransformations.put(InputPotion.REGULAR_EXTENDED, this.splashExt);
			splashTransformations.put(InputPotion.LINGER_EXTENDED, this.splashExt);
			
			lingTransformations.put(InputPotion.REGULAR_EXTENDED, this.lingExt);
			lingTransformations.put(InputPotion.SPLASH_EXTENDED, this.lingExt);
			this.recipeMap.put(Items.REDSTONE, extended);
		}
		
		if(this.outputStrongPotion != null)
		{
			this.regStrong = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), outputStrongPotion);
			this.splashStrong = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), outputStrongPotion);
			this.lingStrong = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), outputStrongPotion);
			
			HashMap<InputPotion, ItemStack> strong = new HashMap<InputPotion, ItemStack>();
			
			strong.put(InputPotion.REGULAR_NORMAL, this.regStrong);
			strong.put(InputPotion.SPLASH_NORMAL, this.splashStrong);
			strong.put(InputPotion.LINGER_NORMAL, this.lingStrong);
			strong.put(InputPotion.INVALID, ItemStack.EMPTY);
			
			splashTransformations.put(InputPotion.REGULAR_STRONG, this.splashStrong);
			splashTransformations.put(InputPotion.LINGER_STRONG, this.splashStrong);
			
			lingTransformations.put(InputPotion.REGULAR_STRONG, this.lingStrong);
			lingTransformations.put(InputPotion.SPLASH_STRONG, this.lingStrong);
			this.recipeMap.put(Items.GLOWSTONE_DUST, strong);
		}
		this.recipeMap.put(Items.GUNPOWDER, splashTransformations);
		this.recipeMap.put(Items.DRAGON_BREATH, lingTransformations);
		//For ease of readability later, we'll iterate through the rest of the possible combinations of Item, InputPotion and map them to ItemStack.EMPTY
		for(HashMap<InputPotion, ItemStack> map : this.recipeMap.values())
		{
			for(InputPotion i : InputPotion.values())
			{
				if(!map.containsKey(i))
				{
					map.put(i, ItemStack.EMPTY);
				}
			}
		}
	}
	@Override
	public boolean isInput(ItemStack input) 
	{
		for(ItemStack stack : this.validInputs)
		{
			if(verifyType(stack, input) && ItemStack.areItemStackTagsEqual(stack, input) && ItemStack.areItemStacksEqual(stack, input))
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
			return recipeMap.get(ingredient.getItem()).get(classifyInput(input)).copy();
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}
	
	@Nonnull
	private final InputPotion classifyInput(@Nonnull ItemStack input)
	{
		for(InputPotion i : InputPotion.values())
		{
			if(i.isType(i, input, this))
			{
				return i;
			}
		}
		return InputPotion.INVALID;
	}
	
	private final boolean verifyType(@Nonnull ItemStack checkAgainst, @Nonnull ItemStack input)
	{
		NBTTagCompound tagA = checkAgainst.getTagCompound();
		NBTTagCompound tagB = input.getTagCompound();
		if(tagA == null && tagB == null)
		{
			return true;
		}
		else if(tagA != null && tagB != null)
		{
			return tagA.getString("Potion").equals(tagB.getString("Potion"));
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Register the brewing recipes
	 */
	public final void registerBrewingRecipes()
	{
		this.setupRecipe();
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
	public void createJEIRecipes() 
	{
		
	}
}
