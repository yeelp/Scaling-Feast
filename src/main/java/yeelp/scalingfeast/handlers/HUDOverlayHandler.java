package yeelp.scalingfeast.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConfig.HUDCategory.DisplayStyle;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.util.Colour;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

@SideOnly(Side.CLIENT)
public class HUDOverlayHandler extends Handler
{
	private static final ResourceLocation icons = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielements.png");
	protected int offset;
	ArrayList<Colour> colours = new ArrayList<Colour>();
	ArrayList<Colour> satColours = new ArrayList<Colour>();
	Random rand = new Random();
	private boolean err = false;
	
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
			if(ModConfig.hud.style != DisplayStyle.DEFAULT)
			{
				//In order to make the HUD look nice, we need to completely remove
				//The hunger bar and re draw it so we have control over the 'jittering'.
				evt.setCanceled(true);
				Minecraft mc = Minecraft.getMinecraft();
				EntityPlayer player = mc.player;
				
				ScaledResolution res = evt.getResolution();
				
				offset = GuiIngameForge.right_height;
				
				int left = res.getScaledWidth()/2 + 91;
				int top = res.getScaledHeight() - offset;
				//If we have AppleSkin, we need to redraw the whole exhaustion bar.
				if(ScalingFeast.hasAppleSkin && ModConfig.compat.appleskin.drawExhaustion && !err)
				{
					try
					{
						Class appleskin = Class.forName("squeek.appleskin.client.HUDOverlayHandler");
						appleskin.getDeclaredMethod("drawExhaustionOverlay", float.class, Minecraft.class, int.class, int.class, float.class).invoke(null, AppleCoreAPI.accessor.getExhaustion(player), mc, left, top, 1f); 
					}
					catch (NoSuchMethodException e) 
					{
						e.printStackTrace();
						err = true;
					} 
					catch (SecurityException e) 
					{
						e.printStackTrace();
						err = true;
					} 
					catch (IllegalAccessException e) 
					{
						e.printStackTrace();
						err = true;
					} 
					catch (IllegalArgumentException e) 
					{
						e.printStackTrace();
						err = true;
					} 
					catch (InvocationTargetException e) 
					{
						e.printStackTrace();
						err = true;
					}
					catch(ClassNotFoundException e)
					{
						ScalingFeast.err("Class not found");
						e.printStackTrace();
						err = true;
					}
					//drawExhaustion(AppleCoreAPI.accessor.getExhaustion(player), mc, left, top);
				}
				//Calculate the random jitter amount beforehand and pass it to the draw methods
				int[] jitterAmount = getJitterAmount(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), player);
				drawStatsOverlay(jitterAmount, mc, player, left, top);
				if(ModConfig.hud.style == DisplayStyle.NUMERICAL)
				{
					drawNumericalOverlay(mc, player, res, left, top );
				}
				//air meter expects this to be done before it runs so it doesn't draw on top of hunger.
				GuiIngameForge.right_height += 10;
				if(ModConfig.compat.shouldFirePost)
				{
					MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(evt, RenderGameOverlayEvent.ElementType.FOOD));
				}
			}
		}
	}
	
	private void drawNumericalOverlay(Minecraft mc, EntityPlayer player, ScaledResolution res, int left, int top)
	{
		int hunger = player.getFoodStats().getFoodLevel();
		float sat = player.getFoodStats().getSaturationLevel();
		int max = player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel();
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
	
	private void drawStatsOverlay(int[] jitterAmount, Minecraft mc, EntityPlayer player, int left, int top)
	{
		boolean isHungerEffectActive = player.isPotionActive(MobEffects.HUNGER);
		int hunger = player.getFoodStats().getFoodLevel();
		float sat = player.getFoodStats().getSaturationLevel();
		int max = player.getCapability(FoodCapProvider.capFoodStat, null).getMaxFoodLevel();
		int ticks = player.getCapability(StarvationTrackerProvider.starvationTracker, null).getCount();
		//Get the number of full bars to draw
		int numBars = hunger/ModConsts.VANILLA_MAX_HUNGER;
		int remainingShanks = hunger % ModConsts.VANILLA_MAX_HUNGER;
		//First, draw the empty bar.
		mc.getTextureManager().bindTexture(Gui.ICONS);
		drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_HUNGER, 16 + (player.isPotionActive(MobEffects.HUNGER) ? 117 : 0), 27, true, false, isHungerEffectActive, null);
		int i = 0;
		int colourIndex = 0;
		for(i = 0; i < numBars; i++)
		{
			mc.mcProfiler.startSection("Food Bar: "+(i+1));
			if(i == 0)
			{
				mc.getTextureManager().bindTexture(Gui.ICONS);
				drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_HUNGER, 52 + (player.isPotionActive(MobEffects.HUNGER) ? 36 : 0), 27, true, false, isHungerEffectActive, null); 
				if(ModConfig.hud.style == DisplayStyle.NUMERICAL)
				{
					break;
				}
			}
			else
			{
				mc.getTextureManager().bindTexture(icons);
				drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_HUNGER, 0, 0, false, false, isHungerEffectActive, colours.get((colourIndex++) % colours.size()));
			}
			mc.mcProfiler.endSection();
		}
		/* 
		 * only draw remaining shanks if
		 * style set to NUMERICAL and total hunger < 20
		 * style set to OVERLAY and there are leftover shanks to draw 
		 */
		if(remainingShanks > 0 && (hunger < ModConsts.VANILLA_MAX_HUNGER || !(ModConfig.hud.style == DisplayStyle.NUMERICAL)))
		{
			mc.mcProfiler.startSection("Food Bar: "+(i+1));
			mc.getTextureManager().bindTexture((hunger < ModConsts.VANILLA_MAX_HUNGER ? Gui.ICONS : icons));
			int u = (hunger < ModConsts.VANILLA_MAX_HUNGER ? 52 + (player.isPotionActive(MobEffects.HUNGER) ? 36 : 0) : 0);
			int v = (hunger < ModConsts.VANILLA_MAX_HUNGER ? 27 : 0);
			drawStatBar(jitterAmount, mc, left, top, remainingShanks, u, v, hunger < ModConsts.VANILLA_MAX_HUNGER, false, isHungerEffectActive, colours.get((colourIndex++) % colours.size())); 
			mc.mcProfiler.endSection();
		}
		if(ModConfig.hud.drawSaturation && ModConfig.hud.style == DisplayStyle.OVERLAY)
		{
			colourIndex = 0;
			numBars = (int)(sat/ModConsts.VANILLA_MAX_SAT);
			float remainingSat = sat%ModConsts.VANILLA_MAX_SAT;
			mc.getTextureManager().bindTexture(icons);
			for(i = 0; i < numBars; i++)
			{
				mc.mcProfiler.startSection("Sat: "+(i+1));
				drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_SAT, 0, 9, false, true, false, satColours.get((colourIndex++) % satColours.size()));
				mc.mcProfiler.endSection();
			}
			if(remainingSat > 0)
			{
				mc.mcProfiler.startSection("Sat: " + (i+1));
				drawStatBar(jitterAmount, mc, left, top, remainingSat, 0, 9, false, true, false, satColours.get((colourIndex++) % satColours.size()));
				mc.mcProfiler.endSection();
			}
		}
		if(max % 20 != 0)
		{
			drawMax(max%20, ticks, mc, left, top, jitterAmount[(int) Math.ceil((max%20)/2.0f) - 1]);
		}
		else
		{
			drawMax(19, ticks, mc, left, top, jitterAmount[9]);
		}
		mc.getTextureManager().bindTexture(Gui.ICONS);
		if(ModConfig.hud.style == DisplayStyle.OVERLAY)
		{
			drawNumericalInfo(hunger/ModConsts.VANILLA_MAX_HUNGER + (hunger > 0 ? 1 : 0), mc, left, top, hunger, max);
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
	
	private void drawStatBar(int[] jitterAmount, Minecraft mc, int left, int top, float amount, int u, int v, boolean vanilla, boolean sat, boolean hungerEffectActive, Colour colour)
	{
		//this is a one indexed value, the variable currShank will be a zero indexed value
		float shanksNeeded = amount/2.0f;
		int x, y = top, i;
		for(i = 0; i < shanksNeeded; i++)
		{
			x = left - i * 8 - 9;
			y = top + jitterAmount[i];
			drawIcon(mc, x, y, u, v, i, shanksNeeded, vanilla, sat, hungerEffectActive, colour);
		}
	}

	private void drawIcon(Minecraft mc, int x, int y, int u, int v, int currShank, float shanksNeeded, boolean vanilla, boolean sat, boolean hungerEffectActive, Colour colour)
	{
		float leftover = shanksNeeded - currShank;
		if(colour != null && !vanilla)
		{
			GL11.glColor3f(1f/255*colour.getR(), 1f/255*colour.getG(), 1f/255*colour.getB());
		}
		if(leftover < 1)
		{
			
			if(!sat || leftover > 0.5)
			{
				u += 9;
			}
			else if(leftover > 0.25)
			{
				u += 18;
			}
			else if(leftover > 0)
			{
				u += 27;
			}
		}
		mc.ingameGUI.drawTexturedModalRect(x, y, u, v, 9, 9);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		if(hungerEffectActive && !vanilla && !sat)
		{
			mc.ingameGUI.drawTexturedModalRect(x, y, u + 27, v, 9, 9);
		}
	}
	
	private void drawMax(int max, int ticks, Minecraft mc, int left, int top, int jitter) 
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
		Colour maxColour = getMaxColour(ticks, ModConfig.foodCap.lossFreq);
		GL11.glColor3f(1.0f/255*maxColour.getR(), 1.0f/255*maxColour.getG(), 1.0f/255*maxColour.getB());
		mc.ingameGUI.drawTexturedModalRect((float)x, y, 36, 9, 9, 9);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GlStateManager.disableBlend();
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(Gui.ICONS);
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
		rand.setSeed(updateCounter * 70643);
		int foodLevel = player.getFoodStats().getFoodLevel();
		float satLevel = player.getFoodStats().getSaturationLevel();
		int[] jitterAmount = new int[10];
		int regen = -1;
		if(player.isPotionActive(SFPotion.metabolism))
		{
			regen = updateCounter % 25;
		}
		if(updateCounter % (foodLevel * 3 + 1) == 0 && satLevel == 0)
		{
			for(int i = 0; i < 10; i++)
			{
				jitterAmount[i] += rand.nextInt(3) - 1;
				if(i == regen)
				{
					jitterAmount[i] += 2;
				}
			}
		}
		return jitterAmount;
	}
	
	private int getColour(int hunger, int max)
	{
		if(hunger == max)
		{
			return 0x55ff55;
		}
		else if(hunger > 0.5 * max)
		{
			return 0xffffff;
		}
		else if(hunger > 0.25 * max)
		{
			return 0xffff55;
		}
		else if(hunger > 0.1 * max)
		{
			return 0xffaa00;
		}
		else if(hunger > 0)
		{
			return 0xff5555;
		}
		else
		{
			return 0xaa0000;
		}
	}
	
	private Colour getMaxColour(int ticks, int maxTicks)
	{
		if(ticks + 1 == maxTicks)
		{
			return new Colour("AA0000");
		}
		else if(ticks > 0.9 * maxTicks)
		{
			return new Colour("FF5555");
		}
		else if(ticks > 0.75 * maxTicks)
		{
			return new Colour("FFAA00");
		}
		else if(ticks > 0.5 * maxTicks)
		{
			return new Colour("FFFF55");
		}
		else if (ticks > 0)
		{
			return new Colour("FFFFFF");
		}
		else
		{
			return new Colour("55FF555");
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
