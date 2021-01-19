package yeelp.scalingfeast.handlers;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.items.HeartyShankItem;

public class TooltipHandler extends Handler
{
	@SubscribeEvent(priority=EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public void onTooltip(ItemTooltipEvent evt)
	{
		Item item = evt.getItemStack().getItem();
		List<String> tooltips = evt.getToolTip();
		if(item instanceof HeartyShankItem && evt.getEntityPlayer() != null)
		{
			tooltips.addAll(1, HeartyShankItem.buildTooltips(evt.getEntityPlayer()));
		}
	}
}
