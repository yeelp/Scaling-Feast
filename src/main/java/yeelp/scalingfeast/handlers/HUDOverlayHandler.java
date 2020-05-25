package yeelp.scalingfeast.handlers;

import java.lang.reflect.Field;
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
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConfig.HUDCategory.DisplayStyle;
import yeelp.scalingfeast.ModConfig.HUDCategory.InfoStyle;
import yeelp.scalingfeast.ModConfig.HUDCategory.MaxColourStyle;
import yeelp.scalingfeast.ModConfig.HUDCategory.TrackerStyle;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.helpers.AppleSkinHelper;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.items.HeartyShankItem;
import yeelp.scalingfeast.util.Colour;
import yeelp.scalingfeast.util.FoodCapModifierProvider;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.SaturationUtil;
import yeelp.scalingfeast.util.StarvationTrackerProvider;

@SideOnly(Side.CLIENT)
public class HUDOverlayHandler extends Handler
{
	private static ResourceLocation icons;
	private static final ResourceLocation STANDARD = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielements.png");
	private static final ResourceLocation REVERSE = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielementsalt.png");
	protected int offset;
	private static ArrayList<Colour> colours = new ArrayList<Colour>();
	private static ArrayList<Colour> satColours = new ArrayList<Colour>();
	private Random rand = new Random();
	private boolean appleSkinErr = false;
	private static int satColour = 0xffff55;
	private static int satColourEmpty = 0x555555;
	
	public HUDOverlayHandler()
	{
		setIcons();
		loadColours();
		loadTextColours();
	}

	public static void setIcons()
	{
		switch(ModConfig.hud.overlayStyle)
		{
			case DEFAULT:
				icons = STANDARD;
				break;
			case REVERSED:
				icons = REVERSE;
				break;
		}
	}
	
	public static void loadColours()
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
	
