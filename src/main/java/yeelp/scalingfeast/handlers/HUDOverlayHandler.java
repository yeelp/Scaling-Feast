package yeelp.scalingfeast.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import static yeelp.scalingfeast.ModConfig.HUDCategory.DisplayStyle;

import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.util.Colour;
import yeelp.scalingfeast.util.FoodStatsMap;

@SideOnly(Side.CLIENT)
public class HUDOverlayHandler extends Handler
{
	private static final ResourceLocation icons = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielements.png");
	protected int offset;
	ArrayList<Colour> colours = new ArrayList<Colour>();
	ArrayList<Colour> satColours = new ArrayList<Colour>();
	Random rand = new Random();
	private String style;
	
	public HUDOverlayHandler()
	{
		if(isEmpty(ModConfig.hud.Hcolours))
		{
			colours.add(new Colour("ff9d00"));
			colours.add(new Colour("ffee00"));
			colours.add(new Colour("00ff00"));
			colours.add(new Colour("0000ff"));
			colours.add(new Colour("00ffff"));
			colours.add(new Colour("e100ff"));
			colours.add(new Colour("ffffff"));
		}
		else
		{
			colours = colourize(ModConfig.hud.Hcolours);
		}
		
		if(isEmpty(ModConfig.hud.Scolours))
		{
			satColours.add(new Colour("d70000"));
			satColours.add(new Colour("d700d7"));
			satColours.add(new Colour("6400d7"));
			satColours.add(new Colour("00d3d7"));
			satColours.add(new Colour("64d700"));
			satColours.add(new Colour("d7d700"));
			satColours.add(new Colour("d7d7d7"));
		}
		else
		{
			satColours = colourize(ModConfig.hud.Scolours);
		}
	}

	@SubscribeEvent
	public void onPreRender(RenderGameOverlayEvent.Pre evt)
	{
		if(evt.getType() == RenderGameOverlayEvent.ElementType.FOOD)
		{
			if(ModConfig.hud.style == DisplayStyle.OVERLAY && Minecraft.getMinecraft().player.getFoodStats().getSaturationLevel() == 0)
			{
				//In order to make the HUD look nice when the player has no saturation, we need to completely remove
				//The hunger bar and re draw it so we have control over the 'jittering'.
				evt.setCanceled(true);
				Minecraft mc = Minecraft.getMinecraft();
				EntityPlayer player = mc.player;
				
				ScaledResolution res = evt.getResolution();
				
				offset = GuiIngameForge.right_height;
				
				int left = res.getScaledWidth()/2 + 91;
				int top = res.getScaledHeight() - offset;
				//If we have AppleSkin, we need to redraw the whole exhaustion bar.
				if(ScalingFeast.hasAppleSkin && ModConfig.compat.appleskin.drawExhaustion)
				{	
					drawExhaustion(AppleCoreAPI.accessor.getExhaustion(player), mc, left, top);
				}
				//Calculate the random jitter amount beforehand and pass it to the draw methods
				int[] jitterAmount = getJitterAmount(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), player);
				drawVanillaStats(jitterAmount, mc, player, left, top);
				drawExtendedStats(jitterAmount, mc, player, left, top);
				//air meter expects this to be done before it runs so it doesn't draw on top of hunger.
				GuiIngameForge.right_height += 10;
			}
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
	
	
		offset = GuiIngameForge.right_height;
	
		ScaledResolution res = evt.getResolution();
		int left = res.getScaledWidth()/2 + 91;
		int top = res.getScaledHeight() - offset;
		if(ModConfig.hud.style == DisplayStyle.OVERLAY)
		{
			if(ModConfig.hud.drawSaturation && (!ScalingFeast.hasAppleSkin || ModConfig.compat.appleskin.drawVanillaSatOverlay))
			{
				mc.getTextureManager().bindTexture(icons);
				drawExtendedSatBar(player.getFoodStats().getSaturationLevel(), mc, left, top+10, satColours.get(0));
				mc.getTextureManager().bindTexture(Gui.ICONS);
			}
			drawExtendedStats(getJitterAmount(mc.ingameGUI.getUpdateCounter(), player), mc, player, left, top + 10);
		}
		else
		{
			drawNumericalOverlay(mc, player, res, left, top + 10);
		}
	}
	
	private void drawNumericalOverlay(Minecraft mc, EntityPlayer player, ScaledResolution res, int left, int top)
	{
		if(FoodStatsMap.hasStats(player.getUniqueID()))
		{
			int hunger = FoodStatsMap.getExtraFoodLevel(player.getUniqueID());
			float sat = FoodStatsMap.getExtraSatLevels(player.getUniqueID()) + (!ScalingFeast.hasAppleSkin || ModConfig.compat.appleskin.drawVanillaSatOverlay ? player.getFoodStats().getSaturationLevel() : 0);
			int max = FoodStatsMap.getMaxFoodLevel(player.getUniqueID());
			String hungerInfo = String.format("+(%d/%d", hunger, max);
			String satInfo = String.format(", %.1f)", sat);
			String info = hungerInfo + (ModConfig.hud.drawSaturation ? satInfo : ")");
			GL11.glPushMatrix();
			GL11.glScalef(0.75f, 0.75f, 0.75f);
			mc.fontRenderer.drawStringWithShadow(info, left/0.75f + 1/0.75f, top/0.75f, getColour(hunger, max));
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glPopMatrix();
			mc.getTextureManager().bindTexture(Gui.ICONS);
		}
	}
	
