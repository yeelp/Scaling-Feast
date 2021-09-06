package yeelp.scalingfeast.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.hunger.HealthRegenEvent.AllowRegen;
import squeek.applecore.api.hunger.HealthRegenEvent.AllowSaturatedRegen;

/**
 * The regen criterion for the regen module
 * @author Yeelp
 *
 */
public final class RegenCriterion {
	
	/**
	 * Hunger regen criterion
	 * @author Yeelp
	 *
	 */
	public enum Hunger {
		/**
		 * Vanilla conditions, just leaves the result as is.
		 */
		VANILLA {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return currResult;
			}
		},
		/**
		 * One possible extension of vanilla regen. Must have at least {@code maxHunger - 2} hunger to regen.
		 */
		MAX_MINUS_TWO {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return player.getFoodStats().getFoodLevel() >= AppleCoreAPI.accessor.getMaxHunger(player) - 2 ? Result.ALLOW : Result.DENY;
			}
		},
		/**
		 * One possible extension of vanilla regen. Must have at least 90% of one's max hunger to regen. 
		 */
		NINETY_PERCENT {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return player.getFoodStats().getFoodLevel() >= Math.floor(AppleCoreAPI.accessor.getMaxHunger(player) * 0.9) ? Result.ALLOW : Result.DENY;
			}
		},
		/**
		 * Disables hunger regen, leaves saturated regen alone.
		 */
		DISABLED {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return Result.DENY;
			}
		};
		
		/**
		 * Determine the result for hunger regen
		 * @param player player in question
		 * @param currResult the current Result.
		 * @return A new Result to set for {@link AllowRegen} events.
		 */
		public abstract Result determineResult(EntityPlayer player, Result currResult);
	}
	
	/**
	 * Saturation regen
	 * @author Yeelp
	 *
	 */
	public enum Saturation {
		/**
		 * Vanilla conditions; leaves things as they are.
		 */
		VANILLA {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return currResult;
			}
		},
		/**
		 * Scaling Feast's extension of vanilla's conditions. Only allow if full hunger with some saturation.
		 */
		FULL {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return player.getFoodStats().getFoodLevel() >= AppleCoreAPI.accessor.getMaxHunger(player) && player.getFoodStats().getSaturationLevel() > 0.0f ? Result.ALLOW : Result.DENY;
			}
		},
		/**
		 * Disables saturated regen, but regular regen can still occur.
		 */
		DISABLED {
			@Override
			public Result determineResult(EntityPlayer player, Result currResult) {
				return Result.DENY;
			}
		};
		
		/**
		 * Determine the result for saturated regen
		 * @param player player in question
		 * @param currResult the current Result
		 * @return a new Result to set for {@link AllowSaturatedRegen} events.
		 */
		public abstract Result determineResult(EntityPlayer player, Result currResult);
	}
}
