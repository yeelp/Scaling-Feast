package yeelp.scalingfeast.integration.module.solcarrot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent.FoodEaten;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.modules.SFSOLCarrotConfigCategory;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.integration.module.AbstractModule;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

/**
 * Module for Spice of Life:Carrot Edition integration
 * @author Yeelp
 *
 */
public final class SOLCarrotModule extends AbstractModule<SFSOLCarrotConfigCategory> {

	private static Pattern regularRegex = Pattern.compile("^\\d+:\\d+$");
	private static Pattern efficiencyRegex = Pattern.compile("^\\d+:\\d+(\\.\\d+)?$");
	private static final int REWARD_MESSAGES_COUNT = 8;
	static final Style GREEN_STYLE = new Style().setColor(TextFormatting.GREEN);
	static final Random rand = new Random();
	static final DecimalFormat PERCENT = new DecimalFormat("#%");

	private List<MaxHungerMilestone> hungerMilestones;
	private List<FoodEfficiencyMilestone> efficiencyMilestones;

	public SOLCarrotModule() {
		super(ModConfig.modules.sol, ModConfig.modules.sol.enabled);
	}
	
	@Override
	public boolean enabled() {
		return ModConfig.modules.sol.enabled;
	}

	@Override
	protected Handler getHandler() {
		return new Handler() {
			@Method(modid = ModConsts.SOLCARROT_ID)
			@SubscribeEvent(priority = EventPriority.LOWEST)
			public final void onFoodEaten(FoodEaten evt) {
				if(SOLCarrotModule.this.enabled()) {
					if(SOLCarrotModule.this.updatePlayer(evt.player)) {
						Deque<ITextComponent> msgs = new LinkedList<ITextComponent>();
						Optional<MaxHungerMilestone> lastRegMilestone = SOLCarrotModule.this.getLastReachedRegularMilestone(evt.player);
						Optional<FoodEfficiencyMilestone> lastEfficiencyMilestone = SOLCarrotModule.this.getLastReachedEfficiencyMilestone(evt.player);
						lastRegMilestone.map((m) -> this.buildNewRewardText(new TextComponentTranslation("modules.scalingfeast.sol.reward", m.getReward())).setStyle(SOLCarrotModule.GREEN_STYLE)).ifPresent(msgs::add);
						lastEfficiencyMilestone.map((m) -> this.buildNewRewardText(new TextComponentTranslation("modules.scalingfeast.sol.efficiencyReward", SOLCarrotModule.PERCENT.format(m.getReward()))).setStyle(SOLCarrotModule.GREEN_STYLE)).ifPresent(msgs::add);
						if(SOLCarrotModule.this.reachedAllMilestones(evt.player)) {
							msgs.add(new TextComponentTranslation("modules.scalingfeast.sol.reachedAllMilestones"));
						}
						if(msgs.isEmpty()) {
							return; //fail safe if empty, just don't send messages.
						}
						ITextComponent last = msgs.removeLast();
						boolean actionBar = SOLCarrotModule.this.getConfig().rewardMsgAboveHotbar;
						msgs.forEach((c) -> evt.player.sendStatusMessage(c.setStyle(SOLCarrotModule.GREEN_STYLE), false));
						evt.player.sendStatusMessage(actionBar ? last : last.setStyle(SOLCarrotModule.GREEN_STYLE), actionBar);
						evt.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
					}
				}
			}
			
			@Method(modid = ModConsts.SOLCARROT_ID)
			@SubscribeEvent(priority = EventPriority.HIGHEST)
			public final void onDeath(PlayerEvent.Clone evt) {
				if(SOLCarrotModule.this.enabled() && !(evt.isWasDeath() && SOLCarrotConfig.shouldResetOnDeath)) {
					SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.getEntityPlayer());
					sfstats.applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE_CARROT_EDITION.createModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE_CARROT_EDITION.getModifierValueForPlayer(evt.getOriginal())));
					sfstats.applyFoodEfficiencyModifier(SFBuiltInModifiers.FoodEfficiencyModifiers.SPICE_OF_LIFE_CARROT_EDITION.createModifier(SFBuiltInModifiers.FoodEfficiencyModifiers.SPICE_OF_LIFE_CARROT_EDITION.getModifierValueForPlayer(evt.getOriginal())));
				}
			}

			private ITextComponent buildNewRewardText(ITextComponent comp) {
				return new TextComponentString(this.getNewSplashText().getFormattedText() + " " + comp.getFormattedText()).setStyle(SOLCarrotModule.GREEN_STYLE);
			}

			private ITextComponent getNewSplashText() {
				return new TextComponentTranslation("modules.scalingfeast.sol.splash" + SOLCarrotModule.rand.nextInt(SOLCarrotModule.REWARD_MESSAGES_COUNT));
			}
		};
	}

	@Override
	protected void processPlayerUpdate(EntityPlayerMP player) {
		this.updatePlayer(player);
	}

	@Override
	protected void processPlayerDisable(EntityPlayerMP player) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(player);
		sfstats.applyFoodEfficiencyModifier(SFBuiltInModifiers.FoodEfficiencyModifiers.SPICE_OF_LIFE_CARROT_EDITION.createModifier(0));
		sfstats.applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE_CARROT_EDITION.createModifier(0));
	}

	@Override
	protected boolean updateFromConfig() {
		List<MaxHungerMilestone> newHungerMilestones = buildListOfMilestones(MaxHungerMilestone::new, this.getConfig().milestones, regularRegex);
		List<FoodEfficiencyMilestone> newEfficiencyMilestones = buildListOfMilestones(FoodEfficiencyMilestone::new, this.getConfig().foodEfficiencyMilstones, efficiencyRegex);
		boolean updated = !newHungerMilestones.equals(this.hungerMilestones) || !newEfficiencyMilestones.equals(this.efficiencyMilestones);
		this.hungerMilestones = newHungerMilestones;
		this.efficiencyMilestones = newEfficiencyMilestones;
		return updated;
	}

	boolean updatePlayer(EntityPlayer player) {
		double currMaxHungerBonus = SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE_CARROT_EDITION.getModifierValueForPlayer(player);
		double currFoodEfficiencyBonus = SFBuiltInModifiers.FoodEfficiencyModifiers.SPICE_OF_LIFE_CARROT_EDITION.getModifierValueForPlayer(player);
		double newMaxHungerBonus = this.getMaxHungerReward(player);
		double newFoodEfficiencyBonus = this.getFoodEfficiencyReward(player);
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(player);
		boolean returnStatus = false;
		if(currMaxHungerBonus != newMaxHungerBonus) {
			sfstats.applyMaxHungerModifier(SFBuiltInModifiers.MaxHungerModifiers.SPICE_OF_LIFE_CARROT_EDITION.createModifier(newMaxHungerBonus));
			returnStatus = true;
		}
		if(currFoodEfficiencyBonus != newFoodEfficiencyBonus) {
			sfstats.applyFoodEfficiencyModifier(SFBuiltInModifiers.FoodEfficiencyModifiers.SPICE_OF_LIFE_CARROT_EDITION.createModifier(newFoodEfficiencyBonus));
			returnStatus = true;
		}
		return returnStatus;
	}

	private double getMaxHungerReward(EntityPlayer player) {
		return this.getConfig().useMilestones ? getReward(player, this.hungerMilestones) : 0;
	}

	Optional<MaxHungerMilestone> getLastReachedRegularMilestone(EntityPlayer player) {
		return lastReachedMilestone(player, this.hungerMilestones);
	}

	private double getFoodEfficiencyReward(EntityPlayer player) {
		return this.getConfig().useFoodEfficiencyMilestones ? getReward(player, this.efficiencyMilestones) : 0;
	}

	Optional<FoodEfficiencyMilestone> getLastReachedEfficiencyMilestone(EntityPlayer player) {
		return lastReachedMilestone(player, this.efficiencyMilestones);
	}

	boolean reachedAllMilestones(EntityPlayer player) {
		int foodsEaten = FoodList.get(player).getProgressInfo().foodsEaten;
		return reachedAll(foodsEaten, this.hungerMilestones) && reachedAll(foodsEaten, this.efficiencyMilestones);
	}

	private static <N extends Number, M extends Milestone<N>> List<M> buildListOfMilestones(Function<String, M> constructor, String[] src, Pattern regex) {
		ArrayList<M> milestones = new ArrayList<M>(src.length);
		Predicate<String> isValid = regex.asPredicate();
		for(String string : src) {
			if(isValid.test(string)) {
				milestones.add(constructor.apply(string));
			}
		}
		return milestones;
	}

	private static <N extends Number, M extends Milestone<N>> double getReward(EntityPlayer player, List<M> milestones) {
		int foodsEaten = FoodList.get(player).getProgressInfo().foodsEaten;
		return milestones.stream().filter((m) -> foodsEaten >= m.getTarget()).mapToDouble((m) -> m.getReward().doubleValue()).sum();
	}

	private static <N extends Number, M extends Milestone<N>> Optional<M> lastReachedMilestone(EntityPlayer player, List<M> milestones) {
		int foodsEaten = FoodList.get(player).getProgressInfo().foodsEaten;
		return milestones.stream().filter((m) -> foodsEaten == m.getTarget()).findFirst();
	}

	private static <M extends Milestone<?>> boolean reachedAll(int amount, List<M> lst) {
		return lst.stream().allMatch((m) -> amount >= m.getTarget());
	}
}
