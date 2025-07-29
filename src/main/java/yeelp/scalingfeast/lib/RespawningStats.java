package yeelp.scalingfeast.lib;

import net.minecraft.entity.player.EntityPlayer;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.config.ModConfig;

public enum RespawningStats {
	MAX_BOTH {
		@Override
		public int getRespawningHungerCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return AppleCoreAPI.accessor.getMaxHunger(newPlayer);
		}

		@Override
		protected float getRespawningSaturationCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return AppleCoreAPI.accessor.getMaxHunger(newPlayer);
		}
	},
	MAX_HUNGER {
		@Override
		public int getRespawningHungerCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return AppleCoreAPI.accessor.getMaxHunger(newPlayer);
		}

		@Override
		protected float getRespawningSaturationCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return 5.0f;
		}
	},
	PERSIST {
		@Override
		public int getRespawningHungerCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return oldPlayer.getFoodStats().getFoodLevel();
		}

		@Override
		protected float getRespawningSaturationCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return oldPlayer.getFoodStats().getSaturationLevel();
		}
	},
	VANILLA {
		@Override
		public int getRespawningHungerCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return 20;
		}

		@Override
		protected float getRespawningSaturationCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return 5.0f;
		}
	},
	STARTING_AMOUNT {
		@Override
		public int getRespawningHungerCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return ModConfig.general.startingHunger;
		}

		@Override
		protected float getRespawningSaturationCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
			return Math.min(ModConfig.general.startingHunger, 5.0f);
		}
	};
	
	protected abstract int getRespawningHungerCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer);
	
	protected abstract float getRespawningSaturationCandidate(EntityPlayer oldPlayer, EntityPlayer newPlayer);
	
	public final int getRespawningHunger(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
		int candidate = this.getRespawningHungerCandidate(oldPlayer, newPlayer);
		if(candidate <= 0) {
			return RespawningStats.STARTING_AMOUNT.getRespawningHungerCandidate(oldPlayer, newPlayer);
		}
		return candidate;
	}
	
	public final float getRespawningSaturation(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
		return Math.min(ScalingFeastAPI.accessor.getPlayerSaturationCap(newPlayer), this.getRespawningSaturationCandidate(oldPlayer, newPlayer));
	}
}
