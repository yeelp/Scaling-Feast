package yeelp.scalingfeast.init;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;

public final class SFAttributes extends Handler {
	public static final IAttribute FOOD_EFFICIENCY = new RangedAttribute(null, "scalingfeast.foodEfficiency", 1.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute MAX_HUNGER_MOD = new RangedAttribute(null, "scalingfeast.maxHungerMod", 1.0, -2048.0, 2048.0).setShouldWatch(true);

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPlayerConstruction(EntityConstructing evt) {
		if(evt.getEntity() instanceof EntityPlayer) {
			AbstractAttributeMap playerAttributes = ((EntityPlayer) evt.getEntity()).getAttributeMap();
			playerAttributes.registerAttribute(FOOD_EFFICIENCY).setBaseValue(1.0);
			playerAttributes.registerAttribute(MAX_HUNGER_MOD).setBaseValue(ModConfig.general.startingHunger - ModConsts.VANILLA_MAX_HUNGER);
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerClone(Clone evt) {
		SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(evt.getEntityPlayer());
		for(SFBuiltInModifiers.BuiltInModifier mod : ImmutableList.of(SFBuiltInModifiers.MaxHungerModifiers.SHANK, SFBuiltInModifiers.MaxHungerModifiers.PENALTY, SFBuiltInModifiers.MaxHungerModifiers.DEATH)) {
			sfstats.applyMaxHungerModifier(mod.createModifier(mod.getModifierValueForPlayer(evt.getOriginal())));
		}
	}
}
