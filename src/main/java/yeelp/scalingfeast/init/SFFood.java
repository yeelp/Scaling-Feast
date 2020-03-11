package yeelp.scalingfeast.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.items.HeartyShankItem;

public class SFFood 
{
	public static ItemFood shank;
	
	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> evt)
	{
		shank = new HeartyShankItem(4, 0.8f);
		ForgeRegistries.ITEMS.register(shank);
	}
}
