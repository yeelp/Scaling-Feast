package yeelp.scalingfeast.handlers;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.FoodStatsMap;

public class HUDOverlayHandler extends Handler
{
	private static final ResourceLocation icons = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielements.png");
	protected int offset;
	ArrayList<Color> colors = new ArrayList<Color>();
	
	@Override
	public void register()
	{
		
	}
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onRender(RenderGameOverlayEvent.Post evt)
	{
		if(evt.getType() != RenderGameOverlayEvent.ElementType.FOOD)
		{
			return;
		}
		else if(evt.isCanceled())
		{
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		
		ScaledResolution res = evt.getResolution();
		
		offset = GuiIngameForge.right_height;
		
		int left = res.getScaledWidth()/2 + 91;
		int top = res.getScaledHeight() - offset;
		
		if(FoodStatsMap.hasStats(player.getUniqueID()))
		{
			drawExtendedFood();
		}
	}
	
	private void drawExtendedFood() 
	{
		// TODO Auto-generated method stub
		
	}
	
}
