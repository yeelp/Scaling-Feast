package yeelp.scalingfeast.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent;
import yeelp.scalingfeast.init.SFEnchantments;

public class EnchantmentHandler 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onExhaustionAddition(ExhaustionEvent.ExhaustionAddition evt)
	{
		int level = EnchantmentHelper.getMaxEnchantmentLevel(SFEnchantments.fasting, evt.player);
		if(level != 0)
		{
			float mod = (1-0.1f*level);
			evt.deltaExhaustion*=mod;
		}
	}
}
