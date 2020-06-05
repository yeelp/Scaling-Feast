package yeelp.scalingfeast.handlers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.ModConfig.HUDCategory.DisplayStyle;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.helpers.AppleSkinHelper;

@SideOnly(Side.CLIENT)
public class CompatibilityHandler extends Handler
{	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@Optional.Method(modid = ModConsts.APPLESKIN_ID)
	public void onRenderTooltip(RenderTooltipEvent.PostText evt)
	{
		if(AppleSkinHelper.isLoaded() && ModConfig.hud.style == DisplayStyle.OVERLAY)
		{
			AppleSkinHelper.disableAppleCoreRecognition();
		}
	}
}