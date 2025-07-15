package yeelp.scalingfeast.enchantments;

import java.util.Optional;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFEnchantments;

public class CurseLaziness extends SFEnchantmentBase {
	public CurseLaziness() {
		super("lazinesscurse", Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {
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
			public void onGetMaxExhaustion(ExhaustionEvent.GetMaxExhaustion evt) {
				int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.lazinessCurse, evt.player);
				if(level != 0) {
					evt.maxExhaustionLevel /= 2.0f;
				}
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableLaziness;
	}

	@Override
	public boolean shouldRegisterHandlerAnyway() {
		return false;
	}
}
