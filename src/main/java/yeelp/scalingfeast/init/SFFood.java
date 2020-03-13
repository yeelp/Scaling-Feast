package yeelp.scalingfeast.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.items.HeartyShankItem;
import yeelp.scalingfeast.items.IronAppleItem;

public class SFFood 
{
	public static ItemFood heartyshank;
	public static ItemFood ironapple;
	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> evt)
	{
		heartyshank = new HeartyShankItem(4, 0.8f);
		ironapple = new IronAppleItem();
		evt.getRegistry().register(heartyshank);
		evt.getRegistry().register(ironapple);
	}
	
	public static void registerRenders()
	{
		registerRender((Item) heartyshank);
		registerRender((Item) ironapple);
	}
	
	private static void registerRender(Item i)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register((Item) i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
}
