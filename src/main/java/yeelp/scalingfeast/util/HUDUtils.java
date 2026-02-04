package yeelp.scalingfeast.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdible;
import squeek.applecore.api.food.IEdibleBlock;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.blocks.HeartyFeastBlock;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.items.ExhaustingApple;
import yeelp.scalingfeast.items.HeartyShankItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class HUDUtils {
	public static final class ColouredString {
		private final String text;
		private final Integer colour;

		ColouredString(@Nonnull String text, @Nullable Integer colour) {
			this.text = text;
			this.colour = colour;
		}

		@Nonnull
		public String getText() {
			return this.text;
		}

		@Nonnull
		public OptionalInt getColour() {
			return colour == null ? OptionalInt.empty() : OptionalInt.of(this.colour);
		}
	}

	public static class AdvancedInfo implements Iterable<Iterable<ColouredString>> {
		final List<ColouredString> hStrings, sStrings;

		AdvancedInfo(Collection<ColouredString> hStrings, Collection<ColouredString> sStrings) {
			this.hStrings = Lists.newLinkedList(hStrings);
			this.sStrings = Lists.newLinkedList(sStrings);
		}

		@Override
		@Nonnull
		public Iterator<Iterable<ColouredString>> iterator() {
			return new AdvancedIterator();
		}

		private class AdvancedIterator implements Iterator<Iterable<ColouredString>> {
			private byte index;

			AdvancedIterator() {
				this.index = 0;
			}

			@Override
			public boolean hasNext() {
				return this.index < 2;
			}

			@Override
			public Iterable<ColouredString> next() {
				switch(this.index++) {
					case 0:
						return AdvancedInfo.this.hStrings;
					case 1:
						return AdvancedInfo.this.sStrings;
					default:
						throw new NoSuchElementException();
				}
			}
		}
	}

	private static final List<Class<?>> EDIBLE_CLASSES = Lists.newArrayList(ItemFood.class, IEdible.class);

    public static AdvancedInfo getAdvancedInfoString(EntityPlayer player) {
		int hunger = player.getFoodStats().getFoodLevel();
		float sat = player.getFoodStats().getSaturationLevel();
		int max = AppleCoreAPI.accessor.getMaxHunger(player);
		float maxSat = ScalingFeastAPI.accessor.getPlayerSaturationCap(player);
		String foodAddition = "", maxAddition = "", satAddition = "", maxSatAddition = "";
		Integer hColour = null, sColour = null;
		FoodValues foodVals = getFoodValuesForBlockBeingLookedAt(player, hunger, max);
		boolean isHeartyShank = false, isExhaustingApple = false;
		if(foodVals == null) {
			EnumHand hand = getHandWithFood(player);
			if(hand != null) {
				ItemStack food = player.getHeldItem(hand);
				if(AppleCoreAPI.accessor.canPlayerEatFood(food, player)) {
					foodVals = AppleCoreAPI.accessor.getFoodValuesForPlayer(food, player);
					Item item = food.getItem();
					if(item instanceof HeartyShankItem && HeartyShankItem.canConsumeForMaxHunger(player)) {
						isHeartyShank = true;
					}
					else if(item instanceof ExhaustingApple) {
						isExhaustingApple = true;
					}
				}
			}
		}
		if(foodVals != null) {
			// The hunger to be gained from eating the food. Either the full amount or the
			// amount of hunger the player is missing.
			// for negative foods, the most they can reduce is the player's current hunger.
			int deltaHunger = Math.min(Math.max(-hunger, foodVals.hunger), max - hunger);
			if(deltaHunger < foodVals.hunger) {
				hColour = 0xff0000;
			}
			if(deltaHunger != 0) {
				foodAddition = String.format("%+d", deltaHunger);
			}
			float satCap = Math.min(hunger + deltaHunger, maxSat);
			float deltaSat = Math.min(Math.max(-sat, foodVals.getUnboundedSaturationIncrement()), satCap - sat);
			if(deltaSat < foodVals.getUnboundedSaturationIncrement()) {
				sColour = 0xff0000;
			}
			if(deltaSat != 0) {
				satAddition = String.format("%+.1f", deltaSat);
			}
			int deltaMaxH;
			float deltaMaxS;
			if(isHeartyShank) {
				deltaMaxH = ModConfig.items.shank.inc;
				short hardHungerCap = ScalingFeastAPI.accessor.getHungerHardCap();
				if(max + deltaMaxH > hardHungerCap) {
					deltaMaxH = hardHungerCap - max;
				}
				maxAddition = String.format("+%d", deltaMaxH);
				float hardSatCap = ScalingFeastAPI.accessor.getSaturationHardCap();
				float scaledSat = ScalingFeastAPI.accessor.getSaturationScaling().clampSaturation(max + ModConfig.items.shank.inc);
				deltaMaxS = Math.min(scaledSat, hardSatCap) - maxSat;
				maxSatAddition = String.format("+%.1f", deltaMaxS);
				if(maxSatAddition.equals("+0.0")) {
					maxSatAddition = "";
				}
			}
			else if(isExhaustingApple) {
				deltaMaxH = -ModConfig.items.shank.inc;
				if(max + deltaMaxH < 1) {
					deltaMaxH = 1 - max;
				}
				maxAddition = String.format("%d", deltaMaxH);
				float hardSatCap = ScalingFeastAPI.accessor.getSaturationHardCap();
				float scaledSat = ScalingFeastAPI.accessor.getSaturationScaling().clampSaturation(max - ModConfig.items.shank.inc);
				deltaMaxS = Math.min(scaledSat, hardSatCap) - maxSat;
				maxSatAddition = String.format("%.1f", deltaMaxS);
				if(maxSatAddition.equals("0.0")) {
					maxSatAddition = "";
				}
				foodAddition = String.format("%+d", (max + deltaMaxH) - hunger);
				satAddition = String.format("%+.1f", (maxSat + deltaMaxS) - sat);
			}
		}
		Queue<ColouredString> hQ = Lists.newLinkedList();
		Queue<ColouredString> sQ = Lists.newLinkedList();
		hQ.add(new ColouredString(String.format("%d", hunger), null));
		hQ.add(new ColouredString(foodAddition, hColour));
		hQ.add(new ColouredString("/", null));
		hQ.add(new ColouredString(String.format("%d", max), null));
		hQ.add(new ColouredString(maxAddition, null));
		sQ.add(new ColouredString(String.format("%.1f", sat), null));
		sQ.add(new ColouredString(satAddition, sColour));
		sQ.add(new ColouredString("/", null));
		sQ.add(new ColouredString(String.format("%.1f", maxSat), null));
		sQ.add(new ColouredString(maxSatAddition, null));
		return new AdvancedInfo(hQ, sQ);
	}

	private static EnumHand getHandWithFood(EntityPlayer player) {
		return Arrays.stream(EnumHand.values()).filter((hand) -> {
			Item item = player.getHeldItem(hand).getItem();
			return EDIBLE_CLASSES.stream().anyMatch((clazz) -> clazz.isInstance(item));
		}).findFirst().orElse(null);
	}

	private static FoodValues getFoodValuesForBlockBeingLookedAt(EntityPlayer player, int hunger, int max) {
		RayTraceResult lookedAt = Minecraft.getMinecraft().objectMouseOver;
		if(lookedAt == null) {
			return null;
		}
		if(!lookedAt.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
			return null;
		}
		BlockPos pos = lookedAt.getBlockPos();
		Block block = player.getEntityWorld().getBlockState(pos).getBlock();
		if(block instanceof IEdibleBlock) {
			if(hunger < max) {
				if(block instanceof HeartyFeastBlock) {
					((HeartyFeastBlock) block).setFoodValuesForPlayer(player);
				}
				return AppleCoreAPI.accessor.getFoodValuesForPlayer(new ItemStack(AppleCoreAPI.registry.getItemFromEdibleBlock(block)), player);
			}
		}
		return null;
	}
}
