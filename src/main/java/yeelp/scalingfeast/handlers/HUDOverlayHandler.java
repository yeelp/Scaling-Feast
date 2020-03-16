package yeelp.scalingfeast.handlers;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.FoodStatsMap;

@SideOnly(Side.CLIENT)
public class HUDOverlayHandler extends Handler
{
	private static final ResourceLocation icons = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielements.png");
	protected int offset;
	ArrayList<Color> colors = new ArrayList<Color>();
	
	@SubscribeEvent
	public void onPreRender(RenderGameOverlayEvent.Pre evt)
	{
		if(evt.getType() == RenderGameOverlayEvent.ElementType.FOOD)
		{
			evt.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
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
			int hunger = FoodStatsMap.getExtraFoodLevel(player.getUniqueID());
			float sat = FoodStatsMap.getExtraSatLevels(player.getUniqueID());
			if(hunger > 0)
			{
				drawExtendedFood(hunger, mc, left, top + 10);
				if(sat > 0)
					drawExtendedSat(sat, mc, left, top + 10);
			}
		}
	}
	private void drawExtendedFood(int hunger, Minecraft mc, int left, int top) 
	{
		mc.getTextureManager().bindTexture(icons);
		mc.mcProfiler.startSection("extendedFood");
		GlStateManager.enableBlend();
		do
		{
			hunger -= 20;
			//getColour
			if(hunger >= 0)
				drawFood(20, mc, left, top);
			else
				drawFood(hunger + 20, mc, left, top);
		}while(hunger > -20);
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	private void drawFood(int amount, Minecraft mc, int left, int top)
	{
		float shanksNeeded = amount/2.0f;
		int x;
		int y = top;
		int i;
		for(i = 0; i < (int)shanksNeeded; i++)
		{
			x = left - i * 8 - 9;
			y = top;
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 0, 0, 9, 9);
		}
		//only true if we need a half shank
		if((int)shanksNeeded < shanksNeeded)
		{
			x = left - i * 8 - 9;
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 9, 0, 9, 9);
		}
	}
	private void drawExtendedSat(float sat, Minecraft mc, int left, int top) 
	{
		// TODO Auto-generated method stub
		
	}
	
}
