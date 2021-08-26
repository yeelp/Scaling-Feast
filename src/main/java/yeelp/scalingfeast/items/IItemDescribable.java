package yeelp.scalingfeast.items;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.handlers.Handler;

/**
 * Used for items that have player sensitive descriptions.
 * 
 * @author Yeelp
 *
 */
public interface IItemDescribable {

	final class TooltipHandler extends Handler {

		@SuppressWarnings("static-method")
		@SubscribeEvent(priority = EventPriority.LOWEST)
		@SideOnly(Side.CLIENT)
		public void onTooltip(ItemTooltipEvent evt) {
			if(evt.getEntityPlayer() != null) {
				if(evt.getItemStack().getItem() instanceof IItemDescribable) {
					IItemDescribable describable = (IItemDescribable) evt.getItemStack().getItem();
					evt.getToolTip().addAll(describable.getDescription(evt.getEntityPlayer()));
				}
			}
		}
	}

	/**
	 * Get the description for this item
	 * 
	 * @param player the player seeing this description
	 * @return the player specific description for the item.
	 */
	Collection<String> getDescription(EntityPlayer player);
}
