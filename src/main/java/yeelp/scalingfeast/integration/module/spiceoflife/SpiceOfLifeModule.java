package yeelp.scalingfeast.integration.module.spiceoflife;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.food.FoodEvent.FoodEaten;
import squeek.spiceoflife.foodtracker.FoodHistory;
import squeek.spiceoflife.foodtracker.foodgroups.FoodGroup;
import squeek.spiceoflife.foodtracker.foodgroups.FoodGroupRegistry;
import squeek.spiceoflife.foodtracker.foodqueue.FoodQueue;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.modules.SFSpiceOfLifeConfigCategory;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.integration.module.AbstractModule;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

import java.util.*;

public final class SpiceOfLifeModule extends AbstractModule<SFSpiceOfLifeConfigCategory> {

	private boolean usingFoodGroups;
	private int requiredAmount;
	private int penaltyAmount;

	/**
	 * A pool of FoodEaten instances, for easy use in checking if two FoodEaten
	 * objects are the same by Spice of Life's terms. Just check if the two
	 * FoodEatenInstances have the same ID!
	 * 
	 * @author Yeelp
	 *
	 */
	private static final class FoodEatenInstance {
		private static final Map<Integer, FoodEatenInstance> POOL = Maps.newHashMap();

		private final int id;

		private FoodEatenInstance(int id) {
			this.id = id;
		}

		static FoodEatenInstance get(squeek.spiceoflife.foodtracker.FoodEaten food) {
			return POOL.computeIfAbsent(getID(food), FoodEatenInstance::new);
		}

		/**
		 * This is a bijection between the set of positive integers and its Cartesian
		 * product. We use this to determine a unique ID for each FoodEatenInstance that
		 * is guaranteed to not clash. And that is because we can add it to the object
		 * pool if it is a new ID and use that to count distinct food items eaten.
		 * 
		 * @param food food item.
		 * @return The ID
		 */
		private static int getID(squeek.spiceoflife.foodtracker.FoodEaten food) {
			int i = Item.getIdFromItem(food.itemStack.getItem());
			int j = food.itemStack.getItemDamage();
			return ((i + j - 1) * (i + j - 2)) / 2 + i;
		}

		public int getID() {
			return this.id;
		}

