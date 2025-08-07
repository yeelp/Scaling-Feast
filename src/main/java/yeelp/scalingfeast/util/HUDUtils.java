package yeelp.scalingfeast.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
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

public class HUDUtils {
	public static class AdvancedInfo implements Iterable<Iterable<Tuple<String, Integer>>> {
		final List<Tuple<String, Integer>> hStrings, sStrings;

		AdvancedInfo(Collection<Tuple<String, Integer>> hStrings, Collection<Tuple<String, Integer>> sStrings) {
			this.hStrings = new LinkedList<Tuple<String, Integer>>(hStrings);
			this.sStrings = new LinkedList<Tuple<String, Integer>>(sStrings);
		}

		@Override
		public Iterator<Iterable<Tuple<String, Integer>>> iterator() {
			return new AdvancedIterator();
		}

		private class AdvancedIterator implements Iterator<Iterable<Tuple<String, Integer>>> {
			private byte index;

			AdvancedIterator() {
				this.index = 0;
			}

			@Override
			public boolean hasNext() {
				return this.index < 2;
			}

			@Override
			public Iterable<Tuple<String, Integer>> next() {
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
			int deltaMaxH = 0;
			float deltaMaxS = 0.0f;
			if(isHeartyShank) {
				deltaMaxH = ModConfig.items.shank.inc;
				short hardHungerCap = ScalingFeastAPI.accessor.getHungerHardCap();
				if(max + deltaMaxH > hardHungerCap) {
					deltaMaxH = hardHungerCap - max;
				}
				maxAddition = String.format("+%d", deltaMaxH);
				float hardSatCap = ScalingFeastAPI.accessor.getSaturationHardCap();
				float scaledSat = ScalingFeastAPI.accessor.getSaturationScaling().clampSaturation(max + ModConfig.items.shank.inc);
				deltaMaxS = (scaledSat < hardSatCap ? scaledSat : hardSatCap) - maxSat;
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
				deltaMaxS = (scaledSat < hardSatCap ? scaledSat : hardSatCap) - maxSat;
				maxSatAddition = String.format("%.1f", deltaMaxS);
				if(maxSatAddition.equals("0.0")) {
					maxSatAddition = "";
				}
				foodAddition = String.format("%+d", (max + deltaMaxH) - hunger);
				satAddition = String.format("%+.1f", (maxSat + deltaMaxS) - sat);
			}
		}
		Queue<Tuple<String, Integer>> hQ = new LinkedList<Tuple<String, Integer>>();
		Queue<Tuple<String, Integer>> sQ = new LinkedList<Tuple<String, Integer>>();
		hQ.add(new Tuple<String, Integer>(String.format("%d", hunger), null));
		hQ.add(new Tuple<String, Integer>(foodAddition, hColour));
		hQ.add(new Tuple<String, Integer>("/", null));
		hQ.add(new Tuple<String, Integer>(String.format("%d", max), null));
		hQ.add(new Tuple<String, Integer>(maxAddition, null));
		sQ.add(new Tuple<String, Integer>(String.format("%.1f", sat), null));
		sQ.add(new Tuple<String, Integer>(satAddition, sColour));
		sQ.add(new Tuple<String, Integer>("/", null));
		sQ.add(new Tuple<String, Integer>(String.format("%.1f", maxSat), null));
		sQ.add(new Tuple<String, Integer>(maxSatAddition, null));
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
		BlockPos pos;
		if(lookedAt == null) {
			return null;
		}
		if(!lookedAt.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
			return null;
		}
		if((pos = lookedAt.getBlockPos()) == null) {
			return null;
		}
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
