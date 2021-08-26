package yeelp.scalingfeast.util;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.handlers.Handler;

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
}
