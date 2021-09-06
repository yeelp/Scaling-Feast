package yeelp.scalingfeast.enchantments;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFEnchantments;

/**
 * The Fasting Enchantment. This enchantment reduces exhaustion by 10% per
 * level.
 * 
 * @author Yeelp
 *
 */
public class EnchantmentFasting extends SFEnchantmentBase {

	static final UUID FASTING_MOD_UUID = UUID.fromString("33c7ebac-21ef-476f-8f95-6ab9a1cb3a14");

	/**
	 * Create a new Fasting Enchantment
	 */
	public EnchantmentFasting() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {
				EntityEquipmentSlot.CHEST});
		this.setRegistryName("fasting");
		this.setName(ModConsts.MOD_ID + ".fasting");
	}

	@Override
	public int getMinEnchantability(int level) {
		return 3 * level + 14;
	}

	@Override
	public int getMaxEnchantability(int level) {
		return -1 * level + 40;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != SFEnchantments.gluttony;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected Optional<Handler> getEnchantmentHandler() {
		return Optional.of(new Handler() {
			@SubscribeEvent
			public void onPlayerUpdate(LivingUpdateEvent evt) {
				if(evt.getEntityLiving() instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
					SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(player);
					double newFastingAmount = 0.1 * EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.fasting, player);
					double oldFastingAmount = sfstats.getFoodEfficiencyModifier(FASTING_MOD_UUID).map(AttributeModifier::getAmount).orElse(0.0d);
					if(newFastingAmount != oldFastingAmount) {
						AttributeModifier fastingMod = new AttributeModifier(FASTING_MOD_UUID, "Fasting enchantment", newFastingAmount, 0);
						if(fastingMod.getAmount() > 0) {
							sfstats.applyFoodEfficiencyModifier(fastingMod);
						}
						else {
							sfstats.getFoodEfficiencyModifierAttribute().removeModifier(fastingMod);
						}
					}
				}
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableFasting;
	}

	@Override
	public boolean shouldRegisterHandlerAnyway() {
		return Loader.isModLoaded(ModConsts.CONARM_ID);
	}
}
