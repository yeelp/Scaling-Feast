package yeelp.scalingfeast.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.config.ModConfig.HUDCategory.OverlayStyle;

@SideOnly(Side.CLIENT)
public enum GUIIcons {
	VANILLA(Gui.ICONS),
	STANDARD(new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielements.png")),
	REVERSED(new ResourceLocation(ModConsts.MOD_ID, "textures/gui/guielementsalt.png"));
	
	private final ResourceLocation loc;
	
	GUIIcons(ResourceLocation loc) {
		this.loc = loc;
	}
	
	private void bindTexture(Minecraft mc) {
		mc.getTextureManager().bindTexture(this.loc);
	}
	
	public static void unbindSFGUIIcons(Minecraft mc) {
		VANILLA.bindTexture(mc);
	}
	
	public static void bindSFGUIIcons(Minecraft mc) {
		if(ModConfig.hud.overlayStyle.equals(OverlayStyle.DEFAULT)) {
			STANDARD.bindTexture(mc);
		}
		else {
			REVERSED.bindTexture(mc);
		}
	}
}
