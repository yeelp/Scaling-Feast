package yeelp.scalingfeast.init;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.lib.SFBuiltInModifiers;
import yeelp.scalingfeast.lib.SFBuiltInModifiers.BuiltInModifier;

public final class SFAttributes extends Handler {
	public static final IAttribute FOOD_EFFICIENCY = new RangedAttribute((IAttribute) null, "scalingfeast.foodEfficiency", 1.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute MAX_HUNGER_MOD = new RangedAttribute((IAttribute) null, "scalingfeast.maxHungerMod", 1.0, -2048.0, 2048.0).setShouldWatch(true);

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
	@SubscribeEvent
	public void onPlayerClone(Clone evt) {
		ImmutableList.of(FOOD_EFFICIENCY, MAX_HUNGER_MOD).stream().map((a) -> getAllBuiltInModifiers(a, evt.getOriginal())).forEach(evt.getEntityPlayer().getAttributeMap()::applyAttributeModifiers);
	}
	
	private static Multimap<String, AttributeModifier> getAllBuiltInModifiers(IAttribute attribute, EntityPlayer player) {
		Collection<BuiltInModifier> modsToGet = null;
		if(attribute == FOOD_EFFICIENCY) {
			modsToGet = SFBuiltInModifiers.FoodEfficiencyModifiers.ALL;
		}
		else {
			modsToGet = SFBuiltInModifiers.MaxHungerModifiers.ALL;
		}
		Multimap<String, AttributeModifier> mods = HashMultimap.<String, AttributeModifier>create();
		mods.putAll(attribute.getName(), modsToGet.stream().map((m) -> m.createModifier(m.getModifierValueForPlayer(player))).collect(Collectors.toList()));
		return mods;
	}
}
