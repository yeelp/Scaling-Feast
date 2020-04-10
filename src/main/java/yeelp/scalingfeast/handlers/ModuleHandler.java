package yeelp.scalingfeast.handlers;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.spiceoflife.foodtracker.FoodHistory;
import squeek.spiceoflife.foodtracker.foodgroups.FoodGroup;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.helpers.SpiceOfLifeHelper;

public class ModuleHandler extends Handler 
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	@Optional.Method(modid = "spiceoflife")
	public void onFoodEaten(FoodEvent.FoodEaten evt)
	{
		if(SpiceOfLifeHelper.isEnabled() && ModConfig.modules.spiceoflife.enabled)
		{
			Set<?> entries;
			if(ModConfig.modules.spiceoflife.useFoodGroups)
			{
				entries = FoodHistory.get(evt.player).getDistinctFoodGroups();
			}
		}
	}
}