	private void drawExtendedStats(int[] jitterAmount, Minecraft mc, EntityPlayer player, int left, int top)
	{
		
		if(FoodStatsMap.hasStats(player.getUniqueID()))
		{
			int hunger = FoodStatsMap.getExtraFoodLevel(player.getUniqueID());
			float sat = FoodStatsMap.getExtraSatLevels(player.getUniqueID());
			int max = FoodStatsMap.getMaxFoodLevel(player.getUniqueID());
			if(hunger > 0)
			{
				drawExtendedFood(hunger, mc, left, top, jitterAmount);
				if(sat > 0 && ModConfig.hud.drawSaturation)
				{
					drawExtendedSat(sat, mc, left, top, jitterAmount);
				}
				if(hunger >= max - max%20)
				{
					if(max % 20 != 0)
						drawMax(max%20, mc, left, top, jitterAmount[(int) Math.ceil((max%20)/2.0f) - 1]);
					else
						drawMax(19, mc, left, top, jitterAmount[9]);
				}
				drawNumericalInfo(hunger / 20 + 2, mc, left, top + 2, hunger, max);
			}
		}
		else
		{
			drawNumericalInfo(1, mc, left, top + 2, player.getFoodStats().getFoodLevel(), 20);
		}
	}

	private void drawNumericalInfo(int i, Minecraft mc, int left, int top, int hunger, int max)
	{
		GL11.glPushMatrix();
		GL11.glScalef(0.75f, 0.75f, 0.75f);
		mc.fontRenderer.drawStringWithShadow("x"+i, left/0.75f + 1/0.75f, top/0.75f, getColour(hunger, max));
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}

