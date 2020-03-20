package yeelp.scalingfeast.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.scalingfeast.ModConsts;

/**
 * Base class to derive potions from.
 * @author Yeelp
 *
 */
public abstract class PotionBase extends Potion 
{
	public static final ResourceLocation POTIONS = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/potioneffects/potionicons.png");
	public PotionBase(boolean badEffectIn, int colour, int x, int y)
	{
		super(badEffectIn, colour);
		this.setIconIndex(x, y);
	}
	
	@Override 
	public boolean hasStatusIcon()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		mc.getTextureManager().bindTexture(POTIONS);
		int iconIndex = this.getStatusIconIndex();
		mc.currentScreen.drawTexturedModalRect((float)x + 6, y + 7, iconIndex * 18, 0, 18, 18);
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		mc.getTextureManager().bindTexture(POTIONS);
		int iconIndex = this.getStatusIconIndex();
		mc.ingameGUI.drawTexturedModalRect((float)x + 3, y + 3, iconIndex * 18, 0, 18, 18);
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
}
