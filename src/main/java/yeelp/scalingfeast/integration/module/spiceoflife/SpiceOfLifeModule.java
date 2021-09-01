package yeelp.scalingfeast.integration.module.spiceoflife;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.food.FoodEvent.FoodEaten;
import squeek.spiceoflife.foodtracker.FoodHistory;
import squeek.spiceoflife.foodtracker.foodgroups.FoodGroup;
import squeek.spiceoflife.foodtracker.foodgroups.FoodGroupRegistry;
import squeek.spiceoflife.foodtracker.foodqueue.FoodQueue;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.modules.SFSpiceOfLifeConfigCategory;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.integration.module.AbstractModule;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

public final class SpiceOfLifeModule extends AbstractModule<SFSpiceOfLifeConfigCategory> {

	private boolean usingFoodGroups;
	private int requiredAmount;
	private int penaltyAmount;

	private enum TooltipType {
		GOOD("modules.scalingfeast.spiceoflife.tooltip.restore", new Style().setColor(TextFormatting.GREEN)),
		BAD("modules.scalingfeast.spiceoflife.tooltip.punish", new Style().setColor(TextFormatting.RED)),
		NEUTRAL {
			@Override
			Optional<String> getTooltipText() {
				return Optional.empty();
			}
		};

		private final String translationKey;
		private final Style style;

		TooltipType() {
			this(null, null);
		}

		TooltipType(String translationKey, Style style) {
			this.translationKey = translationKey;
			this.style = style;
		}

		Optional<String> getTooltipText() {
			return Optional.of(new TextComponentTranslation(this.translationKey).setStyle(this.style).getFormattedText());
		}
	}

	public SpiceOfLifeModule() {
		super(ModConfig.modules.spiceoflife, ModConfig.modules.spiceoflife.enabled);
	}
	
	@Override
	public boolean enabled() {
		return ModConfig.modules.spiceoflife.enabled;
	}

	@Override
	protected Handler getHandler() {
		return new Handler() {
			@Method(modid = ModConsts.SPICEOFLIFE_ID)
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public final void onFoodEaten(FoodEaten evt) {
				if(SpiceOfLifeModule.this.enabled() && this.applicableToFoodHistory()) {
					SpiceOfLifeModule.this.updatePlayer(evt.player);
				}
			}

			@Method(modid = ModConsts.SPICEOFLIFE_ID)
			@SideOnly(Side.CLIENT)
			@SubscribeEvent
			public final void onTooltip(ItemTooltipEvent evt) {
				if(SpiceOfLifeModule.this.enabled() && this.applicableToFoodHistory()) {
					SpiceOfLifeModule.this.getTooltipTypeForItem(evt.getItemStack(), evt.getEntityPlayer()).getTooltipText().ifPresent(evt.getToolTip()::add);
				}
			}

			private boolean applicableToFoodHistory() {
				return !squeek.spiceoflife.ModConfig.USE_HUNGER_QUEUE && !squeek.spiceoflife.ModConfig.USE_TIME_QUEUE;
			}
		};
	}

	@Override
	protected void processPlayerUpdate(EntityPlayerMP player) {
		this.updatePlayer(player);
	}

	@Override
	protected void processPlayerDisable(EntityPlayerMP player) {
		ScalingFeastAPI.accessor.getSFFoodStats(player).applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE.createModifier(0));
	}

	@Override
	protected boolean updateFromConfig() {
		return this.usingFoodGroups == (this.usingFoodGroups = this.getConfig().useFoodGroups) || this.requiredAmount == (this.requiredAmount = MathHelper.clamp(this.getConfig().uniqueRequired, 1, squeek.spiceoflife.ModConfig.FOOD_EATEN_THRESHOLD)) || this.penaltyAmount == (this.penaltyAmount = this.getConfig().penalty);
	}

	void updatePlayer(EntityPlayer player) {
		FoodHistory history = FoodHistory.get(player);
		FoodQueue queue = history.getHistory();
		if(shouldCountHistory(history, queue)) {
			double currPenalty = SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE.getModifierValueForPlayer(player);
			int penalty;
			if(this.usingFoodGroups) {
				penalty = this.getPenaltyWithFoodGroups(history);
			}
			else {
				penalty = this.getPenaltyWithoutFoodGroups(queue);
			}
			if(currPenalty != penalty) {
				ScalingFeastAPI.accessor.getSFFoodStats(player).applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE.createModifier(penalty));
			}
		}
	}

	private int getPenaltyWithoutFoodGroups(FoodQueue queue) {
		long uniqueCount = queue.stream().map((f) -> f.itemStack.getItem()).distinct().count();
		return (int) (MathHelper.clamp(uniqueCount - this.requiredAmount, Integer.MIN_VALUE, 0) * this.penaltyAmount);
	}

	private int getPenaltyWithFoodGroups(FoodHistory history) {
		return MathHelper.clamp(history.getDistinctFoodGroups().size() - this.requiredAmount, Integer.MIN_VALUE, 0) * this.penaltyAmount;
	}

	private boolean shouldCountHistory(FoodHistory history, FoodQueue queue) {
		return history.totalFoodsEatenAllTime >= squeek.spiceoflife.ModConfig.FOOD_EATEN_THRESHOLD && queue.size() >= this.requiredAmount && !squeek.spiceoflife.ModConfig.USE_HUNGER_QUEUE && !squeek.spiceoflife.ModConfig.USE_TIME_QUEUE;
	}

	TooltipType getTooltipTypeForItem(ItemStack stack, EntityPlayer player) {
		FoodHistory history = FoodHistory.get(player);
		FoodQueue queue = history.getHistory();
		// If we eat one more item, are we set to count history?
		// Note that the FoodQueue is of fixed size but this doesn't matter if the + 1
		// exceeds the normal size; we're fine regardless
		if(history.totalFoodsEatenAllTime + 1 >= squeek.spiceoflife.ModConfig.FOOD_EATEN_THRESHOLD && queue.size() + 1 >= this.requiredAmount) {
			long newCount;
			long oldPenalty;
			List<squeek.spiceoflife.foodtracker.FoodEaten> simulatedOldHistory = queue.subList(1, queue.size());
			if(this.usingFoodGroups) {
				HashSet<FoodGroup> oldGroups = simulatedOldHistory.stream().filter((f) -> f.itemStack != ItemStack.EMPTY).collect(HashSet<FoodGroup>::new, (s, f) -> s.addAll(f.getFoodGroups()), Set<FoodGroup>::addAll);
				Set<FoodGroup> newGroups = FoodGroupRegistry.getFoodGroupsForFood(stack);
				newCount = Sets.union(newGroups, oldGroups).size();
				oldPenalty = this.getPenaltyWithFoodGroups(history);
			}
			else {
				long prevCount = simulatedOldHistory.stream().map((f) -> f.itemStack.getItem()).distinct().count();
				boolean containsNewFoodItem = simulatedOldHistory.stream().map((f) -> f.itemStack).filter(stack::isItemEqual).count() == 0;
				newCount = prevCount + (containsNewFoodItem ? 1 : 0);
				oldPenalty = this.getPenaltyWithoutFoodGroups(queue);
			}
			long newPenalty = (long) MathHelper.clamp(newCount - this.requiredAmount, Integer.MIN_VALUE, 0);
			if(newPenalty > oldPenalty) {
				return TooltipType.GOOD;
			}
			else if(newPenalty < oldPenalty) {
				return TooltipType.BAD;
			}
		}
		return TooltipType.NEUTRAL;
	}
}
