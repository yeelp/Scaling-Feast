package yeelp.scalingfeast.enchantments;

import java.util.Optional;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFEnchantments;

/**
 * The Gluttony Enchantment. This enchantment increases food values by 50% per
 * level, rounded down.
 * 
 * @author Yeelp
 *
 */
public class EnchantmentGluttony extends SFEnchantmentBase {
	/**
	 * Create a new Gluttony Enchantment
	 */
	public EnchantmentGluttony() {
		super(Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {
				EntityEquipmentSlot.CHEST});
		this.setRegistryName("gluttony");
		this.setName(ModConsts.MOD_ID + ".gluttony");
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 6 * enchantmentLevel + 20;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return 10 * enchantmentLevel + 30;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != SFEnchantments.fasting;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected Optional<Handler> getEnchantmentHandler() {
		return Optional.of(new Handler() {
			@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
			public void getPlayerSpecificFoodValues(FoodEvent.GetPlayerFoodValues evt) {
				int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.gluttony, evt.player);
				if(level != 0 && evt.player != null) // Player can apparently be null, according to AppleCore.
				{
					float mod = (1 + 0.5f * level);
					FoodValues foodvals = evt.foodValues;
					int newHunger = (int) (foodvals.hunger * mod);
					evt.foodValues = new FoodValues(newHunger, foodvals.saturationModifier);
				}
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableGluttony;
	}
}
