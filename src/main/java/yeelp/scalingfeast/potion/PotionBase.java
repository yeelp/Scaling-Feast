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
 * 
 * @author Yeelp
 *
 */
public abstract class PotionBase extends Potion {
	private static final ResourceLocation POTIONS = new ResourceLocation(ModConsts.MOD_ID, "textures/gui/potioneffects/potionicons.png");
	private boolean isInstant;

	/**
	 * Build a new Potion
	 * 
	 * @param badEffectIn is the effect bad?
	 * @param colour      colour of the particles
	 * @param x           icon index x
	 * @param y           icon index y
	 * @param isInstant   is the effect instant (e.g. Harming)
	 */
	public PotionBase(boolean badEffectIn, int colour, int x, int y, boolean isInstant) {
		super(badEffectIn, colour);
		this.setIconIndex(x, y);
		this.isInstant = isInstant;
	}

	@Override
	public boolean isInstant() {
		return this.isInstant;
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(POTIONS);
		int iconIndex = this.getStatusIconIndex();
		gui.drawTexturedModalRect((float) x + 6, y + 7, iconIndex * 18, 0, 18, 18);
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(POTIONS);
		int iconIndex = this.getStatusIconIndex();
		mc.ingameGUI.drawTexturedModalRect((float) x + 3, y + 3, iconIndex * 18, 0, 18, 18);
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}

}