		@Override
		public int hashCode() {
			return Integer.hashCode(this.id);
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof FoodEatenInstance) {
				FoodEatenInstance instance = (FoodEatenInstance) obj;
				return instance.getID() == this.id;
			}
			return false;
		}
	}

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
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public void onFoodEaten(FoodEaten evt) {
				if(SpiceOfLifeModule.this.enabled() && SpiceOfLifeModule.applicableToFoodHistory() && !evt.player.world.isRemote) {
					SpiceOfLifeModule.this.updatePlayer(evt.player);
				}
			}

			@SideOnly(Side.CLIENT)
			@SubscribeEvent
			public void onTooltip(ItemTooltipEvent evt) {
				if(SpiceOfLifeModule.this.enabled() && SpiceOfLifeModule.applicableToFoodHistory() && evt.getEntityPlayer() != null && evt.getItemStack().getItem() instanceof ItemFood) {
					SpiceOfLifeModule.this.getTooltipTypeForItem(evt.getItemStack(), evt.getEntityPlayer()).getTooltipText().ifPresent(evt.getToolTip()::add);
				}
			}

			@SubscribeEvent(priority = EventPriority.HIGHEST)
			public void onClone(Clone evt) {
				if(SpiceOfLifeModule.this.enabled() && SpiceOfLifeModule.applicableToFoodHistory() && (!evt.isWasDeath() || squeek.spiceoflife.ModConfig.FOOD_HISTORY_PERSISTS_THROUGH_DEATH)) {
					ScalingFeastAPI.accessor.getSFFoodStats(evt.getEntityPlayer()).applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE.createModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE.getModifierValueForPlayer(evt.getOriginal())));
				}
			}
		};
	}

	@Override
	protected void processPlayerUpdate(EntityPlayerMP player) {
		if(applicableToFoodHistory()) {
			this.updatePlayer(player);
		}
	}

	@Override
	protected void processPlayerDisable(EntityPlayerMP player) {
		ScalingFeastAPI.accessor.getSFFoodStats(player).applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE.createModifier(0));
	}

	@Override
	protected boolean updateFromConfig() {
		boolean changed = false;
		if(this.usingFoodGroups != this.getConfig().useFoodGroups) {
			changed = true;
			this.usingFoodGroups = this.getConfig().useFoodGroups;
		}
		if(this.requiredAmount != MathHelper.clamp(this.getConfig().uniqueRequired, 1, squeek.spiceoflife.ModConfig.FOOD_HISTORY_LENGTH)) {
			changed = true;
			this.requiredAmount = MathHelper.clamp(this.getConfig().uniqueRequired, 1, squeek.spiceoflife.ModConfig.FOOD_HISTORY_LENGTH);
		}
		if(this.penaltyAmount != this.getConfig().penalty) {
			changed = true;
			this.penaltyAmount = this.getConfig().penalty;
		}
		return changed;
	}

	void updatePlayer(EntityPlayer player) {
		FoodHistory history = FoodHistory.get(player);
		FoodQueue queue = history.getHistory();
		if(this.shouldCountHistory(history, queue)) {
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
		return (int) (Math.min(getUniqueCount(queue) - this.requiredAmount, 0) * this.penaltyAmount);
	}

	private int getPenaltyWithFoodGroups(FoodHistory history) {
		return Math.min(history.getDistinctFoodGroups().size() - this.requiredAmount, 0) * this.penaltyAmount;
	}

	private boolean shouldCountHistory(FoodHistory history, FoodQueue queue) {
		return history.totalFoodsEatenAllTime >= squeek.spiceoflife.ModConfig.FOOD_EATEN_THRESHOLD && queue.size() >= this.requiredAmount;
	}

	static boolean applicableToFoodHistory() {
		return !squeek.spiceoflife.ModConfig.USE_HUNGER_QUEUE && !squeek.spiceoflife.ModConfig.USE_TIME_QUEUE;
	}

	TooltipType getTooltipTypeForItem(ItemStack stack, EntityPlayer player) {
		FoodHistory history = FoodHistory.get(player);
		FoodQueue queue = history.getHistory();
		// If we eat one more item, are we set to count history?
		// Note that the FoodQueue is of fixed size but this doesn't matter if the + 1
		// exceeds the normal size; we're fine regardless
		if(applicableToFoodHistory() && history.totalFoodsEatenAllTime + 1 >= squeek.spiceoflife.ModConfig.FOOD_EATEN_THRESHOLD && queue.size() + 1 >= this.requiredAmount) {
			long newCount;
			long oldPenalty;
			final int startCount = queue.size() == squeek.spiceoflife.ModConfig.FOOD_HISTORY_LENGTH ? 1 : 0;
			// new list is needed so simulated food eaten doesn't actually get added to
			// actual food queue
			List<squeek.spiceoflife.foodtracker.FoodEaten> simulatedNewQueue = Lists.newLinkedList(queue.subList(startCount, queue.size()));
			simulatedNewQueue.add(new squeek.spiceoflife.foodtracker.FoodEaten(stack, player));
			if(this.usingFoodGroups) {
				HashSet<FoodGroup> oldGroups = simulatedNewQueue.stream().filter((f) -> f.itemStack != ItemStack.EMPTY).collect(HashSet::new, (s, f) -> s.addAll(f.getFoodGroups()), Set::addAll);
				Set<FoodGroup> newGroups = FoodGroupRegistry.getFoodGroupsForFood(stack);
				newCount = Sets.union(newGroups, oldGroups).size();
				oldPenalty = this.getPenaltyWithFoodGroups(history);
			}
			else {
				newCount = getUniqueCount(simulatedNewQueue);
				oldPenalty = this.getPenaltyWithoutFoodGroups(queue);
			}
			if(queue.size() + 1 == this.requiredAmount) {
				// if the next food we eat just brings us to the minimum diversity count,
				// then we set the old penalty to zero, since these penalties aren't currently
				// in effect yet
				oldPenalty = 0;
			}
			long newPenalty = Math.min(newCount - this.requiredAmount, 0) * this.penaltyAmount;
			if(newPenalty > oldPenalty) {
				return TooltipType.GOOD;
			}
			else if(newPenalty < oldPenalty) {
				return TooltipType.BAD;
			}
		}
		return TooltipType.NEUTRAL;
	}

	private static long getUniqueCount(Collection<squeek.spiceoflife.foodtracker.FoodEaten> foodsEaten) {
		return foodsEaten.stream().map(FoodEatenInstance::get).distinct().count();
	}
}
