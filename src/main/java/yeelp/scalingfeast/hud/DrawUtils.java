package yeelp.scalingfeast.hud;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.util.Colour;

@SideOnly(Side.CLIENT)
public abstract class DrawUtils {

	private static int[] jitter;
	private static final Random RNG = new Random();

	private static final List<Colour> HUNGER_COLOURS = Lists.newArrayList();
	private static final List<Colour> SAT_COLOURS = Lists.newArrayList();
	private static final List<Colour> BLOAT_COLOURS = Lists.newArrayList();
	private static final List<Colour> HUNGER_COLOURS_FALLBACK = colourizeFallback("ff9d00", "ffee00", "00ff00", "0000ff", "00ffff", "e100ff", "ffffff");
	private static final List<Colour> SAT_COLOURS_FALLBACK = colourizeFallback("d70000", "d700d7", "6400d7", "00d3d7", "64d700", "d7d700", "d7d7d7");
	private static final List<Colour> BLOAT_COLOURS_FALLBACK = colourizeFallback("ffff6e", "ff6e6e", "6eff6e", "6effff", "6e6eff", "ff6eff", "e6e6e6");
	private static int satColour = 0xffff55, satColourEmpty = 0x555555;
	
	public static void calculateJitterAmount(int updateCounter, EntityPlayer player) {
		RNG.setSeed(updateCounter * 70643);
		FoodStats stats = player.getFoodStats();
		int foodLevel = stats.getFoodLevel();
		float satLevel = stats.getSaturationLevel();
		short bloatLevel = ScalingFeastAPI.accessor.getSFFoodStats(player).getBloatedHungerAmount();
		// length 20 for hunger plus bloated
		jitter = new int[20];
		int regen = -1;
		if(player.isPotionActive(SFPotion.metabolism)) {
			regen = updateCounter % 25;
		}
		if(satLevel == 0) {
			int jitterFrequency = (foodLevel + bloatLevel) * 3 + 1;
			for(int i = 0; i < jitter.length; i++) {
				if(updateCounter % jitterFrequency == 0) {
					jitter[i] += RNG.nextInt(3) - 1;
				}
			}
		}
		if(regen >= 0 && regen < jitter.length) {
			jitter[regen] -= 2;
		}
	}

	public static int[] getJitterAmount() {
		return jitter;
	}

	private static final void colourize(String[] colours, List<Colour> lst, List<Colour> fallback) {
		lst.clear();
		Arrays.stream(colours).map(String::trim).filter(Predicates.not(String::isEmpty)).map(Colour::new).forEach(lst::add);
		if(lst.size() == 0) {
			lst.addAll(fallback);
		}
	}
	
	private static List<Colour> colourizeFallback(String...strings) {
		return Arrays.stream(strings).collect(Lists::newArrayList, (l, s) -> l.add(new Colour(s)), (l1, l2) -> l1.addAll(l2));
	}

	public static final void updateColours() {
		colourize(ModConfig.hud.Hcolours, HUNGER_COLOURS, HUNGER_COLOURS_FALLBACK);
		colourize(ModConfig.hud.Scolours, SAT_COLOURS, SAT_COLOURS_FALLBACK);
		colourize(ModConfig.hud.Bcolours, BLOAT_COLOURS, BLOAT_COLOURS_FALLBACK);
	}
	
	public static final void updateTextColours() {
		try {
			satColour = Integer.decode("0x" + ModConfig.hud.satTextColour);
		}
		catch(NumberFormatException e) {
			ScalingFeast.err("Error setting saturation text colour! " + "0x" + ModConfig.hud.satTextColour + " isn't a valid colour!");
			e.printStackTrace();
		}
		try {
			satColourEmpty = Integer.decode("0x" + ModConfig.hud.satTextColourEmpty);
		}
		catch(NumberFormatException e) {
			ScalingFeast.err("Error setting empty saturation text colour! " + "0x" + ModConfig.hud.satTextColourEmpty + " isn't a valid colour!");
			e.printStackTrace();
		}
	}
	
	static int uCoordShiftForSaturation(float excess) {
		if(excess > 0.5) {
			return 9;
		}
		else if(excess > 0.25) {
			return 18;
		}
		else if(excess > 0) {
			return 27;
		}
		return 0;
	}
	
	private static Colour getColourForBar(int barNo, List<Colour> lst) {
		return lst.get(barNo % lst.size());
	}
	
	static Colour getHungerColour(int hunger) {
		return getColourForBar(Math.max(intToIndex(hunger) + (ModConfig.hud.replaceVanilla ? 0 : -1), 0), HUNGER_COLOURS);
	}
	
	static final Colour getBloatedColour(int bloat) {
		return getColourForBar(intToIndex(bloat), BLOAT_COLOURS);
	}
	
	static final Colour getSaturationColour(float sat) {
		return getColourForBar((int)((sat/20) - 0.001f), SAT_COLOURS);
	}
	
	static final int getSaturationTextColour(float sat) {
		return sat > 0 ? satColour : satColourEmpty;
	}
	
	private static final int intToIndex(int a) {
		//use max sat const to upcast to float
		return (int) ((a/ModConsts.VANILLA_MAX_SAT) - 0.01f);
	}
}