package yeelp.scalingfeast.lib;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier.Attribute;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier.Operation;

public final class SFBuiltInModifiers {
	public static final class MaxHungerModifiers {
		public static final BuiltInModifier SHANK = new BuiltInModifier.StaticOperationModifier("3d034127-e9ca-4ab5-be27-005ad5e83cec", "Hearty Shank Bonus", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier PENALTY = new BuiltInModifier.StaticOperationModifier("a9deb46b-b0c6-4a67-9add-0a8e0586fb61", "Starvation Penalty", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier DEATH = new BuiltInModifier.StaticOperationModifier("52f8899e-7b0b-499b-a4c7-5b6bdb27060c", "Death Penalty", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier XP = new BuiltInModifier.StaticOperationModifier("157f01fc-05b7-4443-8654-1cbc1251f7ed", "XP Bonus", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier SPICE_OF_LIFE_CARROT_EDITION = new BuiltInModifier.StaticOperationModifier("e5e6cab3-44df-4781-8dcb-9585a0de4eb7", "Spice of Life: Carrot Edition Bonus", Operation.ADD, Attribute.MAX_HUNGER);
		public static final BuiltInModifier SPICE_OF_LIFE = new BuiltInModifier.StaticOperationModifier("de1e2c2a-574f-40db-a010-1fa2a12f744a", "Spice of Life Penalty", Operation.ADD, Attribute.MAX_HUNGER);
	}
	
	public static final class FoodEfficiencyModifiers {
		public static final BuiltInModifier XP = new BuiltInModifier.DynamicOperationModifier("24bd97f0-392d-4c7f-8738-bfbaf34340f4", "XP Bonus", ModConfig.features.xpBonuses.foodEfficiencyBonusType::getOperation, Attribute.FOOD_EFFICIENCY);
		public static final BuiltInModifier SPICE_OF_LIFE_CARROT_EDITION = new BuiltInModifier.DynamicOperationModifier("1736a445-1a35-4230-8e3a-7aedda394df2", "Spice of Life: Carrot Edition Bonus", ModConfig.modules.sol.foodEfficiencyMilestoneType::getOperation, Attribute.FOOD_EFFICIENCY);
		public enum FoodEfficiencyOperations {
			STACK_ADDITIVELY(BuiltInModifier.Operation.MULTIPLY),
			STACK_MULTIPLICATIVELY(BuiltInModifier.Operation.PERCENT_MULTIPLY);
			
			private final BuiltInModifier.Operation op;
			
			private FoodEfficiencyOperations(BuiltInModifier.Operation op) {
				this.op = op;
			}
			
			BuiltInModifier.Operation getOperation() {
				return this.op;
			}
		}
	}
		
	public static abstract class BuiltInModifier {
		enum Operation {
			ADD,
			MULTIPLY,
			PERCENT_MULTIPLY;
		}
		
		enum Attribute {
			MAX_HUNGER(SFFoodStats::getMaxHungerModifier),
			FOOD_EFFICIENCY(SFFoodStats::getFoodEfficiencyModifier);
			
			private final BiFunction<SFFoodStats, UUID, Optional<AttributeModifier>> modifierExtractor;

			private Attribute(BiFunction<SFFoodStats, UUID, Optional<AttributeModifier>> modifierExtractor) {
				this.modifierExtractor = modifierExtractor;
			}
			
			Optional<AttributeModifier> getAttributeModifier(SFFoodStats sfFoodStats, UUID uuid) {
				return this.modifierExtractor.apply(sfFoodStats, uuid);
			}
		}
		private final UUID uuid;
		private final String name;
		private final Attribute attribute;
		
		BuiltInModifier(String uuid, String name, Attribute attribute) {
			this.uuid = UUID.fromString(uuid);
			this.name = name;
			this.attribute = attribute;
		}
		
		public AttributeModifier createModifier(double amount) {
			return new AttributeModifier(this.uuid, this.name, amount, this.getOperation());
		}
		
		public double getModifierValueForPlayer(EntityPlayer player) {
			return this.attribute.getAttributeModifier(ScalingFeastAPI.accessor.getSFFoodStats(player), this.getUUID()).map(AttributeModifier::getAmount).orElse(0.0);
		}

		public UUID getUUID() {
			return this.uuid;
		}

		public String getName() {
			return this.name;
		}	
		
		public byte getOperation() {
			return (byte) this.getOp().ordinal();
		}
		
		protected abstract Operation getOp();
		
		public static final class StaticOperationModifier extends BuiltInModifier {

			private final Operation op;
			StaticOperationModifier(String uuid, String name, Operation op, Attribute attribute) {
				super(uuid, name, attribute);
				this.op = op;
			}
			
			@Override
			protected Operation getOp() {
				return this.op;
			}
		}
		
		public static final class DynamicOperationModifier extends BuiltInModifier {

			private final Supplier<Operation> opGetter;
			DynamicOperationModifier(String uuid, String name, Supplier<Operation> opGetter, Attribute attribute) {
				super(uuid, name, attribute);
				this.opGetter = opGetter;
			}
			
			@Override
			protected Operation getOp() {
				return this.opGetter.get();
			}
		}
	}
}
