package yeelp.scalingfeast.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.items.EnchantedIronAppleItem;
import yeelp.scalingfeast.items.HeartyShankItem;
import yeelp.scalingfeast.items.IronAppleItem;

public class SFFood 
{
	public static ItemFood heartyshank;
	public static ItemFood ironapple;
	public static ItemFood enchantedironapple;
	public static void init()
	{
		heartyshank = new HeartyShankItem(4, 0.8f);
		ironapple = new IronAppleItem();
		enchantedironapple = new EnchantedIronAppleItem();
		ForgeRegistries.ITEMS.register(heartyshank);
		ForgeRegistries.ITEMS.register(ironapple);
		ForgeRegistries.ITEMS.register(enchantedironapple);
	}
	
	public static void registerRenders()
	{
		registerRender((Item) heartyshank);
		registerRender((Item) ironapple);
		registerRender((Item) enchantedironapple);
	}
	
	private static void registerRender(Item i)
	{		
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
}
