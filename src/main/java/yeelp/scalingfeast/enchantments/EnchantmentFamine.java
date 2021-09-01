package yeelp.scalingfeast.enchantments;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFEnchantments;

/**
 * The Famine Enchantment. Entities struck with this enchantment suffer a hunger
 * and weakness effect, depending on the level.
 * 
 * @author Yeelp
 *
 */
public class EnchantmentFamine extends SFEnchantmentBase {
	/**
	 * Create a new Fasting Enchantment
	 */
	public EnchantmentFamine() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {
				EntityEquipmentSlot.MAINHAND});
		this.setRegistryName("famine");
		this.setName(ModConsts.MOD_ID + ".famine");
	}

	@Override
	public int getMinEnchantability(int level) {
		return 7 * level + 11;
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 3 * level + 40;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	protected Optional<Handler> getEnchantmentHandler() {
		return Optional.of(new Handler() {
			private final Set<UUID> fullSwing = new HashSet<UUID>();
			@SubscribeEvent
			public void onSwing(AttackEntityEvent evt) {
				if(evt.getEntityPlayer().getCooledAttackStrength(0) == 1.0f) {
					this.fullSwing.add(evt.getEntityPlayer().getUniqueID());
				}	
			}

			@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
			public void onAttackEvent(LivingAttackEvent evt) {
				Entity source = evt.getSource().getTrueSource();
				if(!(source instanceof EntityLivingBase)) {
					return;
				}
				EntityLivingBase defender = evt.getEntityLiving();
				boolean isPlayerAttacker = source instanceof EntityPlayer;
				int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.famine, (EntityLivingBase) source);
				if(level != 0) {
					if(!(defender instanceof EntityPlayer)) {
						int wlevel = (level < 3 ? 0 : 1);
						defender.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 15 * 20, wlevel));
					}
					else {
						boolean canPlayerUseFamine = false;
						if(isPlayerAttacker) {
							EntityPlayer attacker = (EntityPlayer) source;
							canPlayerUseFamine = this.fullSwing.remove(attacker.getUniqueID());
						}
						if(!isPlayerAttacker || canPlayerUseFamine) {
							// Player attacker => PlayerUsedFamine
							EntityPlayer player = (EntityPlayer) defender;
							ScalingFeastAPI.mutator.damageFoodStats(player, 1.25f * level);
						}
					}
				}
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableFamine;
	}
}
