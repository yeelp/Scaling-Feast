package yeelp.scalingfeast.enchantments;

import java.util.Optional;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFEnchantments;

/**
 * The Eternal Feast enchantment.
 * 
 * @author Yeelp
 *
 */
public class EnchantmentEternalFeast extends SFEnchantmentBase {
	/**
	 * Create a new Eternal Feast enchantment. This enchantment restores hunger
	 * every time the user kills an entity.
	 */
	public EnchantmentEternalFeast() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {
				EntityEquipmentSlot.MAINHAND});
		this.setRegistryName("eternalfeast");
		this.setName(ModConsts.MOD_ID + ".eternalfeast");
	}

	@Override
	public int getMinEnchantability(int level) {
		return 3 * level + 13;
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 3 * level + 24;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected Optional<Handler> getEnchantmentHandler() {
		return Optional.of(new Handler() {
			@SubscribeEvent
			public void onKillEvent(LivingDeathEvent evt) {
				Entity entity = evt.getSource().getTrueSource();
				if(entity != null && entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.eternalfeast, player);
					if(level != 0) {
						ScalingFeastAPI.mutator.addFoodStatsWithOverflow(player, 2 * level);
					}
				}
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableEternalFeast;
	}

	@Override
	public boolean shouldRegisterHandlerAnyway() {
		return false;
	}
}
