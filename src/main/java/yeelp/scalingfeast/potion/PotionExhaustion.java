package yeelp.scalingfeast.potion;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import yeelp.scalingfeast.handlers.Handler;

/**
 * The base potion for all potions that modify exhaustion rates
 * 
 * @author Yeelp
 *
 */
public abstract class PotionExhaustion extends PotionBase {

	/**
	 * The Handler for exhaustion rates from {@link PotionExhaustion}
	 * 
	 * @author Yeelp
	 *
	 */
	public static final class ExhaustionHandler extends Handler {

		@SuppressWarnings("static-method")
		@SubscribeEvent
		public void onPlayerExhaustion(ExhaustionEvent.ExhaustionAddition evt) {
			float netEffect = evt.player.getActivePotionEffects().stream().filter((e) -> e.getPotion() instanceof PotionExhaustion).map((e) -> e.getAmplifier() * ((PotionExhaustion) e.getPotion()).getModifier()).reduce(Float::sum).orElse(0.0f);
			evt.deltaExhaustion *= MathHelper.clamp(1 + netEffect, 0, Integer.MAX_VALUE);
		}
	}

	private final float modifier;

	/**
	 * Make a new PotionExhaustion. The effect is determined to be bad if
	 * {@code modifier > 0}
	 * 
	 * @param colour   the hex colour of the potion
	 * @param x        the x coord of the icon
	 * @param y        the y coord of the icon
	 * @param modifier the decimal representation of the percent to modify
	 *                 exhaustion by. For example, 0.1 is a 10% increase to
	 *                 exhaustion rates and -0.1 is a 10% decrease to exhaustion.
	 */
	public PotionExhaustion(int colour, int x, int y, float modifier) {
		super(modifier > 0, colour, x, y, false);
		this.modifier = modifier;
		if(modifier < 0) {
			this.setBeneficial();
		}
	}
	
	final float getModifier() {
		return this.modifier;
	}
}
