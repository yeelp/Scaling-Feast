package yeelp.scalingfeast.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
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
	private static final ResourceLocation POTIONS = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/potioneffects/potionicons.png");
	private boolean isInstant;
	/**
	 * Build a new Potion
	 * @param badEffectIn is the effect bad?
	 * @param colour colour of the particles
	 * @param x icon index x
	 * @param y icon index y
	 * @param isInstant is the effect instant (e.g. Harming)
	 */
	public PotionBase(boolean badEffectIn, int colour, int x, int y, boolean isInstant)
	{
		super(badEffectIn, colour);
		this.setIconIndex(x, y);
		this.isInstant = isInstant;
	}
	
	@Override
	public boolean isInstant()
	{
		return this.isInstant;
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		mc.getTextureManager().bindTexture(POTIONS);
		int iconIndex = this.getStatusIconIndex();
		mc.currentScreen.drawTexturedModalRect((float)x + 6, y + 7, iconIndex * 18, 0, 18, 18);
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
	
	@SuppressWarnings("deprecation")
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
