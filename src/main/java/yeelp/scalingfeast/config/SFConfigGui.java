/*package yeelp.scalingfeast.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;

public class SFConfigGui extends GuiConfig 
{
	public SFConfigGui(GuiScreen parent)
	{
		super(parent, getConfigElements(), ModConsts.MOD_ID, false, false, getTitle(parent));
	}

	private static String getTitle(GuiScreen parent) 
	{
		if(parent instanceof GuiModList)
		{
			return GuiConfig.getAbridgedConfigPath(ModConfig.getConfig().toString());
		}
		return "Scaling Feast Config";
	}

	private static List<IConfigElement> getConfigElements() 
	{
		List<IConfigElement> lst = new ArrayList<IConfigElement>();
		Configuration config = ModConfig.getConfig();
		if(config != null)
		{
			ConfigCategory fs = config.getCategory(ModConfig.CATEGORY_EXTENDED_FOOD_STATS);
			ConfigCategory items = config.getCategory(ModConfig.CATEGORY_ITEMS);
			ConfigCategory ascompat = config.getCategory(ModConfig.CATEGORY_COMPAT_APPLESKIN);
			ConfigCategory hud = config.getCategory(ModConfig.CATEGORY_HUD);
			lst.add(new ConfigElement(fs));
			lst.add(new ConfigElement(items));
			lst.add(new ConfigElement(hud));
			lst.add(new ConfigElement(ascompat));
		}
		return lst;
	}

}*/
