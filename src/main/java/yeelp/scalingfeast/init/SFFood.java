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

public class SFFood 
{
	public static ItemFood heartyshank;
	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> evt)
	{
		heartyshank = new HeartyShankItem(4, 0.8f);
		evt.getRegistry().register(heartyshank);
	}
	
	public static void registerRenders()
	{
		registerRender((Item) heartyshank);
	}
	
	private static void registerRender(Item i)
	{
		ScalingFeast.info(i.getRegistryName().toString());
		Minecraft m = Minecraft.getMinecraft();
		ScalingFeast.info(m.toString());
		RenderItem r = m.getRenderItem();
		ScalingFeast.info(r.toString());
		ItemModelMesher mesh = r.getItemModelMesher();
		ScalingFeast.info(mesh.toString());
		ModelResourceLocation res = new ModelResourceLocation(i.getRegistryName(), "inventory");
		ScalingFeast.info(res.toString());
		mesh.register((Item) i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		
	}
}
