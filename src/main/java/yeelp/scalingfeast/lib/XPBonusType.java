package yeelp.scalingfeast.lib;

import java.util.OptionalInt;

import net.minecraft.entity.player.EntityPlayer;

public enum XPBonusType {
	NONE {
		@Override
		protected OptionalInt getCurrentXPValue(EntityPlayer player) {
			return OptionalInt.empty();
		}
	},
	LEVEL {
		@Override
		protected OptionalInt getCurrentXPValue(EntityPlayer player) {
			return OptionalInt.of(player.experienceLevel);
		}
	},
	AMOUNT {
		@Override
		protected OptionalInt getCurrentXPValue(EntityPlayer player) {
			return OptionalInt.of(player.experienceTotal);
		}
	};

	protected abstract OptionalInt getCurrentXPValue(EntityPlayer player);
}
