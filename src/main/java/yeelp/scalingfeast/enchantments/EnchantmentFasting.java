package yeelp.scalingfeast.enchantments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFAttributes;
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
	private static final String FASTING_MOD_KEY = "Fasting Modifier";
	private static final int FASTING_MOD_OP = 2;

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
					InventoryPlayer inv = player.inventory;
					Collection<ItemStack> items = new ArrayList<ItemStack>(inv.mainInventory);
					items.addAll(inv.armorInventory);
					items.addAll(inv.offHandInventory);
					for(ItemStack itemStack : items) {
						if(itemStack.isEmpty()) {
							continue;
						}
						if(this.shouldApplyFastingMod(itemStack)) {
							this.addFastingModifier(itemStack);
						}
						else if(this.shouldUpdateFastingMod(itemStack)) {
							this.updateFastingModifier(itemStack);
						}
						else if(this.shouldRemoveFastingMod(itemStack)) {
							this.removeFastingModifier(itemStack);
						}
					}
				}
			}

			private boolean shouldApplyFastingMod(ItemStack stack) {
				return this.hasFasting(stack) && !this.hasFastingModifier(stack);
			}

			private boolean shouldRemoveFastingMod(ItemStack stack) {
				return !this.hasFasting(stack) && this.hasFastingModifier(stack);
			}

			private boolean shouldUpdateFastingMod(ItemStack stack) {
				return stack.getAttributeModifiers(EntityEquipmentSlot.CHEST).get(SFAttributes.FOOD_EFFICIENCY.getName()).stream().filter((m) -> m.getID().equals(FASTING_MOD_UUID)).findFirst().map((m) -> m.getAmount() != EnchantmentHelper.getEnchantmentLevel(SFEnchantments.fasting, stack) * 0.1).orElse(false);
			}

			private boolean hasFastingModifier(ItemStack stack) {
				return stack.getAttributeModifiers(EntityEquipmentSlot.CHEST).get(SFAttributes.FOOD_EFFICIENCY.getName()).stream().anyMatch((m) -> m.getID().equals(FASTING_MOD_UUID));
			}

			private boolean hasFasting(ItemStack stack) {
				return EnchantmentHelper.getEnchantmentLevel(SFEnchantments.fasting, stack) > 0;
			}

			private void addFastingModifier(ItemStack stack) {
				stack.addAttributeModifier(EnchantmentFasting.FASTING_MOD_KEY, new AttributeModifier(FASTING_MOD_UUID, FASTING_MOD_KEY, 0.1 * EnchantmentHelper.getEnchantmentLevel(SFEnchantments.fasting, stack), FASTING_MOD_OP), EntityEquipmentSlot.CHEST);
			}

			private void removeFastingModifier(ItemStack stack) {
				NBTTagList lst = getModifiers(stack);
				int index = getIndexOfFastingModifier(lst);
				if(index != -1) {
					lst.removeTag(index);
				}
			}

			private void updateFastingModifier(ItemStack stack) {
				NBTTagList lst = getModifiers(stack);
				int index = getIndexOfFastingModifier(lst);
				lst.getCompoundTagAt(index).setDouble("Amount", 0.1 * EnchantmentHelper.getEnchantmentLevel(SFEnchantments.fasting, stack));
			}

			private NBTTagList getModifiers(ItemStack stack) {
				return stack.getTagCompound().getTagList("AttributeModifiers", 10);
			}

			private int getIndexOfFastingModifier(NBTTagList lst) {
				int index = 0;
				for(; index < lst.tagCount() && !lst.getCompoundTagAt(index).getUniqueId("UUID").equals(EnchantmentFasting.FASTING_MOD_UUID); index++);
				return index < lst.tagCount() ? index : -1;
			}
		});
	}

	@Override
	public boolean enabled() {
		return ModConfig.items.enchants.enableFasting;
	}
}