	private void drawExtendedFood(int hunger, Minecraft mc, int left, int top, int[] jitterAmount) 
	{
		mc.getTextureManager().bindTexture(icons);
		mc.mcProfiler.startSection("extendedFood");
		GlStateManager.enableBlend();
		int colourIndex = 0;
		do
		{
			hunger -= 20;
			Colour c = colours.get((colourIndex++) % colours.size());
			if(hunger >= 0)
			{
				drawExtendedFoodBar(20, mc, left, top, jitterAmount, c);
			}
			else
			{
				drawExtendedFoodBar(hunger + 20, mc, left, top, jitterAmount, c);
			}
		}while(hunger > -20);
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	private void drawExtendedFoodBar(int amount, Minecraft mc, int left, int top, int[] jitterAmount, Colour colour)
	{
		float shanksNeeded = amount/2.0f;
		int x;
		int y = top;
		int i;
		boolean hungerEffect = mc.player.isPotionActive(MobEffects.HUNGER);
		float red = 1f/255 * colour.getR();
		float green = 1f/255 * colour.getG();
		float blue = 1f/255 * colour.getB();
		for(i = 0; i < (int)shanksNeeded; i++)
		{
			x = left - i * 8 - 9;
			y = top + jitterAmount[i];
			GL11.glColor3f(red, green, blue);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 0, 0, 9, 9);
			GL11.glColor3f(1.0f,  1.0f, 1.0f);
			if(hungerEffect)
			{
				mc.ingameGUI.drawTexturedModalRect((float)x, y, 18, 0, 9, 9);
			}
		}
		//only true if we need a half shank
		if((int)shanksNeeded < shanksNeeded)
		{
			x = left - i * 8 - 9;
			y = top + jitterAmount[i];
			GL11.glColor3f(red, green, blue);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 9, 0, 9, 9);
			GL11.glColor3f(1.0f,  1.0f, 1.0f);
			if(hungerEffect)
			{
				mc.ingameGUI.drawTexturedModalRect((float)x, y, 27, 0, 9, 9);
			}
		}
	}
	private void drawExtendedSat(float sat, Minecraft mc, int left, int top, int[] jitterAmount) 
	{
		mc.getTextureManager().bindTexture(icons);
		mc.mcProfiler.startSection("extendedSaturation");
		GlStateManager.enableBlend();
		int colourIndex = (ModConfig.compat.appleskin.drawVanillaSatOverlay ? 0 : 1);
		do
		{
			sat -= 20;
			Colour c = satColours.get((colourIndex++) % satColours.size());
			if(sat >= 0)
			{
				drawExtendedSatBar(20, mc, left, top, c);
			}
			else
			{
				drawExtendedSatBar(sat + 20, mc, left, top, c);
			}
		}while(sat > -20);
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	private void drawExtendedSatBar(float amount, Minecraft mc, int left, int top, Colour colour)
	{
		float barsNeeded = amount/2;
		int x, y = top, i;
		float red = 1f/255 * colour.getR();
		float green = 1f/255 * colour.getG();
		float blue = 1f/255 * colour.getB();
		for(i = 0; i < (int)barsNeeded; i++)
		{
			x = left - i * 8 - 9;
			y = top;
			GL11.glColor3f(red, green, blue);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 0, 9, 9, 9);
			GL11.glColor3f(1.0f,  1.0f, 1.0f);
		}
		x = left - i * 8 - 9;
		float leftover = barsNeeded - (int)barsNeeded;
		int u = 0;
		if(leftover > 0.5)
		{
			u = 9;
		}
		else if(leftover > 0.25)
		{
			u = 18;
		}
		else if(leftover > 0)
		{
			u = 27;
		}
		else
		{
			return;
		}
		GL11.glColor3f(red, green, blue);
		mc.ingameGUI.drawTexturedModalRect((float)x, y, u, 9, 9, 9);
		GL11.glColor3f(1.0f,  1.0f, 1.0f);
	}
	
	private void drawMax(int max, Minecraft mc, int left, int top, int jitter) 
	{
		mc.getTextureManager().bindTexture(icons);
		mc.mcProfiler.startSection("extendedMax");
		GlStateManager.enableBlend();
		int x = 0;
		int y = top + jitter;
		for(int i = 0; i < (int)max/2.0f; i++)
		{
			x = left - i * 8 - 9;
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		mc.ingameGUI.drawTexturedModalRect((float)x, y, 36, 9, 9, 9);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	
	private void drawVanillaStats(int[] jitterAmount, Minecraft mc, EntityPlayer player, int left, int top)
	{
		//Draw empty hunger bar
		mc.getTextureManager().bindTexture(Gui.ICONS);
		mc.mcProfiler.startSection("overriddenVanillaHunger");
		GlStateManager.enableBlend();
		float shanks = player.getFoodStats().getFoodLevel()/2.0f;
		int u = 16 + (player.isPotionActive(MobEffects.HUNGER) ? 117 : 0);
		int v = 27;
		for(int i = 0; i < 10; i++)
		{
			int x = left - i * 8 - 9;
			int y = top + (jitterAmount != null ? jitterAmount[i] : 0);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, u, v, 9, 9);
		}
		//draw food stats
		u = 52 + (player.isPotionActive(MobEffects.HUNGER) ? 36 : 0);
		int x;
		int i;
		for(i = 0; i < (int)shanks; i++)
		{
			x = left - i * 8 - 9;
			int y = top + (jitterAmount != null ? jitterAmount[i] : 0);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, u, v, 9, 9);
		}
		if((int)shanks < shanks)
		{
			x = left - i * 8 - 9;
			int y = top + (jitterAmount != null ? jitterAmount[i] : 0);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, u + 9, v, 9, 9);
		}
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
	}
	
	private void drawExhaustion(float exhaustion, Minecraft mc, int left, int top) 
	{
		mc.getTextureManager().bindTexture(icons);
		mc.mcProfiler.startSection("overriddenExhaustion");
		GlStateManager.enableBlend();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 0.75f);
		float max = AppleCoreAPI.accessor.getMaxExhaustion(mc.player);
		float ratio = exhaustion/max;
		int width = (int)(ratio * 81);
		mc.ingameGUI.drawTexturedModalRect(left - (int)(ratio*81), top, 81 - width, 18, width, 9);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	
	private int[] getJitterAmount(int updateCounter, EntityPlayer player)
	{
		int foodLevel = player.getFoodStats().getFoodLevel();
		float satLevel = player.getFoodStats().getSaturationLevel();
		int[] jitterAmount = new int[10];
		if(updateCounter % (foodLevel * 3 + 1) == 0 && satLevel == 0)
		{
			for(int i = 0; i < 10; i++)
			{
				jitterAmount[i] += rand.nextInt(3) - 1;
			}
		}
		return jitterAmount;
	}
	
	private int getColour(int hunger, int max)
	{
		if(hunger == max)
		{
			return 0x00aa00;
		}
		else if (hunger > 0.75 * max)
		{
			return 0x55ff55;
		}
		else if (hunger > 0.5 * max)
		{
			return 0xffff55;
		}
		else if (hunger > 0.25 * max)
		{
			return 0xff5555;
		}
		else
		{
			return 0xaa0000;
		}
	}
	
	private ArrayList<Colour> colourize(String[] arr) 
	{
		ArrayList<Colour> lst = new ArrayList<Colour>();
		for(String hex : arr)
		{
			lst.add(new Colour(hex));
		}
		return lst;
	}
	
	private boolean isEmpty(String[] arr)
	{
		for(String str : arr)
		{
			if(str != null)
			{
				return false;
			}
		}
		return true;
	}
}
