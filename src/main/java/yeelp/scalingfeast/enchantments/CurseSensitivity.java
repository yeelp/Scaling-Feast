package yeelp.scalingfeast.enchantments;

import java.util.Optional;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFEnchantments;
import yeelp.scalingfeast.init.SFPotion;

public class CurseSensitivity extends SFEnchantmentBase {
	public CurseSensitivity() {
		super("sensitivitycurse", Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {
				EntityEquipmentSlot.CHEST});
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	public int getMinEnchantability(int level) {
		return 10;
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 50;
	}

	@Override
	protected Optional<Handler> getEnchantmentHandler() {
		return Optional.of(new Handler() {
			@SubscribeEvent
			public void onFoodStatsAddition(FoodEvent.FoodStatsAddition evt) {
				EntityPlayer player = evt.player;
				int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.sensitivityCurse, player);
				if(level != 0 || ModConfig.items.enchants.globalSensitvity) {
					int overflow = (evt.foodValuesToBeAdded.hunger + player.getFoodStats().getFoodLevel()) - AppleCoreAPI.accessor.getMaxHunger(player);
					if(overflow > 0) {
						if(overflow > 256) {
							overflow = 256; // fail safe in case potion amplifiers > 255 behave oddly.
						}
						player.addPotionEffect(new PotionEffect(SFPotion.softstomach, 20 * 20, overflow - 1));
					}
				}
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableSensitivity && !ModConfig.items.enchants.globalSensitvity;
	}

	@Override
	public boolean shouldRegisterHandlerAnyway() {
		return ModConfig.items.enchants.globalSensitvity;
	}
	
	
}
