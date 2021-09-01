package yeelp.scalingfeast.lib;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.init.SFAttributes;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier.Attribute;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier.Operation;

public final class SFBuiltInModifiers {
	public static final class MaxHungerModifiers {
		public static final BuiltInModifier SHANK = new BuiltInModifier("3d034127-e9ca-4ab5-be27-005ad5e83cec", "Hearty Shank Bonus", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier PENALTY = new BuiltInModifier("a9deb46b-b0c6-4a67-9add-0a8e0586fb61", "Starvation Penalty", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier DEATH = new BuiltInModifier("52f8899e-7b0b-499b-a4c7-5b6bdb27060c", "Death Penalty", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier XP = new BuiltInModifier("157f01fc-05b7-4443-8654-1cbc1251f7ed", "XP Bonus", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier SPICE_OF_LIFE_CARROT_EDITION = new BuiltInModifier("e5e6cab3-44df-4781-8dcb-9585a0de4eb7", "Spice of Life: Carrot Edition Bonus", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier SPICE_OF_LIFE = new BuiltInModifier("de1e2c2a-574f-40db-a010-1fa2a12f744a", "Spice of Life Penalty", Operation.ADD, Attribute.MAX_HUNGER);
		
		public static final Collection<BuiltInModifier> ALL = ImmutableList.of(SHANK, PENALTY, DEATH, XP, SPICE_OF_LIFE_CARROT_EDITION, SPICE_OF_LIFE);
	}
	
	public static final class FoodEfficiencyModifiers {
		public static final BuiltInModifier XP = new BuiltInModifier("24bd97f0-392d-4c7f-8738-bfbaf34340f4", "XP Bonus", Operation.PERCENT_MULTIPLY, Attribute.FOOD_EFFICIENCY);
		public static final BuiltInModifier SPICE_OF_LIFE_CARROT_EDITION = new BuiltInModifier("1736a445-1a35-4230-8e3a-7aedda394df2", "Spice of Life: Carrot Edition Bonus", Operation.PERCENT_MULTIPLY, Attribute.FOOD_EFFICIENCY);
	
		public static final Collection<BuiltInModifier> ALL = ImmutableList.of(XP, SPICE_OF_LIFE_CARROT_EDITION);
	}
	
	public static final class BuiltInModifier {
		enum Operation {
			ADD,
			MULTIPLY,
			PERCENT_MULTIPLY;
		}
		
		enum Attribute {
			MAX_HUNGER(SFAttributes.MAX_HUNGER_MOD),
			FOOD_EFFICIENCY(SFAttributes.FOOD_EFFICIENCY);
			
			IAttribute attribute;

			Attribute(IAttribute attribute) {
				this.attribute = attribute;
			}
		}
		private final UUID uuid;
		private final String name;
		private final Operation op;
		private final Attribute attribute;
		
		BuiltInModifier(String uuid, String name, Operation op, Attribute attribute) {
			this.uuid = UUID.fromString(uuid);
			this.name = name;
			this.op = op;
			this.attribute = attribute;
		}
		
		public AttributeModifier createModifier(double amount) {
			return new AttributeModifier(this.uuid, this.name, amount, this.op.ordinal());
		}
		
		public double getModifierValueForPlayer(EntityPlayer player) {
			Function<UUID, Optional<AttributeModifier>> f;
			switch(this.attribute) {
				case FOOD_EFFICIENCY:
					f = ScalingFeastAPI.accessor.getSFFoodStats(player)::getFoodEfficiencyModifier;
					break;
				default:
					f = ScalingFeastAPI.accessor.getSFFoodStats(player)::getMaxHungerModifier;
					break;
			}
			return getCurrentAttributeValue(f, this);
		}

		public UUID getUUID() {
			return this.uuid;
		}

		public String getName() {
			return this.name;
		}	
		
		public byte getOperation() {
			return (byte) this.op.ordinal();
		}
		
		private static double getCurrentAttributeValue(Function<UUID, Optional<AttributeModifier>> f, BuiltInModifier mod) {
			return f.apply(mod.getUUID()).map(AttributeModifier::getAmount).orElse(0.0);
		}
	}
	
	
}