	public static void loadTextColours()
	{
		try
		{
			satColour = Integer.decode("0x"+ModConfig.hud.satTextColour);
		}
		catch(NumberFormatException e)
		{
			ScalingFeast.err("Error setting saturation text colour! " + "0x"+ModConfig.hud.satTextColour + " isn't a valid colour!");
			e.printStackTrace();
		}
		try
		{
			satColourEmpty = Integer.decode("0x"+ModConfig.hud.satTextColourEmpty);
		}
		catch(NumberFormatException e)
		{
			ScalingFeast.err("Error setting empty saturation text colour! " + "0x"+ModConfig.hud.satTextColourEmpty + " isn't a valid colour!");
			e.printStackTrace();
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
				if(AppleSkinHelper.isLoaded() && !appleSkinErr)
				{
					appleSkinErr = !AppleSkinHelper.drawExhaustion(AppleCoreAPI.accessor.getExhaustion(player), mc, left, top, 0);
				}
				//Calculate the random jitter amount beforehand and pass it to the draw methods
				int[] jitterAmount = getJitterAmount(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), player);
				drawStatsOverlay(jitterAmount, mc, player, left, top);
				//air meter expects this to be done before it runs so it doesn't draw on top of hunger.
				GuiIngameForge.right_height += 10;
				if(ModConfig.compat.shouldFirePost)
				{
					MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(evt, RenderGameOverlayEvent.ElementType.FOOD));
				}
			}
		}
	}
	
	private void drawStatsOverlay(int[] jitterAmount, Minecraft mc, EntityPlayer player, int left, int top)
	{
		boolean isHungerEffectActive = player.isPotionActive(MobEffects.HUNGER);
		int hunger = player.getFoodStats().getFoodLevel();
		float sat = player.getFoodStats().getSaturationLevel();
		int max = ScalingFeastAPI.accessor.getModifiedFoodCap(player);
		int ticks = ScalingFeastAPI.accessor.getStarvationTracker(player).getCount();
		//Get the number of full bars to draw
		int numBars = hunger/ModConsts.VANILLA_MAX_HUNGER;
		int remainingShanks = hunger % ModConsts.VANILLA_MAX_HUNGER;
		//First, draw the empty bar.
		mc.getTextureManager().bindTexture(Gui.ICONS);
		drawStatBar(jitterAmount, mc, left, top, (max < ModConsts.VANILLA_MAX_HUNGER ? (int)(2*Math.ceil(max/2.0f)) : ModConsts.VANILLA_MAX_HUNGER), 16 + (player.isPotionActive(MobEffects.HUNGER) ? 117 : 0), 27, true, false, false, isHungerEffectActive, null);
		int i = 0;
		int colourIndex = 0;
		for(i = 0; i < numBars; i++)
		{
			mc.mcProfiler.startSection("Food Bar: "+(i+1));
			if(i == 0)
			{
				mc.getTextureManager().bindTexture(Gui.ICONS);
				drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_HUNGER, 52 + (player.isPotionActive(MobEffects.HUNGER) ? 36 : 0), 27, true, false, false, isHungerEffectActive, null);
				if(ModConfig.hud.replaceVanilla)
				{
					mc.mcProfiler.endStartSection("Vanilla Override");
					mc.getTextureManager().bindTexture(icons);
					drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_HUNGER, 0, 0, false, false, false, isHungerEffectActive, colours.get((colourIndex++) % colours.size()));
				}
			}
			else
			{
				mc.getTextureManager().bindTexture(icons);
				drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_HUNGER, 0, 0, false, false, false, isHungerEffectActive, colours.get((colourIndex++) % colours.size()));
			}
			mc.mcProfiler.endSection();
		}
		if(remainingShanks > 0)
		{
			mc.mcProfiler.startSection("Food Bar: "+(i+1));
			mc.getTextureManager().bindTexture((hunger < ModConsts.VANILLA_MAX_HUNGER ? Gui.ICONS : icons));
			int u = (hunger < ModConsts.VANILLA_MAX_HUNGER ? 52 + (player.isPotionActive(MobEffects.HUNGER) ? 36 : 0) : 0);
			int v = (hunger < ModConsts.VANILLA_MAX_HUNGER ? 27 : 0);
			drawStatBar(jitterAmount, mc, left, top, remainingShanks, u, v, hunger < ModConsts.VANILLA_MAX_HUNGER, false, false, isHungerEffectActive, colours.get((colourIndex++) % colours.size()));
			if(ModConfig.hud.replaceVanilla && hunger < ModConsts.VANILLA_MAX_HUNGER)
			{
				mc.mcProfiler.endStartSection("Vanilla Leftovers Override");
				mc.getTextureManager().bindTexture(icons);
				drawStatBar(jitterAmount, mc, left, top, remainingShanks, 0, 0, false, true, false, isHungerEffectActive, colours.get(0));
			}
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
				drawStatBar(jitterAmount, mc, left, top, ModConsts.VANILLA_MAX_SAT, 0, 9, false, false, true, false, satColours.get((colourIndex++) % satColours.size()));
				mc.mcProfiler.endSection();
			}
			if(remainingSat > 0)
			{
				mc.mcProfiler.startSection("Sat: " + (i+1));
				drawStatBar(jitterAmount, mc, left, top, remainingSat, 0, 9, false, false, true, false, satColours.get((colourIndex++) % satColours.size()));
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
		if(ModConfig.hud.trackerStyle == TrackerStyle.SATURATION && ModConfig.foodCap.starve.lossFreq > 1)
		{
			drawStatBar(jitterAmount, mc, left, top, (20.0f/(ModConfig.foodCap.starve.lossFreq-1))*ticks, 0, 9, false, false, true, false, new Colour("aa0000"));
		}
		mc.getTextureManager().bindTexture(Gui.ICONS);
		if(ModConfig.hud.style == DisplayStyle.OVERLAY)
		{
			switch(ModConfig.hud.infoStyle)
			{
				case SIMPLE:
					drawSimpleInfo((int)Math.ceil((float)hunger/ModConsts.VANILLA_MAX_HUNGER), mc, left, top, hunger, max);
					break;
				case ADVANCED:
					drawAdvancedInfo(mc, player, left, top);
					break;
			}
		}
	}

	private void drawSimpleInfo(int i, Minecraft mc, int left, int top, int hunger, int max)
	{
		GL11.glPushMatrix();
		GL11.glScalef(0.6f, 0.6f, 1.0f);
		mc.fontRenderer.drawStringWithShadow("x"+i+"/"+(int)Math.ceil((float)max/ModConsts.VANILLA_MAX_HUNGER), left/0.6f + 1/0.6f, top/0.6f + 4.5f/0.6f, getColour(hunger, max));
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	
	private void drawAdvancedInfo(Minecraft mc, EntityPlayer player, int left, int top)
	{
		int hunger = player.getFoodStats().getFoodLevel();
		float sat = player.getFoodStats().getSaturationLevel();
		int max = ScalingFeastAPI.accessor.getModifiedFoodCap(player);
		float maxSat = SaturationUtil.getSaturationCapForPlayer(player);
		String foodAddition = "";
		String maxAddition = "";
		String satAddition = "";
		String maxSatAddition = "";
		EnumHand hand = getHandWithFood(player);
		if(hand != null && AppleCoreAPI.accessor.canPlayerEatFood(player.getHeldItem(hand), player))
		{
			ItemStack heldStack = player.getHeldItem(hand);
			FoodValues foodValues = AppleCoreAPI.accessor.getFoodValuesForPlayer(heldStack, player);
			int deltaHunger = Math.min(foodValues.hunger, max - hunger);
			float deltaSat = Math.min(foodValues.getUnboundedSaturationIncrement(), hunger + deltaHunger - sat);
			if(deltaHunger > 0)
			{
				foodAddition = "+"+Integer.toString(deltaHunger);
			}
			if(deltaSat > 0)
			{
				satAddition = String.format("+%.1f", deltaSat);
			}
			if(heldStack.getItem() instanceof HeartyShankItem)
			{
				maxAddition = "+"+Integer.toString(ModConfig.foodCap.inc);
				maxSatAddition = String.format("+%.1f", SaturationUtil.capSaturationValue(maxSat + ModConfig.foodCap.inc) - maxSat);
				if(maxSatAddition.equals("+0.0"))
				{
					maxSatAddition = "";
				}
			}
		}
		String hungerInfo = String.format("%d%s/%d%s", hunger, foodAddition, max, maxAddition);
		String satInfo = String.format("%.1f%s/%.1f%s", sat, satAddition, maxSat, maxSatAddition).trim();
		GL11.glPushMatrix();
		GL11.glTranslatef(ModConfig.hud.infoXOffset, ModConfig.hud.infoYOffset, 0);
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		if(ModConfig.hud.drawSaturation)
		{	
			mc.fontRenderer.drawStringWithShadow(satInfo, (left+1)/0.5f, top/0.5f, (sat > 0 ? satColour : satColourEmpty));
		}
		mc.fontRenderer.drawStringWithShadow(hungerInfo, (left+1)/0.5f, top/0.5f + 4.5f/0.5f, getColour(hunger, max));
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}

	private void drawStatBar(int[] jitterAmount, Minecraft mc, int left, int top, float amount, int u, int v, boolean vanilla, boolean vanillaOverride, boolean sat, boolean hungerEffectActive, Colour colour)
	{
		GlStateManager.enableBlend();
		//this is a one indexed value, the variable currShank will be a zero indexed value
		float shanksNeeded = amount/2.0f;
		int x, y = top, i;
		for(i = 0; i < shanksNeeded; i++)
		{
			x = left - i * 8 - 9;
			y = top + jitterAmount[i];
			drawIcon(mc, x, y, u, v, i, shanksNeeded, vanilla, vanillaOverride, sat, hungerEffectActive, colour);
		}
		GlStateManager.disableBlend();
	}

	private void drawIcon(Minecraft mc, int x, int y, int u, int v, int currShank, float shanksNeeded, boolean vanilla, boolean vanillaOverride, boolean sat, boolean hungerEffectActive, Colour colour)
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
				u += (vanillaOverride ? 18 : 9);
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
		GlStateManager.enableAlpha();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int x = 0;
		int y = top + jitter;
		for(int i = 0; i < (int)max/2.0f; i++)
		{
			x = left - i * 8 - 9;
		}
		int hunger = mc.player.getFoodStats().getFoodLevel();
		int foodMax = ScalingFeastAPI.accessor.getModifiedFoodCap(mc.player);
		float alpha = (hunger < 20*Math.floor(foodMax/20.0f) && hunger > 0 && foodMax > ModConsts.VANILLA_MAX_HUNGER? (float)ModConfig.hud.maxOutlineTransparency : 1.0f);
		Colour maxColour = getMaxColour(ticks, ModConfig.foodCap.starve.lossFreq);
		GL11.glColor4f(1.0f/255*maxColour.getR(), 1.0f/255*maxColour.getG(), 1.0f/255*maxColour.getB(), alpha);
		
		if(ModConfig.hud.maxColourStyle == MaxColourStyle.CUSTOM)
		{
			//blend the start and end colours only if tick > 0, other wise, just draw start colour
			if(ticks > 0)
			{
				mc.ingameGUI.drawTexturedModalRect((float)x, y, 36, 9, 9, 9);
			}
			Colour overlayColour = new Colour(ModConfig.hud.maxColourStart);
			GL11.glColor4f(1.0f/255*overlayColour.getR(), 1.0f/255*overlayColour.getG(), 1.0f/255*overlayColour.getB(), (ticks + 1 < ModConfig.foodCap.starve.lossFreq ? ((float)ModConfig.foodCap.starve.lossFreq - ticks)/ModConfig.foodCap.starve.lossFreq : 0)*alpha);
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 36, 9, 9, 9);
		}
		else
		{
			mc.ingameGUI.drawTexturedModalRect((float)x, y, 36, 9, 9, 9);
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableAlpha();
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
		if(satLevel == 0 || regen != -1)
		{
			for(int i = 0; i < 10; i++)
			{
				if(updateCounter % (foodLevel * 3 + 1) == 0 && satLevel == 0)
				{
					jitterAmount[i] += rand.nextInt(3) - 1;
				}
				if(i == regen)
				{
					jitterAmount[i] -= 2;
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
		switch(ModConfig.hud.trackerStyle)
		{
			case MAX_COLOUR:		
				switch(ModConfig.hud.maxColourStyle)
				{
					case DEFAULT:
						if(maxTicks == 1 || ModConfig.foodCap.starve.starveLoss == 0)
						{
							return new Colour("FFFFFF");
						}
						else if(ticks + 1 == maxTicks)
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
					case CUSTOM:
						return new Colour(ModConfig.hud.maxColourEnd);
					//unreachable, but needed for JVM
					default:
						return null;
				}
			case SATURATION:
				return new Colour(ModConfig.hud.maxColourEnd);
			//again, unreachable, but needed for JVM
			default:
				return null;
		}
	}
	
	private static ArrayList<Colour> colourize(String[] arr) 
	{
		ArrayList<Colour> lst = new ArrayList<Colour>();
		for(String hex : arr)
		{
			lst.add(new Colour(hex));
		}
		return lst;
	}
	
	private static boolean isEmpty(String[] arr)
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
	
	private boolean isFoodAlwaysEdible(ItemFood heldItem)
	{
		Field edibility = null;
		try
		{
			edibility = ObfuscationReflectionHelper.findField(ItemFood.class, "field_77852_bZ");
		}
		catch(UnableToFindFieldException e)
		{
			//perhaps the field is deobfuscated?
			try
			{
				edibility = ObfuscationReflectionHelper.findField(ItemFood.class, "alwaysEdible"); 
			}
			catch(UnableToFindFieldException f)
			{
				//for now, silently ignore, no point spaming the console ~60 times a second for this (in fact it'd get blocked)
				return false;
			}
		}
		try
		{
			return edibility != null ? (boolean) edibility.getBoolean(heldItem) : false;
		}
		catch(IllegalAccessException e)
		{
			return false;
		}
	}

	private EnumHand getHandWithFood(EntityPlayer player)
	{
		if(player.getHeldItemMainhand().getItem() instanceof ItemFood)
		{
			return EnumHand.MAIN_HAND;
		}
		else if(player.getHeldItemOffhand().getItem() instanceof ItemFood)
		{
			return EnumHand.OFF_HAND;
		}
		else
		{
			return null;
		}
	}
}
